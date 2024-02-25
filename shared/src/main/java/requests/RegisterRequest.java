package requests;

public class RegisterRequest extends LoginRequest {
    private String email;


    public RegisterRequest(){}

    public RegisterRequest(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}