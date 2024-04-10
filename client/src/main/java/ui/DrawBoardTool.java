package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.List;

public class DrawBoardTool {
    private ChessPiece[][] squares;

    public void drawWhiteBoard(ChessBoard board, List<ChessPosition> highlights){   //white player at bottom
        this.squares = board.getSquares();

        System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);

        printHeader("    a  b  c  d  e  f  g  h    ");

        int rowCounter = 7;
        for (int i = 0; i < 4; ++i) {
            printRow(rowCounter, true, true, highlights);
            printRow(rowCounter - 1, false, true, highlights);
            rowCounter -= 2;
        }

        printHeader("    a  b  c  d  e  f  g  h    ");
    }

    public void drawBlackBoard(ChessBoard board, List<ChessPosition> highlights){   //black player at bottom
        this.squares = board.getSquares();

        System.out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);

        printHeader("    h  g  f  e  d  c  b  a    ");

        int rowCounter = 0;
        for (int i = 0; i < 4; ++i) {
            printRow(rowCounter, false, false, highlights);
            printRow(rowCounter + 1, true, false, highlights);
            rowCounter += 2;
        }

        printHeader("    h  g  f  e  d  c  b  a    ");
    }


    private void printHeader(String label) {
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print(label);
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print("\n");
    }

    private void printRow(int rowNumber, boolean isWhiteFirst, boolean leftToRight, List<ChessPosition> highlights) {
        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print(" " + (rowNumber+1) + " ");

        int startCol, endCol, step;
        if (leftToRight) {
            startCol = 0;
            endCol = 8;
            step = 1;
        } else {
            startCol = 7;
            endCol = -1;
            step = -1;
        }

        for (int colNumber = startCol; colNumber != endCol; colNumber += step) {
            ChessPosition currentPosition = new ChessPosition(rowNumber + 1, colNumber + 1);
            boolean isHighlighted = highlights != null && highlights.contains(currentPosition);

            if ((colNumber % 2 == 0 && isWhiteFirst) || (colNumber % 2 != 0 && !isWhiteFirst)) {
                if (isHighlighted) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                }else{
                    System.out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                }
            } else {
                if (isHighlighted) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                }else{
                    System.out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                }
            }
            System.out.print(getTypeAndColor(rowNumber, colNumber));

            System.out.print(EscapeSequences.RESET_BG_COLOR);
        }

        System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        System.out.print(" " + (rowNumber+1) + " ");
        System.out.print(EscapeSequences.RESET_BG_COLOR);
        System.out.print("\n");
    }

    private String getTypeAndColor(int rowIndex, int colIndex){
        ChessPiece piece = squares[rowIndex][colIndex];

        ChessPiece.PieceType pieceType = null;
        ChessGame.TeamColor pieceColor = null;
        if (piece != null){
            pieceType = piece.getPieceType();
            pieceColor = piece.getTeamColor();
        }

        String escapeSequence;
        switch (pieceType) {
            case null:
                escapeSequence = EscapeSequences.EMPTY;
                break;
            case KING:
                if (pieceColor == ChessGame.TeamColor.WHITE){
                    escapeSequence = EscapeSequences.WHITE_KING;
                }else{
                    escapeSequence = EscapeSequences.BLACK_KING;
                }
                break;
            case QUEEN:
                if (pieceColor == ChessGame.TeamColor.WHITE){
                    escapeSequence = EscapeSequences.WHITE_QUEEN;
                }else{
                    escapeSequence = EscapeSequences.BLACK_QUEEN;
                }
                break;
            case BISHOP:
                if (pieceColor == ChessGame.TeamColor.WHITE){
                    escapeSequence = EscapeSequences.WHITE_BISHOP;
                }else{
                    escapeSequence = EscapeSequences.BLACK_BISHOP;
                }
                break;
            case KNIGHT:
                if (pieceColor == ChessGame.TeamColor.WHITE){
                    escapeSequence = EscapeSequences.WHITE_KNIGHT;
                }else{
                    escapeSequence = EscapeSequences.BLACK_KNIGHT;
                }
                break;
            case ROOK:
                if (pieceColor == ChessGame.TeamColor.WHITE){
                    escapeSequence = EscapeSequences.WHITE_ROOK;
                }else{
                    escapeSequence = EscapeSequences.BLACK_ROOK;
                }
                break;
            case PAWN:
                if (pieceColor == ChessGame.TeamColor.WHITE){
                    escapeSequence = EscapeSequences.WHITE_PAWN;
                }else{
                    escapeSequence = EscapeSequences.BLACK_PAWN;
                }
                break;
        }
        return escapeSequence;
    }

}
