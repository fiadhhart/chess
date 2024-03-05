package service;

import dataAccess.*;
import requests.BaseRequest;
import responses.BaseResponse;
import service.exceptions.UnauthorizedException;

public class LogoutService {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public LogoutService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public BaseResponse logout(BaseRequest request, String authToken) throws UnauthorizedException, DataAccessException {
        try {
            String verifiedAuthToken = authDAO.getAuth(authToken);

            if (verifiedAuthToken != null) {
                authDAO.deleteAuth(authToken);
                return new BaseResponse();

            } else {
                throw new UnauthorizedException("Error: unauthorized");
            }

        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }
}