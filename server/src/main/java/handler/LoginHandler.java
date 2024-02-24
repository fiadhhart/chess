package handler;

import dataAccess.DataAccessException;
import requests.LoginRequest;
import responses.AuthResponse;
import service.LoginService;
import service.UnauthorizedException;
import spark.Request;


public class LoginHandler extends BaseHandler<LoginRequest, AuthResponse> {
    private LoginService loginService = new LoginService();

    @Override
    protected LoginRequest parseRequest(Request req) {
        return gson.fromJson(req.body(), LoginRequest.class);
    }

    @Override
    protected AuthResponse createErrorResponse(Exception e) {
        return new AuthResponse(e.getMessage());
    }

    @Override
    protected Tuple<AuthResponse,Integer> performOperation(LoginRequest request) throws DataAccessException {
        try{
            return new Tuple<>(loginService.login(request), 200);
        } catch (UnauthorizedException e) {
            return new Tuple<>(new AuthResponse(e.getMessage()), 401);
        }
    }
}
