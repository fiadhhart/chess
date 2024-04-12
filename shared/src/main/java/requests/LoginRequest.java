package requests;

public class LoginRequest extends BaseRequest {
    protected String username;
    protected String password;


    public LoginRequest() {}

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
}