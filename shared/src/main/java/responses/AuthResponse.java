package responses;

public class AuthResponse extends BaseResponse{
    private String username;
    private String authToken;

    // Default constructor (required by Gson for serialization)
    public AuthResponse() {}

    // Parameterized constructor for successful login
    public AuthResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    // Parameterized constructor for failed login with error message
    public AuthResponse(String message){
        super(message);
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

}

