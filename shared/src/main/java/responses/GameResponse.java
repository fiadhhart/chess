package responses;

public class GameResponse extends BaseResponse{
    private Integer gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;


    public GameResponse(){}

    public GameResponse(Integer gameID, String whiteUsername, String blackUsername, String gameName) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
    }

    public GameResponse(String message){
        super(message);
    }


    public Integer getGameID() {
        return gameID;
    }
    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
    public String getWhiteUsername() {
        return whiteUsername;
    }
    public String getBlackUsername() {
        return blackUsername;
    }
    public String getGameName() {
        return gameName;
    }
}