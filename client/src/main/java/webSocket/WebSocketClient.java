package webSocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import ui.Notify;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import javax.websocket.*;
import java.net.URI;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLOutput;

public class WebSocketClient extends Endpoint{
    private Session session;

    public WebSocketClient(String errorMessage, Notify notify) throws DeploymentException, URISyntaxException, IOException {
        establishConnection();

        UserGameCommand userGameCommand = new UserGameCommand(errorMessage);

        sendAndReceiveMessage(userGameCommand, notify);
    }
    public WebSocketClient(String authToken,
                           UserGameCommand.CommandType commandType,
                           Integer gameID,
                           ChessGame.TeamColor playerColor,
                           ChessMove move,
                           Notify notify) throws URISyntaxException, DeploymentException, IOException{

        establishConnection();

        UserGameCommand userGameCommand = null;
        switch (commandType) {
            case JOIN_PLAYER:
                userGameCommand = new JoinPlayerCommand(authToken, gameID, playerColor);
                break;
            case JOIN_OBSERVER:
                userGameCommand = new JoinObserverCommand(authToken, gameID);
                break;
            case MAKE_MOVE:
                userGameCommand = new MakeMoveCommand(authToken, gameID, move);
                break;
            case LEAVE:
                userGameCommand = new LeaveCommand(authToken, gameID);
                break;
            case RESIGN:
                userGameCommand = new ResignCommand(authToken, gameID);
                break;
        }

        sendAndReceiveMessage(userGameCommand, notify);
    }

    // Establish WebSocket connection when the WebSocketClient is instantiated
    private void establishConnection() throws URISyntaxException, DeploymentException, IOException {
        // Establish WebSocket connection when the WebSocketClient is instantiated
        URI uri = new URI("ws://localhost:3030/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
    }

    private void sendAndReceiveMessage(UserGameCommand userGameCommand, Notify notify) throws IOException {
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                notify.notify(serverMessage, message);
            }
        });

        //send message
        try {
            this.session.getBasicRemote().sendText(userGameCommand.toJson());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Method to close the WebSocket connection
    public void close() throws IOException {
        if (session != null) {
            session.close();
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}
}