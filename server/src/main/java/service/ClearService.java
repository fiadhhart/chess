package service;

import dataAccess.*;
import requests.BaseRequest;
import responses.BaseResponse;

public class ClearService {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public ClearService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public BaseResponse clear(BaseRequest request) throws DataAccessException {
        try {
            gameDAO.clear();
            authDAO.clear();
            userDAO.clear();
            return new BaseResponse();

        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }
}