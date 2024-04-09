package handler;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import requests.AccessGameRequest;
import responses.AccessGameResponse;
import responses.BaseResponse;
import service.AccessGameService;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;
import spark.Request;

public class AccessGameHandler extends BaseHandler<AccessGameRequest, AccessGameResponse>{
    private AccessGameService accessGameService;

    public AccessGameHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        super(userDAO, gameDAO, authDAO);
        this.accessGameService = new AccessGameService(userDAO, gameDAO, authDAO);
    }

    @Override
    protected AccessGameRequest parseRequest(Request req) {
        return gson.fromJson(req.body(), AccessGameRequest.class);
    }

    @Override
    protected AccessGameResponse createErrorResponse(Exception e) {
        return new AccessGameResponse(e.getMessage());
    }

    @Override
    protected Tuple<AccessGameResponse, Integer> performOperation(AccessGameRequest request, String authToken) throws DataAccessException {
        try{
            return new Tuple<>(accessGameService.accessGame(request, authToken), 200);
        } catch (BadRequestException e) {
            return new Tuple<>(new AccessGameResponse(e.getMessage()), 400);
        } catch (UnauthorizedException e) {
            return new Tuple<>(new AccessGameResponse(e.getMessage()), 401);
        }
    }
}
