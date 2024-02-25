package handler;

import dataAccess.DataAccessException;
import requests.BaseRequest;
import responses.AuthResponse;
import responses.BaseResponse;
import service.LogoutService;
import service.exceptions.UnauthorizedException;
import spark.Request;

public class LogoutHandler extends BaseHandler<BaseRequest, BaseResponse>{
    private LogoutService logoutService = new LogoutService();

    @Override
    protected BaseRequest parseRequest(Request req) {
        return gson.fromJson(req.body(), BaseRequest.class);
    }

    @Override
    protected BaseResponse createErrorResponse(Exception e) {
        return new BaseResponse(e.getMessage());
    }

    @Override
    protected Tuple<BaseResponse, Integer> performOperation(BaseRequest request, String authToken) throws DataAccessException {
        try{
            return new Tuple<>(logoutService.logout(request, authToken), 200);
        } catch (UnauthorizedException e) {
            return new Tuple<>(new AuthResponse(e.getMessage()), 401);
        }
    }
}