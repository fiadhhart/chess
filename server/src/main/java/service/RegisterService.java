package service;

import dataAccess.*;
import dataAccess.AuthMemDAO;
import dataAccess.UserMemDAO;
import requests.RegisterRequest;
import responses.AuthResponse;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;

public class RegisterService {
    public AuthResponse register(RegisterRequest request) throws BadRequestException, AlreadyTakenException, DataAccessException{
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
        UserDAO userDAO = new UserMemDAO();
        AuthDAO authDAO = new AuthMemDAO();

        if (username == null || password == null || email == null) {
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