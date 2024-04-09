package facade;

import chess.ChessGame;
import com.google.gson.Gson;
import requests.*;
import responses.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {
    private final String baseUrl;

    public ServerFacade(String baseUrl) {
        this.baseUrl = baseUrl;
    }


    public AuthResponse registerUser(RegisterRequest request) throws IOException {
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();

        String endpoint = baseUrl + "/user";

        String requestBody = String.format("{\"username\": \"%s\", \"password\": \"%s\", \"email\": \"%s\"}", username, password, email);

        return sendRequest("POST", endpoint, requestBody, null, AuthResponse.class);
    }

    public AuthResponse loginUser(LoginRequest request) throws IOException {
        String username = request.getUsername();
        String password = request.getPassword();

        String endpoint = baseUrl + "/session";

        String requestBody = String.format("{\"username\": \"%s\", \"password\": \"%s\"}", username, password);

        return sendRequest("POST", endpoint, requestBody, null, AuthResponse.class);
    }

    public BaseResponse logoutUser(BaseRequest request, String authToken) throws IOException {

        String endpoint = baseUrl + "/session";

        return sendRequest("DELETE", endpoint, null, authToken, BaseResponse.class);
    }

    public CreateGameResponse createGame(CreateGameRequest request, String authToken) throws IOException {
        String gameName = request.getGameName();

        String endpoint = baseUrl + "/game";

        String requestBody = String.format("{\"gameName\": \"%s\"}", gameName);

        return sendRequest("POST", endpoint, requestBody, authToken, CreateGameResponse.class);
    }

    public BaseResponse joinGame(JoinGameRequest request, String authToken) throws IOException {
        ChessGame.TeamColor playerColor = request.getPlayerColor();
        Integer gameID = request.getGameID();

        String endpoint = baseUrl + "/game";

        String requestBody = String.format("{\"gameID\": %d, \"playerColor\": \"%s\"}", gameID, playerColor);

        return sendRequest("PUT", endpoint, requestBody, authToken, BaseResponse.class);
    }

    public ListGamesResponse listGames(BaseRequest request, String authToken) throws IOException {
        String endpoint = baseUrl + "/game";

        return sendRequest("GET", endpoint, null, authToken, ListGamesResponse.class);
    }

    public BaseResponse clearDatabase(BaseRequest request) throws IOException {
        String endpoint = baseUrl + "/db";

        return sendRequest("DELETE", endpoint, null, null, BaseResponse.class);
    }

    public AccessGameResponse getChessGame(AccessGameRequest request, String authToken) throws IOException {
        Integer gameID = request.getGameID();

        String endpoint = baseUrl + "/game/" + gameID; // Assuming the endpoint follows this pattern

        return sendRequest("GET", endpoint, null, authToken, AccessGameResponse.class);
    }


    private <T> T sendRequest(String method, String endpoint, String requestBody, String authToken, Class<T> responseClass) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setDoOutput(true);

        if (authToken != null) {
            connection.setRequestProperty("Authorization", authToken);
        }

        if (requestBody != null) {
            try (OutputStream os = connection.getOutputStream()) {
                os.write(requestBody.getBytes());
            }
        }

        int responseCode = connection.getResponseCode();
        InputStream responseBodyStream = null;

        if (responseCode == HttpURLConnection.HTTP_OK) {
            responseBodyStream = connection.getInputStream();
        } else {
            responseBodyStream = connection.getErrorStream();
        }

        InputStreamReader responseBodyReader = new InputStreamReader(responseBodyStream);
        T responseObject = new Gson().fromJson(responseBodyReader, responseClass);

        responseBodyReader.close();
        connection.disconnect();

        return responseObject;
    }

}