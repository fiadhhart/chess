package requests;



public class LoginRequest extends BaseRequest {
    private String username;
    private String password;

    // Default constructor (required by Gson for deserialization)
    public LoginRequest() {}

    // Parameterized constructor
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
