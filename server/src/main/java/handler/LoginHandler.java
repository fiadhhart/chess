package handler;

import com.google.gson.Gson;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Route;

import requests.LoginRequest;
import responses.LoginResponse;
import service.LoginService;


public class LoginHandler implements Route {
    private Gson gson = new Gson();
    private LoginService loginService = new LoginService();

    @Override
    public Object handle(Request req, Response res) {
        // Deserialize JSON request body to LoginRequest object
        LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);

        // Call LoginService to perform login operation
        LoginResponse response = null;
        try {
            response = loginService.login(request);
            res.status(200);
        } catch (UnauthorizedException e) {
            res.status(401);
            response = new LoginResponse(e.getMessage());
        } catch (Exception e){
            res.status(500);
            response = new LoginResponse(e.getMessage());
        }
        //res.status(response.getStatusCode());

        // Serialize LoginResponse object to JSON and return
        res.type("application/json");
        return gson.toJson(response);
    }
}






