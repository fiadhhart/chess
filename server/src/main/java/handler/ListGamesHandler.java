package handler;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import requests.BaseRequest;
import responses.ListGamesResponse;
import service.ListGamesService;
import service.exceptions.UnauthorizedException;
import spark.Request;

public class ListGamesHandler extends BaseHandler<BaseRequest, ListGamesResponse>{
    private ListGamesService listGamesService;

    public ListGamesHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        super(userDAO, gameDAO, authDAO);
        this.listGamesService = new ListGamesService(userDAO, gameDAO, authDAO);
    }

    @Override
    protected BaseRequest parseRequest(Request req) {
        return gson.fromJson(req.body(), BaseRequest.class);
    }

    @Override
    protected ListGamesResponse createErrorResponse(Exception e) {
        return new ListGamesResponse(e.getMessage());
    }

    @Override
    protected Tuple<ListGamesResponse, Integer> performOperation(BaseRequest request, String authToken) throws DataAccessException {
        try {
            return new Tuple<>(listGamesService.listGames(request, authToken), 200);
        } catch (UnauthorizedException e) {
            return new Tuple<>(new ListGamesResponse(e.getMessage()), 401);
        }
    }
}