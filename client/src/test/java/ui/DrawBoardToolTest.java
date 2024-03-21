package ui;

import chess.ChessBoard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DrawBoardToolTest {
    @Test
    void whiteBoard() {
        ChessBoard board = new ChessBoard();
        board.resetBoard();

        DrawBoardTool drawBoardTool = new DrawBoardTool();
        drawBoardTool.drawWhiteBoard(board);
    }

    @Test
    void blackBoard() {
        ChessBoard board = new ChessBoard();
        board.resetBoard();

        DrawBoardTool drawBoardTool = new DrawBoardTool();
        drawBoardTool.drawBlackBoard(board);
    }

}