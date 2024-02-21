package model;
import chess.ChessGame;

public class GameData {

    private Integer gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame game;

    public GameData(Integer gameID, String gameName){
        this.gameID = gameID;
        this.whiteUsername = null;
        this.blackUsername = null;
        this.gameName = gameName;
        this.game = new ChessGame();
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
    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }
    public String getBlackUsername() {
        return blackUsername;
    }
    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }
    public String getGameName() {
        return gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
    public ChessGame getGame() {
        return game;
    }
    public void setGame(ChessGame game) {
        this.game = game;
    }
}
