package handler;

import dataAccess.DataAccessException;
import responses.BaseResponse;
import service.ClearService;
import requests.BaseRequest;
import spark.Request;


public class ClearHandler extends BaseHandler<BaseRequest, BaseResponse> {
    private ClearService clearService = new ClearService();

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