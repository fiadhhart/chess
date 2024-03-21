package ui;

import chess.ChessBoard;
import facade.ServerFacade;

public class GameplayUI {
    private ServerFacade serverFacade;
    public void run(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;

        System.out.println("In gameplayUI");

        ChessBoard board = new ChessBoard();
        board.resetBoard();

        DrawBoardTool drawBoardTool = new DrawBoardTool();
        drawBoardTool.drawWhiteBoard(board);
        drawBoardTool.drawBlackBoard(board);
    }
}
