package handler;

import com.google.gson.Gson;
import responses.BaseResponse;
import service.ClearService;
import requests.BaseRequest;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearHandler implements Route {
    private Gson gson = new Gson();
    private ClearService clearService = new ClearService();

    @Override
    public Object handle(Request req, Response res) {
        // Deserialize JSON request body to BaseRequest object
        BaseRequest request = gson.fromJson(req.body(), BaseRequest.class);

        // Call ClearService to perform clear operation
        BaseResponse response = null;
        try {
            res.status(200);
            response = clearService.clear(request);
        } catch (Exception e){
            res.status(500);
            response = new BaseResponse(e.getMessage());
        }

        // Serialize BaseResponse object to JSON and return
        res.type("application/json");
        return gson.toJson(response);
    }
}
