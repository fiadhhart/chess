package handler;

import dataAccess.DataAccessException;
import requests.CreateGameRequest;
import responses.GameResponse;
import service.BadRequestException;
import service.CreateGameService;
import service.UnauthorizedException;
import spark.Request;

public class CreateGameHandler extends BaseHandler<CreateGameRequest, GameResponse>{
    private CreateGameService createGameService = new CreateGameService();

    @Override
    protected CreateGameRequest parseRequest(Request req) {
        return gson.fromJson(req.body(), CreateGameRequest.class);
    }

    @Override
    protected GameResponse createErrorResponse(Exception e) {
        return new GameResponse(e.getMessage());
    }

    @Override
    protected Tuple<GameResponse, Integer> performOperation(CreateGameRequest request, String authToken) throws DataAccessException {
        try {
            return new Tuple<>(createGameService.createGame(request, authToken), 200);
        } catch (BadRequestException e) {
            return new Tuple<>(new GameResponse(e.getMessage()), 400);
        } catch (UnauthorizedException e) {
            return new Tuple<>(new GameResponse(e.getMessage()), 401);
        }
    }
}
