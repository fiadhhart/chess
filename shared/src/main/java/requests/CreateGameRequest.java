package requests;

public class CreateGameRequest extends BaseRequest{

    private String gameName;


    public CreateGameRequest(){}

    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }


    public String getGameName() {
        return gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
