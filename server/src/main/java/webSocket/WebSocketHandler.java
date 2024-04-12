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
import javax.websocket.OnClose;
import java.io.IOException;
import java.util.Objects;
import javax.websocket.*;


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

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException {

        UserGameCommand msg = new Gson().fromJson(message, UserGameCommand.class);

        Integer gameID = null;
        ChessGame game = null;
        String authToken = null;
        String username = null;
        ChessGame.TeamColor playerColor = null;
        ServerMessage messageToBroadcast = null;
        String whiteUsername = null;
        String blackUsername = null;
        ServerMessage errorMessage = null;

        switch(msg.getCommandType()){
            case null:
                String errorMsg = msg.getAuthString();  //not actually an authString. is actually the error message
                errorMessage = new ErrorMessage(errorMsg);
                session.getRemote().sendString(new Gson().toJson(errorMessage));

                break;

            case UserGameCommand.CommandType.JOIN_PLAYER:
                JoinPlayerCommand joinPlayerCommand = new Gson().fromJson(message, JoinPlayerCommand.class);

                try{
                    gameID = joinPlayerCommand.gameID;
                    game = gameDAO.getChessGame(gameID);
                    authToken = joinPlayerCommand.getAuthString();
                    username = authDAO.getUsername(authToken);
                    playerColor = joinPlayerCommand.playerColor;
                }catch (Exception e){
                    errorMessage = new ErrorMessage("join info didn't work");
                    session.getRemote().sendString(new Gson().toJson(errorMessage));
                    return;
                }

                //load game to self
                session.getRemote().sendString(new Gson().toJson(new LoadGameMessage(game)));

                //notify all others
                messageToBroadcast = new NotificationMessage( username + " joined the game as the " + playerColor.toString() + " player." );
                webSocketSessions.addSessionToGame(gameID, authToken, session);
                webSocketSessions.broadcastSession(gameID, authToken, messageToBroadcast);

                break;

            case UserGameCommand.CommandType.JOIN_OBSERVER:
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

                break;

            case UserGameCommand.CommandType.MAKE_MOVE:
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
                    errorMessage = new ErrorMessage("invalid move");
                    session.getRemote().sendString(new Gson().toJson(errorMessage));
                    return;
                }

                //load game to all
                messageToBroadcast = new LoadGameMessage(game);
                webSocketSessions.broadcastSession(gameID, null, messageToBroadcast);

                //notify others
                messageToBroadcast = new NotificationMessage( username + " moved " + move.fancyToString() + "." );
                webSocketSessions.broadcastSession(gameID, authToken, messageToBroadcast);

                //check, checkmate, stalemate?
                boolean isInCheck = game.isInCheck(game.getTeamTurn());
                boolean isInCheckmate = game.isInCheckmate(game.getTeamTurn());
                boolean isInStalemate = game.isInStalemate(game.getTeamTurn());
                if(isInCheckmate){
                    messageToBroadcast = new NotificationMessage(username + " is in checkmate. Game over");
                    webSocketSessions.broadcastSession(gameID, null, messageToBroadcast);

                    gameDAO.removeUser(ChessGame.TeamColor.WHITE, gameID);
                    gameDAO.removeUser(ChessGame.TeamColor.BLACK, gameID);

                }else if(isInCheck){
                    messageToBroadcast = new NotificationMessage(username + " is in check.");
                    webSocketSessions.broadcastSession(gameID, null, messageToBroadcast);

                }else if(isInStalemate){
                    messageToBroadcast = new NotificationMessage("The game is in stalemate. Game over");
                    webSocketSessions.broadcastSession(gameID, null, messageToBroadcast);

                    gameDAO.removeUser(ChessGame.TeamColor.WHITE, gameID);
                    gameDAO.removeUser(ChessGame.TeamColor.BLACK, gameID);
                }

                break;

            case UserGameCommand.CommandType.LEAVE:
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

                break;

            case UserGameCommand.CommandType.RESIGN:
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

                break;
        }
    }

    /*
    @OnClose
    public void onClose(Integer gameID, String authToken) {
        System.out.println("Connection closed");
    }

     */
}