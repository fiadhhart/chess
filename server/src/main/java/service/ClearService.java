package service;

import dataAccess.*;
import requests.BaseRequest;
import responses.BaseResponse;

public class ClearService {
    public BaseResponse clear(BaseRequest request) throws DataAccessException {
        UserDAO userDAO = new UserMemDAO();
        AuthDAO authDAO = new AuthMemDAO();
        GameDAO gameDAO = new GameMemDAO();

        try {
            userDAO.clear();
            gameDAO.clear();
            authDAO.clear();
            return new BaseResponse();

        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }

    }
}
