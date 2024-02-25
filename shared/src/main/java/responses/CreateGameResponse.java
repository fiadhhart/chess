package responses;

public class CreateGameResponse extends BaseResponse{
    private Integer gameID;


    public CreateGameResponse(){}

    public CreateGameResponse(Integer gameID) {
        this.gameID = gameID;
    }

    public CreateGameResponse(String message){
        super(message);
    }


    public int getGameID() {
        return gameID;
    }
    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}