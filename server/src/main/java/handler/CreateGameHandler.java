package handler;

import dataAccess.DataAccessException;
import requests.CreateGameRequest;
import responses.CreateGameResponse;
import service.BadRequestException;
import service.CreateGameService;
import service.UnauthorizedException;
import spark.Request;

public class CreateGameHandler extends BaseHandler<CreateGameRequest, CreateGameResponse>{
    private CreateGameService createGameService = new CreateGameService();

    @Override
    protected CreateGameRequest parseRequest(Request req) {
        return gson.fromJson(req.body(), CreateGameRequest.class);
    }

    @Override
    protected CreateGameResponse createErrorResponse(Exception e) {
        return new CreateGameResponse(e.getMessage());
    }

    @Override
    protected Tuple<CreateGameResponse, Integer> performOperation(CreateGameRequest request, String authToken) throws DataAccessException {
        try {
            return new Tuple<>(createGameService.createGame(request, authToken), 200);
        } catch (BadRequestException e) {
            return new Tuple<>(new CreateGameResponse(e.getMessage()), 400);
        } catch (UnauthorizedException e) {
            return new Tuple<>(new CreateGameResponse(e.getMessage()), 401);
        }
    }
}
