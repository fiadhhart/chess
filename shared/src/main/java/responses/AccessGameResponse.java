package responses;

import chess.ChessGame;

public class AccessGameResponse extends BaseResponse{
    private ChessGame game;


    public AccessGameResponse(){}

    public AccessGameResponse(ChessGame game) {
        this.game = game;
    }

    public AccessGameResponse(String message){
        super(message);
    }


    public ChessGame getGame() {
        return game;
    }
    public void setGame(ChessGame game) {
        this.game = game;
    }
}
