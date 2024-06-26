package responses;

public class AuthResponse extends BaseResponse{
    private String username;
    private String authToken;


    public AuthResponse() {}

    public AuthResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public AuthResponse(String message){
        super(message);
    }


    public String getUsername() {
        return username;
    }
    public String getAuthToken() {
        return authToken;
    }
}