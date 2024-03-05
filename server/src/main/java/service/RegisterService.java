package service;

import dataAccess.*;
import requests.RegisterRequest;
import responses.AuthResponse;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;

public class RegisterService {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public RegisterService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public AuthResponse register(RegisterRequest request) throws BadRequestException, AlreadyTakenException, DataAccessException{
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();

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