package webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.Notify;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinObserverCommand;
import webSocketMessages.userCommands.JoinPlayerCommand;
import webSocketMessages.userCommands.UserGameCommand;
import javax.websocket.*;
import java.net.URI;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLOutput;

public class WebSocketClient extends Endpoint{
    private Session session;

    public WebSocketClient(String authToken, Integer gameID, ChessGame.TeamColor playerColor, Notify notify) throws URISyntaxException, DeploymentException, IOException {
        // Establish WebSocket connection when the WebSocketClient is instantiated
        URI uri = new URI("ws://localhost:3030/connect");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        //make JOIN_PLAYER or JOIN_OBSERVER command
        UserGameCommand userGameCommand;
        if (playerColor != null){
            userGameCommand = new JoinPlayerCommand(authToken, gameID, playerColor);
        }else{
            userGameCommand = new JoinObserverCommand(authToken, gameID);
        }

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                System.out.println("got back to websocketclient");
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