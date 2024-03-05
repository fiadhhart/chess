package service;

import dataAccess.*;
import requests.BaseRequest;
import responses.GameResponse;
import responses.ListGamesResponse;
import service.exceptions.UnauthorizedException;

import java.util.List;

public class ListGamesService {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public ListGamesService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public ListGamesResponse listGames(BaseRequest request, String authToken) throws UnauthorizedException, DataAccessException {
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