package ui;

import chess.ChessBoard;
import chess.ChessPosition;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DrawBoardToolTest {
    @Test
    void whiteBoard() {
        ChessBoard board = new ChessBoard();
        board.resetBoard();

        DrawBoardTool drawBoardTool = new DrawBoardTool();
        drawBoardTool.drawWhiteBoard(board, null);
    }

    @Test
    void blackBoard() {
        ChessBoard board = new ChessBoard();
        board.resetBoard();

        DrawBoardTool drawBoardTool = new DrawBoardTool();
        drawBoardTool.drawBlackBoard(board, null);
    }

    @Test
    void highlights(){
        List<ChessPosition> positions = Arrays.asList(
                new ChessPosition(1, 1),  // A1
                new ChessPosition(3, 4),  // D3
                new ChessPosition(5, 7),  // G5
                new ChessPosition(8, 2),  // B8
                new ChessPosition(6, 6)   // F6
        );

        ChessBoard board = new ChessBoard();
        board.resetBoard();

        DrawBoardTool drawBoardTool = new DrawBoardTool();
        drawBoardTool.drawBlackBoard(board, positions);

        drawBoardTool.drawBlackBoard(board, null);

    }

}