package handler;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import requests.LoginRequest;
import responses.AuthResponse;
import service.LoginService;
import service.exceptions.UnauthorizedException;
import spark.Request;


public class LoginHandler extends BaseHandler<LoginRequest, AuthResponse> {
    private LoginService loginService;

    public LoginHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        super(userDAO, gameDAO, authDAO);
        this.loginService = new LoginService(userDAO, gameDAO, authDAO);
    }

    @Override
    protected LoginRequest parseRequest(Request req) {
        return gson.fromJson(req.body(), LoginRequest.class);
    }

    @Override
    protected AuthResponse createErrorResponse(Exception e) {
        return new AuthResponse(e.getMessage());
    }

    @Override
    protected Tuple<AuthResponse,Integer> performOperation(LoginRequest request, String authToken) throws DataAccessException {
        try{
            return new Tuple<>(loginService.login(request), 200);
        } catch (UnauthorizedException e) {
            return new Tuple<>(new AuthResponse(e.getMessage()), 401);
        }
    }
}