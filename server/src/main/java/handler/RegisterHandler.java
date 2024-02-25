package handler;

import dataAccess.DataAccessException;
import requests.RegisterRequest;
import responses.AuthResponse;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;
import spark.Request;
import service.RegisterService;

public class RegisterHandler extends BaseHandler<RegisterRequest, AuthResponse> {
    private RegisterService registerService = new RegisterService();

    @Override
    protected RegisterRequest parseRequest(Request req) {
        return gson.fromJson(req.body(), RegisterRequest.class);
    }

    @Override
    protected AuthResponse createErrorResponse(Exception e) {
        return new AuthResponse(e.getMessage());
    }

    @Override
    protected Tuple<AuthResponse,Integer> performOperation(RegisterRequest request, String authToken) throws DataAccessException {
        try {
            return new Tuple<>(registerService.register(request), 200);
        } catch (BadRequestException e) {
            return new Tuple<>(new AuthResponse(e.getMessage()), 400);
        } catch (AlreadyTakenException e) {
            return new Tuple<>(new AuthResponse(e.getMessage()), 403);
        }
    }
}