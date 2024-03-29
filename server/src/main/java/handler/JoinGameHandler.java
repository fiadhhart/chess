package handler;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import requests.JoinGameRequest;
import responses.BaseResponse;
import service.exceptions.AlreadyTakenException;
import service.exceptions.BadRequestException;
import service.JoinGameService;
import service.exceptions.UnauthorizedException;
import spark.Request;

public class JoinGameHandler extends BaseHandler<JoinGameRequest, BaseResponse>{
    private JoinGameService joinGameService;

    public JoinGameHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        super(userDAO, gameDAO, authDAO);
        this.joinGameService = new JoinGameService(userDAO, gameDAO, authDAO);
    }

    @Override
    protected JoinGameRequest parseRequest(Request req) {
        return gson.fromJson(req.body(), JoinGameRequest.class);
    }

    @Override
    protected BaseResponse createErrorResponse(Exception e) {
        return new BaseResponse(e.getMessage());
    }

    @Override
    protected Tuple<BaseResponse, Integer> performOperation(JoinGameRequest request, String authToken) throws DataAccessException {
        try{
            return new Tuple<>(joinGameService.joinGame(request, authToken), 200);
        } catch (BadRequestException e) {
            return new Tuple<>(new BaseResponse(e.getMessage()), 400);
        } catch (UnauthorizedException e) {
            return new Tuple<>(new BaseResponse(e.getMessage()), 401);
        } catch (AlreadyTakenException e) {
            return new Tuple<>(new BaseResponse(e.getMessage()), 403);
        }
    }
}