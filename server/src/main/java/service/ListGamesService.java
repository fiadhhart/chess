package service;

import dataAccess.*;
import dataAccess.AuthMemDAO;
import dataAccess.GameMemDAO;
import requests.BaseRequest;
import responses.GameResponse;
import responses.ListGamesResponse;
import service.exceptions.UnauthorizedException;

import java.util.List;

public class ListGamesService {
    public ListGamesResponse listGames(BaseRequest request, String authToken) throws UnauthorizedException, DataAccessException {
        GameDAO gameDAO = new GameMemDAO();
        AuthDAO authDAO = new AuthMemDAO();

        try {
            String verifiedAuthToken = authDAO.getAuth(authToken);
            if (verifiedAuthToken != null) {

                List<GameResponse> gamesList = gameDAO.listGames();
                return new ListGamesResponse(gamesList);

            } else {
                throw new UnauthorizedException("Error: unauthorized");
            }

        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }
}