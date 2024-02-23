package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import dataAccess.UserMemDAO;
import requests.LoginRequest;
import responses.LoginResponse;

public class LoginService {
    public LoginResponse login(LoginRequest request) throws UnauthorizedException, DataAccessException {
        String username = request.getUsername();
        String password = request.getPassword();

        try {
            UserDAO userDAO = new UserMemDAO();


            // Check if the username and password are valid
            String[] userData = UserMemDAO.getUser(username, password);
            if (userData != null) {
                // If the user is authenticated, create an auth token
                String authToken = AuthDAO.createAuth(username);
                // Return a successful login response with the auth token
                return new LoginResponse(username, authToken);
            } else {
                // If the username and password are incorrect, return a failed login response
                //return new LoginResponse(401, "Error: unauthorized");
                throw new UnauthorizedException("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            // If an exception occurs during data access, return a failed login response
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }
}