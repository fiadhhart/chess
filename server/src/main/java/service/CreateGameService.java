package service;

import dataAccess.*;
import requests.CreateGameRequest;
import responses.CreateGameResponse;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;

public class CreateGameService {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public CreateGameService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public CreateGameResponse createGame(CreateGameRequest request, String authToken) throws BadRequestException, UnauthorizedException, DataAccessException {
        String gameName = request.getGameName();

        if (gameName == null) {
            throw new BadRequestException("Error: bad request");
        }

        try {
            String verifiedAuthToken = authDAO.getAuth(authToken);

            if (verifiedAuthToken != null) {
                if (gameDAO.getGame(gameName) == null){
                    int gameID = gameDAO.createGame(gameName);
                    return new CreateGameResponse(gameID);

                }else{
                    throw new BadRequestException("Error: bad request");
                }
            } else {
                throw new UnauthorizedException("Error: unauthorized");
            }

        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }
}