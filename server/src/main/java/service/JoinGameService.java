package service;

import chess.ChessGame;
import dataAccess.*;
import requests.JoinGameRequest;
import responses.BaseResponse;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;

import java.util.Objects;

public class JoinGameService {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public JoinGameService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public BaseResponse joinGame(JoinGameRequest request, String authToken) throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException {
        ChessGame.TeamColor playerColor = request.getPlayerColor();
        Integer gameID = request.getGameID();
        String username = authDAO.getUsername(authToken);

        if (gameID == null) {
            throw new BadRequestException("Error: bad request");
        }

        try {
            String verifiedAuthToken = authDAO.getAuth(authToken);
            if (verifiedAuthToken != null) {
                if(Objects.equals(gameDAO.getGame(gameID), gameID)){

                    if(playerColor == null){
                        gameDAO.updateGameUsername(playerColor, gameID, username);
                        return new BaseResponse();
                    }
                    String userThere = gameDAO.getPlayer(playerColor, gameID);
                    if(userThere == null || userThere.equals(username)){
                        gameDAO.updateGameUsername(playerColor, gameID, username);
                        return new BaseResponse();
                    } else{
                        throw new AlreadyTakenException("Error: already taken");
                    }

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