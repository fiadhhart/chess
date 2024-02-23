package responses;

public class LoginResponse {
    private String username;
    private String authToken;
    private int statusCode;
    private String message;


    // Default constructor (required by Gson for serialization)
    public LoginResponse() {}

    // Parameterized constructor for successful login
    public LoginResponse(int statusCode, String username, String authToken) {
        this.statusCode = statusCode;
        this.username = username;
        this.authToken = authToken;
    }

    // Parameterized constructor for failed login with error message
    public LoginResponse(int statusCode, String errorMessage) {
        this.statusCode = statusCode;
        this.message = errorMessage;
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

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}

