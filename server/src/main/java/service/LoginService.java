package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import requests.LoginRequest;
import responses.LoginResponse;

public class LoginService {
    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        try {
            // Check if the username and password are valid
            String[] userData = UserDAO.getUser(username, password);
            if (userData != null) {
                // If the user is authenticated, create an auth token
                String authToken = AuthDAO.createAuth(username);
                // Return a successful login response with the auth token
                return new LoginResponse(200, username, authToken);
            } else {
                // If the username and password are incorrect, return a failed login response
                return new LoginResponse(401, "Error: unauthorized");
            }
        } catch (DataAccessException e) {
            // If an exception occurs during data access, return a failed login response
            return new LoginResponse(500, "Error: " + e.getMessage());
        }
    }
}