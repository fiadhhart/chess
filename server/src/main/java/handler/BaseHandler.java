package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import responses.BaseResponse;
import requests.BaseRequest;
import spark.Request;
import spark.Response;
import spark.Route;

public abstract class BaseHandler<T extends BaseRequest, R extends BaseResponse> implements Route {
    protected Gson gson = new Gson();

    @Override
    public Object handle(Request req, Response res) {
        // Deserialize JSON request body to Request object
        T request = parseRequest(req);

        // Call Service to perform operation
        R response = null;
        try {
            Tuple<R, Integer> operationResult = performOperation(request);
            res.status(operationResult.getSecond());
            response = operationResult.getFirst();

        } catch (Exception e){
            res.status(500);
            response = createErrorResponse(e);
        }

        // Serialize Response object to JSON and return
        res.type("application/json");
        return gson.toJson(response);
    }

    protected abstract T parseRequest(Request req);
    protected abstract R createErrorResponse(Exception e);
    protected abstract Tuple<R,Integer> performOperation(T request) throws DataAccessException;
}

