package handler;

import com.google.gson.Gson;
import requests.RegisterRequest;
import responses.AuthResponse;
import service.AlreadyTakenException;
import service.BadRequestException;
import spark.Request;
import spark.Response;
import spark.Route;
import service.RegisterService;

public class RegisterHandler implements Route {
    private Gson gson = new Gson();
    private RegisterService registerService = new RegisterService();

    @Override
    public Object handle(Request req, Response res) {
        // Deserialize JSON request body to RegisterRequest object
        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);

        // Call RegisterService to perform register operation
        AuthResponse response = null;
        try {
            res.status(200);
            response = registerService.register(request);
        } catch (BadRequestException e) {
            res.status(400);
            response = new AuthResponse(e.getMessage());
        } catch (AlreadyTakenException e) {
            res.status(403);
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

