package service;

import chess.ChessGame;
import dataAccess.*;
import dataAccess.AuthMemDAO;
import dataAccess.GameMemDAO;
import requests.JoinGameRequest;
import responses.BaseResponse;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;

import java.util.Objects;

public class JoinGameService {
    public BaseResponse joinGame(JoinGameRequest request, String authToken) throws BadRequestException, UnauthorizedException, AlreadyTakenException, DataAccessException {
        ChessGame.TeamColor playerColor = request.getPlayerColor();
        Integer gameID = request.getGameID();
        AuthDAO authDAO = new AuthMemDAO();
        GameDAO gameDAO = new GameMemDAO();
        String username = authDAO.getUsername(authToken);

        if (gameID == null) {
            throw new BadRequestException("Error: bad request");
        }

        try {
            String verifiedAuthToken = authDAO.getAuth(authToken);
            if (verifiedAuthToken != null) {
                if(Objects.equals(gameDAO.getGame(gameID), gameID)){
                    if(!gameDAO.isPlayerOccupied(playerColor, gameID)){
                        gameDAO.updateGameUsername(playerColor, gameID, username);
                        return new BaseResponse();

                    }else{
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