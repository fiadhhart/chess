package requests;

public class AccessGameRequest extends BaseRequest{
    private Integer gameID;


    public AccessGameRequest(){}

    public AccessGameRequest(Integer gameID) {
        this.gameID = gameID;
    }


    public Integer getGameID() {
        return gameID;
    }
}
