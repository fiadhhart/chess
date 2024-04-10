package webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import javax.websocket.OnClose;
import java.io.IOException;


@WebSocket
public class WebSocketHandler {
    private final WebSocketSessions webSocketSessions = new WebSocketSessions();
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
    public void onMessage(Session session, String message) throws IOException, DataAccessException {

        /*
        //echo
        System.out.printf("Received: %s", message);
        session.getRemote().sendString("WebSocket response: " + message);
        */

        UserGameCommand msg = new Gson().fromJson(message, UserGameCommand.class);
        //webSocketSessions.addSessionToGame(msg.getGameID(), msg.getAuthString(), session);

        switch(msg.getCommandType()){
            case UserGameCommand.CommandType.JOIN_PLAYER:
                JoinPlayerCommand joinPlayerCommand = new Gson().fromJson(message, JoinPlayerCommand.class);

                String authToken = joinPlayerCommand.getAuthString();
                ChessGame.TeamColor playerColor = joinPlayerCommand.playerColor;
                String username = authDAO.getUsername(authToken);

                ServerMessage notificationMessage = new NotificationMessage("Player " + username + "joined side " + playerColor.toString() + "." );
                session.getRemote().sendString(new Gson().toJson(notificationMessage) );

                //create new server message
                //send notification of load game to this session
                //send broadcast of the notification except self

                break;
            case UserGameCommand.CommandType.JOIN_OBSERVER:
                System.out.println("join_observer");
                //JoinObserver commandObj = new Gson().fromJson(message, JoinObserver.class);
                //GameService.joinObserver(commandObj.getAuthString(), commandObj.getGameID(), webSocketSessions);
                break;
            case UserGameCommand.CommandType.MAKE_MOVE:
                System.out.println("make_move");
                //MakeMove commandObj = new Gson().fromJson(message, MakeMove.class);
                //GameService.makeMove(commandObj.getAuthString(), commandObj.getGameID(), commandObj.getMove(), webSocketSessions);
                break;
            case UserGameCommand.CommandType.LEAVE:
                System.out.println("leave");
                //Leave commandObj = new Gson().fromJson(message, Leave.class);
                //GameService.leaveGame(commandObj.getAuthString(), commandObj.getGameID(), webSocketSessions);
                break;
            case UserGameCommand.CommandType.RESIGN:
                System.out.println("resign");
                //Resign commandObj = new Gson().fromJson(message, Resign.class);
                //GameService.resignGame(commandObj.getAuthString(), commandObj.getGameID(), webSocketSessions);
                break;
            default:
                System.out.println("error in WebSocketHandler");
        }
    }

    @OnClose
    public void onClose(Integer gameID, String authToken) {
        System.out.println("Connection closed");
        //webSocketSessions.removeSessionFromGame(gameID, authToken);
    }
}