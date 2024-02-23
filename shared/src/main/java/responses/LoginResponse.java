package responses;

public class LoginResponse {
    private String username;
    private String authToken;
    private String message;


    // Default constructor (required by Gson for serialization)
    public LoginResponse() {}



    // Parameterized constructor for successful login
    public LoginResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    // Parameterized constructor for failed login with error message
    public LoginResponse(String message){
        this.message = message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

