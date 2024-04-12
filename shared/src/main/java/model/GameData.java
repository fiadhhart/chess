package model;
import chess.ChessBoard;
import chess.ChessGame;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        ChessBoard board = new ChessBoard();
        board.resetBoard();
        this.game.setBoard(board);
        this.game.setTeamTurn(ChessGame.TeamColor.WHITE);
    }


    public Integer getGameID() {
        return gameID;
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
    public ChessGame getGame() {
        return game;
    }
    public void setGame(ChessGame game) {
        this.game = game;
    }
}