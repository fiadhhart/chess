package responses;

public class BaseResponse {

    protected String message;

    // Default constructor (required by Gson for serialization)
    public BaseResponse() {}

    // Parameterized constructor for responses with error message
    public BaseResponse(String message) {
        this.message = message;
    }

    // Getter and setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}