package service;

import dataAccess.*;
import requests.LoginRequest;
import responses.AuthResponse;

public class LoginService {
    public AuthResponse login(LoginRequest request) throws UnauthorizedException, DataAccessException {
        String username = request.getUsername();
        String password = request.getPassword();
        UserDAO userDAO = new UserMemDAO();
        AuthDAO authDAO = new AuthMemDAO();

        try {
            String[] userData = userDAO.getUser(username, password);

            if (userData != null) {
                String authToken = authDAO.createAuth(username);
                return new AuthResponse(username, authToken);

            } else {
                throw new UnauthorizedException("Error: unauthorized");
            }

        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }
}