package service;

import dataAccess.*;
import requests.LoginRequest;
import responses.AuthResponse;
import service.exceptions.UnauthorizedException;

public class LoginService {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public LoginService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public AuthResponse login(LoginRequest request) throws UnauthorizedException, DataAccessException {
        String username = request.getUsername();
        String password = request.getPassword();

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