package service;

import dataAccess.*;
import requests.CreateGameRequest;
import responses.GameResponse;

public class CreateGameService {
    public GameResponse createGame(CreateGameRequest request, String authToken) throws BadRequestException, UnauthorizedException, DataAccessException {
        String gameName = request.getGameName();
        GameDAO gameDAO = new GameMemDAO();
        AuthDAO authDAO = new AuthMemDAO();


        if (gameName.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }

        try {
            String verifiedAuthToken = authDAO.getAuth(authToken);

            if (verifiedAuthToken != null) {
                if (gameDAO.getGame(gameName) == null){
                    int gameID = gameDAO.createGame(gameName);
                    return new GameResponse(gameID);

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
