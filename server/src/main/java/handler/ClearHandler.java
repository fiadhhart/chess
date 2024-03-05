package handler;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import responses.BaseResponse;
import service.ClearService;
import requests.BaseRequest;
import spark.Request;


public class ClearHandler extends BaseHandler<BaseRequest, BaseResponse> {
    private ClearService clearService;

    public ClearHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        super(userDAO, gameDAO, authDAO);
        this.clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    @Override
    protected BaseRequest parseRequest(Request req) {
        return gson.fromJson(req.body(), BaseRequest.class);
    }

    @Override
    protected BaseResponse createErrorResponse(Exception e) {
        return new BaseResponse(e.getMessage());
    }

    @Override
    protected Tuple<BaseResponse,Integer> performOperation(BaseRequest request, String authToken) throws DataAccessException {
        return new Tuple<>(clearService.clear(request), 200);
    }
}