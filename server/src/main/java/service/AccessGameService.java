package service;

import chess.ChessGame;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import requests.AccessGameRequest;
import responses.AccessGameResponse;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;

public class AccessGameService {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public AccessGameService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public AccessGameResponse accessGame(AccessGameRequest request, String authToken) throws BadRequestException, UnauthorizedException, DataAccessException {
        Integer gameID = request.getGameID();

        if (gameID == null) {
            throw new BadRequestException("Error: bad request");
        }

        try {
            String verifiedAuthToken = authDAO.getAuth(authToken);
            if (verifiedAuthToken != null) {

                ChessGame game = gameDAO.getChessGame(gameID);
                if (game != null) {
                    return new AccessGameResponse(game);
                } else {
                    throw new BadRequestException("Error: game not found");
                }


            } else {
                throw new UnauthorizedException("Error: unauthorized");
            }

        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }
}
