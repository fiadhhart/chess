package responses;

public class CreateGameResponse extends BaseResponse{

    private int gameID;

    public CreateGameResponse(){}

    public CreateGameResponse(int gameID) {
        this.gameID = gameID;
    }

    public CreateGameResponse(String message){
        super(message);
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
