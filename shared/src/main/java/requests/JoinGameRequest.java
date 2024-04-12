package requests;

import chess.ChessGame;

public class JoinGameRequest extends BaseRequest{
    private ChessGame.TeamColor playerColor;
    private Integer gameID;


    public JoinGameRequest(){}

    public JoinGameRequest(ChessGame.TeamColor playerColor, Integer gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public JoinGameRequest(Integer gameID) {
        this.gameID = gameID;
    }


    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
    public Integer getGameID() {
        return gameID;
    }
}