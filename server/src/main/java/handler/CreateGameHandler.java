package handler;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import requests.CreateGameRequest;
import responses.CreateGameResponse;
import service.exceptions.BadRequestException;
import service.CreateGameService;
import service.exceptions.UnauthorizedException;
import spark.Request;

public class CreateGameHandler extends BaseHandler<CreateGameRequest, CreateGameResponse>{
    private CreateGameService createGameService;

    public CreateGameHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        super(userDAO, gameDAO, authDAO);
        this.createGameService = new CreateGameService(userDAO, gameDAO, authDAO);
    }

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