package handler;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import responses.BaseResponse;
import requests.BaseRequest;
import spark.Request;
import spark.Response;
import spark.Route;

public abstract class BaseHandler<T extends BaseRequest, R extends BaseResponse> implements Route {
    protected Gson gson = new Gson();
    protected UserDAO userDAO;
    protected GameDAO gameDAO;
    protected AuthDAO authDAO;

    public BaseHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request req, Response res) {
        // Deserialize JSON request body to Request object
        T request = parseRequest(req);

        // Call Service to perform operation
        R response = null;
        try {
            String authToken = req.headers("Authorization");
            Tuple<R, Integer> operationResult = performOperation(request, authToken);
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
    protected abstract Tuple<R,Integer> performOperation(T request, String authToken) throws DataAccessException;
}