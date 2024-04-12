package webSocket;

import chess.*;
import com.google.gson.Gson;
import dataAccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;


@WebSocket
public class WebSocketHandler {
    private static WebSocketSessions webSocketSessions = new WebSocketSessions();
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public WebSocketHandler(){}

    public WebSocketHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
    }


    private Integer gameID = null;
    private ChessGame game = null;
    private String authToken = null;
    private String username = null;
    private ChessGame.TeamColor playerColor = null;
    private ServerMessage messageToBroadcast = null;
    private String whiteUsername = null;
    private String blackUsername = null;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {

        UserGameCommand msg = new Gson().fromJson(message, UserGameCommand.class);

        switch(msg.getCommandType()){
            case null:
                session.getRemote().sendString(new Gson().toJson(new ErrorMessage(msg.getAuthString())));   //not actually an authString. is actually the error message
                break;
            case UserGameCommand.CommandType.JOIN_PLAYER:
                joinPlayer(session, message);
                break;
            case UserGameCommand.CommandType.JOIN_OBSERVER:
                joinObserver(session, message);
                break;
            case UserGameCommand.CommandType.MAKE_MOVE:
                makeMove(session, message);
                break;
            case UserGameCommand.CommandType.LEAVE:
                leave(session, message);
                break;
            case UserGameCommand.CommandType.RESIGN:
                resign(session, message);
                break;
        }
    }

    private void joinPlayer(Session session, String message) throws DataAccessException, IOException {
        JoinPlayerCommand joinPlayerCommand = new Gson().fromJson(message, JoinPlayerCommand.class);

        gameID = joinPlayerCommand.gameID;
        game = gameDAO.getChessGame(gameID);
        authToken = joinPlayerCommand.getAuthString();
        username = authDAO.getUsername(authToken);
        playerColor = joinPlayerCommand.playerColor;

        //load game to self
        session.getRemote().sendString(new Gson().toJson(new LoadGameMessage(game)));

        //notify all others
        messageToBroadcast = new NotificationMessage( username + " joined the game as the " + playerColor.toString() + " player." );
        webSocketSessions.addSessionToGame(gameID, authToken, session);
        webSocketSessions.broadcastSession(gameID, authToken, messageToBroadcast);
    }
    private void joinObserver(Session session, String message) throws DataAccessException, IOException {
        JoinObserverCommand joinObserverCommand = new Gson().fromJson(message, JoinObserverCommand.class);

        gameID = joinObserverCommand.gameID;
        game = gameDAO.getChessGame(gameID);
        authToken = joinObserverCommand.getAuthString();
        username = authDAO.getUsername(authToken);

        //load game to self
        session.getRemote().sendString(new Gson().toJson(new LoadGameMessage(game)));

        //notify all others
        messageToBroadcast = new NotificationMessage( username + " joined the game as an observer." );
        webSocketSessions.addSessionToGame(gameID, authToken, session);
        webSocketSessions.broadcastSession(gameID, authToken, messageToBroadcast);
    }
    private void leave(Session session, String message) throws DataAccessException, IOException {
        LeaveCommand leaveCommand = new Gson().fromJson(message, LeaveCommand.class);

        gameID = leaveCommand.gameID;
        authToken = leaveCommand.getAuthString();
        username = authDAO.getUsername(authToken);

        //remove player from db
        whiteUsername = gameDAO.getPlayer(ChessGame.TeamColor.WHITE, gameID);
        blackUsername = gameDAO.getPlayer(ChessGame.TeamColor.BLACK, gameID);
        if (Objects.equals(username, whiteUsername)){
            gameDAO.removeUser(ChessGame.TeamColor.WHITE, gameID);
        }
        if (Objects.equals(username, blackUsername)){
            gameDAO.removeUser(ChessGame.TeamColor.BLACK, gameID);
        }

        //notify all others
        messageToBroadcast = new NotificationMessage( username + " left the game." );
        webSocketSessions.removeSessionFromGame(gameID, authToken);
        webSocketSessions.broadcastSession(gameID, authToken, messageToBroadcast);
    }
    private void makeMove(Session session, String message) throws DataAccessException, IOException{
        MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);

        gameID = makeMoveCommand.gameID;
        game = gameDAO.getChessGame(gameID);
        ChessMove move = makeMoveCommand.move;
        authToken = makeMoveCommand.getAuthString();
        username = authDAO.getUsername(authToken);

        //make the move
        try {
            game.makeMove(move);
            gameDAO.setGame(gameID, game);
        }catch(InvalidMoveException e) {
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("invalid move")));
            return;
        }

        //load game to all
        messageToBroadcast = new LoadGameMessage(game);
        webSocketSessions.broadcastSession(gameID, null, messageToBroadcast);

        //notify others
        messageToBroadcast = new NotificationMessage( username + " moved " + move.fancyToString() + "." );
        webSocketSessions.broadcastSession(gameID, authToken, messageToBroadcast);

        //check, checkmate, stalemate?
        checkGameStatus();
    }
    private void resign(Session session, String message) throws DataAccessException, IOException{
        ResignCommand resignCommand = new Gson().fromJson(message, ResignCommand.class);

        gameID = resignCommand.gameID;
        authToken = resignCommand.getAuthString();
        username = authDAO.getUsername(authToken);

        //make sure is a player
        whiteUsername = gameDAO.getPlayer(ChessGame.TeamColor.WHITE, gameID);
        blackUsername = gameDAO.getPlayer(ChessGame.TeamColor.BLACK, gameID);
        if (!Objects.equals(username, whiteUsername) && !Objects.equals(username, blackUsername)){
            session.getRemote().sendString(new Gson().toJson(new ErrorMessage("not a player")));
            return;
        }else{
            //remove players from db
            gameDAO.removeUser(ChessGame.TeamColor.WHITE, gameID);
            gameDAO.removeUser(ChessGame.TeamColor.BLACK, gameID);
        }

        //notify all
        messageToBroadcast = new NotificationMessage( username + " resigned. Game over" );
        webSocketSessions.broadcastSession(gameID, null, messageToBroadcast);
    }

    private void checkGameStatus() throws DataAccessException, IOException {
        boolean isInCheck = game.isInCheck(game.getTeamTurn());
        boolean isInCheckmate = game.isInCheckmate(game.getTeamTurn());
        boolean isInStalemate = game.isInStalemate(game.getTeamTurn());
        String atRiskUsername;
        if(isInCheckmate){
            if (game.getTeamTurn() == ChessGame.TeamColor.WHITE){atRiskUsername = gameDAO.getPlayer(ChessGame.TeamColor.WHITE, gameID);}
            else{atRiskUsername = gameDAO.getPlayer(ChessGame.TeamColor.BLACK, gameID);}

            messageToBroadcast = new NotificationMessage(atRiskUsername + " is in checkmate. Game over");
            webSocketSessions.broadcastSession(gameID, null, messageToBroadcast);

            gameDAO.removeUser(ChessGame.TeamColor.WHITE, gameID);
            gameDAO.removeUser(ChessGame.TeamColor.BLACK, gameID);

        }else if(isInCheck){
            if (game.getTeamTurn() == ChessGame.TeamColor.WHITE){atRiskUsername = gameDAO.getPlayer(ChessGame.TeamColor.WHITE, gameID);}
            else{atRiskUsername = gameDAO.getPlayer(ChessGame.TeamColor.BLACK, gameID);}

            messageToBroadcast = new NotificationMessage(atRiskUsername + " is in check.");
            webSocketSessions.broadcastSession(gameID, null, messageToBroadcast);

        }else if(isInStalemate){
            messageToBroadcast = new NotificationMessage("The game is in stalemate. Game over");
            webSocketSessions.broadcastSession(gameID, null, messageToBroadcast);

            gameDAO.removeUser(ChessGame.TeamColor.WHITE, gameID);
            gameDAO.removeUser(ChessGame.TeamColor.BLACK, gameID);
        }
    }
}