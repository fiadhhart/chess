package webSocket;

import chess.ChessGame;
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
    //private final WebSocketSessions webSocketSessions = new WebSocketSessions();
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

                Integer gameID = joinPlayerCommand.gameID;
                ChessGame game = gameDAO.getChessGame(gameID);
                ServerMessage loadGameMessage = new LoadGameMessage(game);
                session.getRemote().sendString(new Gson().toJson(loadGameMessage));

                String authToken = joinPlayerCommand.getAuthString();
                String username = authDAO.getUsername(authToken);
                ChessGame.TeamColor playerColor = joinPlayerCommand.playerColor;
                ServerMessage notificationMessage = new NotificationMessage( username + " joined the game as the " + playerColor.toString() + " player." );
                session.getRemote().sendString(new Gson().toJson(notificationMessage));


                //send broadcast of the notification except self

                break;
            case UserGameCommand.CommandType.JOIN_OBSERVER:
                JoinObserverCommand joinObserverCommand = new Gson().fromJson(message, JoinObserverCommand.class);
                //JoinObserver commandObj = new Gson().fromJson(message, JoinObserver.class);
                //GameService.joinObserver(commandObj.getAuthString(), commandObj.getGameID(), webSocketSessions);
                break;
            case UserGameCommand.CommandType.MAKE_MOVE:
                MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                //MakeMove commandObj = new Gson().fromJson(message, MakeMove.class);
                //GameService.makeMove(commandObj.getAuthString(), commandObj.getGameID(), commandObj.getMove(), webSocketSessions);
                break;
            case UserGameCommand.CommandType.LEAVE:
                LeaveCommand leaveCommand = new Gson().fromJson(message, LeaveCommand.class);
                //Leave commandObj = new Gson().fromJson(message, Leave.class);
                //GameService.leaveGame(commandObj.getAuthString(), commandObj.getGameID(), webSocketSessions);
                break;
            case UserGameCommand.CommandType.RESIGN:
                ResignCommand resignCommand = new Gson().fromJson(message, ResignCommand.class);
                //Resign commandObj = new Gson().fromJson(message, Resign.class);
                //GameService.resignGame(commandObj.getAuthString(), commandObj.getGameID(), webSocketSessions);
                break;
        }
    }

    @OnClose
    public void onClose(Integer gameID, String authToken) {
        System.out.println("Connection closed");
        //webSocketSessions.removeSessionFromGame(gameID, authToken);
    }
}