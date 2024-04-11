package webSocket;

import chess.*;
import com.google.gson.Gson;
import dataAccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import javax.websocket.OnClose;
import java.io.IOException;


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

        Integer gameID;
        ChessGame game;
        String authToken;
        String username;
        ChessGame.TeamColor playerColor;
        ServerMessage messageToBroadcast;


        switch(msg.getCommandType()){
            case UserGameCommand.CommandType.JOIN_PLAYER:
                JoinPlayerCommand joinPlayerCommand = new Gson().fromJson(message, JoinPlayerCommand.class);

                //load game to self
                gameID = joinPlayerCommand.gameID;
                game = gameDAO.getChessGame(gameID);
                session.getRemote().sendString(new Gson().toJson(new LoadGameMessage(game)));

                //notify all others
                authToken = joinPlayerCommand.getAuthString();
                username = authDAO.getUsername(authToken);
                playerColor = joinPlayerCommand.playerColor;
                //session.getRemote().sendString(new Gson().toJson(new NotificationMessage( username + " joined the game as the " + playerColor.toString() + " player." )));

                messageToBroadcast = new NotificationMessage( username + " joined the game as the " + playerColor.toString() + " player." );
                webSocketSessions.addSessionToGame(gameID, authToken, session);
                webSocketSessions.broadcastSession(gameID, authToken, messageToBroadcast);

                break;

            case UserGameCommand.CommandType.JOIN_OBSERVER:
                JoinObserverCommand joinObserverCommand = new Gson().fromJson(message, JoinObserverCommand.class);

                //load game to self
                gameID = joinObserverCommand.gameID;
                game = gameDAO.getChessGame(gameID);
                session.getRemote().sendString(new Gson().toJson(new LoadGameMessage(game)));

                //notify all others
                authToken = joinObserverCommand.getAuthString();
                username = authDAO.getUsername(authToken);
                messageToBroadcast = new NotificationMessage( username + " joined the game as an observer." );
                webSocketSessions.addSessionToGame(gameID, authToken, session);
                webSocketSessions.broadcastSession(gameID, authToken, messageToBroadcast);

                break;

            case UserGameCommand.CommandType.MAKE_MOVE:
                MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);

                //make the move
                gameID = makeMoveCommand.gameID;
                game = gameDAO.getChessGame(gameID);
                ChessMove move = makeMoveCommand.move;
                try {
                    game.makeMove(move);
                    gameDAO.setGame(gameID, game);
                }catch(InvalidMoveException e) {
                    throw e;
                }

                //load game to all
                game = gameDAO.getChessGame(gameID);
                messageToBroadcast = new LoadGameMessage(game);
                webSocketSessions.broadcastSession(gameID, null, messageToBroadcast);

                //notify others
                authToken = makeMoveCommand.getAuthString();
                username = authDAO.getUsername(authToken);
                messageToBroadcast = new NotificationMessage( username + " moved " + move.fancyToString() + "." );
                webSocketSessions.broadcastSession(gameID, authToken, messageToBroadcast);

                break;

            case UserGameCommand.CommandType.LEAVE:
                LeaveCommand leaveCommand = new Gson().fromJson(message, LeaveCommand.class);

                //notify all others
                gameID = leaveCommand.gameID;
                authToken = leaveCommand.getAuthString();
                username = authDAO.getUsername(authToken);
                messageToBroadcast = new NotificationMessage( username + " left the game." );
                webSocketSessions.removeSessionFromGame(gameID, authToken);
                webSocketSessions.broadcastSession(gameID, authToken, messageToBroadcast);

                break;

            case UserGameCommand.CommandType.RESIGN:
                ResignCommand resignCommand = new Gson().fromJson(message, ResignCommand.class);

                //notify all
                gameID = resignCommand.gameID;
                authToken = resignCommand.getAuthString();
                username = authDAO.getUsername(authToken);
                messageToBroadcast = new NotificationMessage( username + " resigned." );
                webSocketSessions.broadcastSession(gameID, null, messageToBroadcast);

                break;
        }
    }

    @OnClose
    public void onClose(Integer gameID, String authToken) {
        System.out.println("Connection closed");
    }
}