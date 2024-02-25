package service;

import dataAccess.*;
import dataAccess.AuthMemDAO;
import requests.BaseRequest;
import responses.BaseResponse;
import service.exceptions.UnauthorizedException;

public class LogoutService {
    public BaseResponse logout(BaseRequest request, String authToken) throws UnauthorizedException, DataAccessException {
        AuthDAO authDAO = new AuthMemDAO();

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