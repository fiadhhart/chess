package handler;

import com.google.gson.Gson;
import service.UnauthorizedException;
import spark.Request;
import spark.Response;
import spark.Route;

import requests.LoginRequest;
import responses.AuthResponse;
import service.LoginService;


public class LoginHandler implements Route {
    private Gson gson = new Gson();
    private LoginService loginService = new LoginService();

    @Override
    public Object handle(Request req, Response res) {
        // Deserialize JSON request body to LoginRequest object
        LoginRequest request = gson.fromJson(req.body(), LoginRequest.class);

        // Call LoginService to perform login operation
        AuthResponse response = null;
        try {
            res.status(200);
            response = loginService.login(request);
        } catch (UnauthorizedException e) {
            res.status(401);
            response = new AuthResponse(e.getMessage());
        } catch (Exception e){
            res.status(500);
            response = new AuthResponse(e.getMessage());
        }

        // Serialize AuthResponse object to JSON and return
        res.type("application/json");
        return gson.toJson(response);
    }
}






