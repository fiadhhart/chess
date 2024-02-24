package responses;

public class GameResponse extends BaseResponse{

    private int gameID;

    public GameResponse(){}

    public GameResponse(int gameID) {
        this.gameID = gameID;
    }

    public GameResponse(String message){
        super(message);
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
