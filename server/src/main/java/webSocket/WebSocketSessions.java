package webSocket;

import org.eclipse.jetty.websocket.api.Session;
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

    //FIXME: make broadcast method with for loops

    Map<String, Session> getSessionsForGame(Integer gameID) {
        return sessionMap.get(gameID);
    }
}