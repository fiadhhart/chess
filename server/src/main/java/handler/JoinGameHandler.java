package handler;

import dataAccess.DataAccessException;
import requests.JoinGameRequest;
import responses.AuthResponse;
import responses.BaseResponse;
import service.AlreadyTakenException;
import service.BadRequestException;
import service.JoinGameService;
import service.UnauthorizedException;
import spark.Request;

public class JoinGameHandler extends BaseHandler<JoinGameRequest, BaseResponse>{

    private JoinGameService joinGameService = new JoinGameService();

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
