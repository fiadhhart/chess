package service;

import dataAccess.*;
import requests.RegisterRequest;
import responses.AuthResponse;

public class RegisterService {
    public AuthResponse register(RegisterRequest request) throws BadRequestException, AlreadyTakenException, DataAccessException{
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
        UserDAO userDAO = new UserMemDAO();
        AuthDAO authDAO = new AuthMemDAO();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }

        try {
            String userData = userDAO.getUser(username);

            if (userData == null){
                userDAO.createUser(username,password,email);
                String authToken = authDAO.createAuth(username);
                return new AuthResponse(username, authToken);

            }else{
                throw new AlreadyTakenException("Error: already taken");
            }

        } catch (DataAccessException e) {
            throw new DataAccessException("Error: " + e.getMessage());
        }
    }
}
