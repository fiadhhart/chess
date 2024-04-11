package webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class WebSocketSessions {
    private final Map<Integer, Map<String, Session>> sessionMap = new HashMap<>();  //Map<gameID, Map<authToken, Session>>

    public void addSessionToGame(Integer gameID, String authToken, Session session){

        Map<String, Session> gameSessions = sessionMap.computeIfAbsent(gameID, k -> new HashMap<>());

        gameSessions.put(authToken, session);
    }

    public void removeSessionFromGame(Integer gameID, String authToken) {

        Map<String, Session> gameSessions = sessionMap.get(gameID);

        if (gameSessions != null) {
            gameSessions.remove(authToken);
        }
    }

    public void broadcastSession(Integer gameID, String excludeAuthToken, ServerMessage message) throws IOException {

        Map<String, Session> sessionsForGame = getSessionsForGame(gameID);

        for (Map.Entry<String, Session> entry : sessionsForGame.entrySet()) {
            String authToken = entry.getKey();
            Session session = entry.getValue();

            if (excludeAuthToken == null || !authToken.equals(excludeAuthToken)) {
                session.getRemote().sendString(new Gson().toJson(message));
            }
        }
    }

    Map<String, Session> getSessionsForGame(Integer gameID) {
        return sessionMap.get(gameID);
    }
}