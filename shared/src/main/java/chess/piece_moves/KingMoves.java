package chess.piece_moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

/**
    calculates (returns via getMoves method) all possible moves for a King from a starting position.
 */
public class KingMoves {
    private Collection<ChessMove> possibleMoves;
    private ChessPosition startingPosition;
    private ChessBoard board;
    private ChessGame.TeamColor color;

    private int conditionRow;
    private int addRowStep;
    private int conditionCol;
    private int addColStep;

    public KingMoves(ChessPosition startingPosition, ChessBoard board, ChessGame.TeamColor color) {
        this.startingPosition = startingPosition;
        this.possibleMoves = new ArrayList<>();
        this.board = board;
        this.color = color;
    }

    public Collection<ChessMove> getMoves(){
        this.calculateAll();
        return possibleMoves;
    }

    private void calculateAll(){
        //up
        addRowMove(true);
        //down
        addRowMove(false);
        //right
        addColMove(true);
        //left
        addColMove(false);
        //up right
        addDiagonalMove(true, true);
        //down right
        addDiagonalMove(false, true);
        //down left
        addDiagonalMove(false, false);
        //up left
        addDiagonalMove(true, false);

        subtractBlocked();
    }

    private void addRowMove(boolean up){
        if (up){conditionRow = 8;addRowStep = 1;} //up
        else{conditionRow = 1;addRowStep = -1;} //down

        if (startingPosition.getRow() != conditionRow) {
            ChessPosition newPosition = new ChessPosition(startingPosition.getRow() + addRowStep, startingPosition.getColumn());
            ChessMove newMove = new ChessMove(startingPosition, newPosition);
            possibleMoves.add(newMove);
        }
    }
    private void addColMove(boolean right){
        if (right){conditionCol = 8;addColStep = 1;} //right
        else{conditionCol = 1;addColStep = -1;} //left

        if (startingPosition.getColumn() != conditionCol) {
            ChessPosition newPosition = new ChessPosition(startingPosition.getRow(), startingPosition.getColumn() + addColStep);
            ChessMove newMove = new ChessMove(startingPosition, newPosition);
            possibleMoves.add(newMove);
        }
    }
    private void addDiagonalMove(boolean up, boolean right){
        if (up){conditionRow = 8;addRowStep = 1;} //up
        else{conditionRow = 1;addRowStep = -1;} //down
        if (right){conditionCol = 8;addColStep = 1;} //right
        else{conditionCol = 1;addColStep = -1;} //left

        if (startingPosition.getRow() != conditionRow && startingPosition.getColumn() != conditionCol) {
            ChessPosition newPosition = new ChessPosition(startingPosition.getRow() + addRowStep, startingPosition.getColumn() + addColStep);
            ChessMove newMove = new ChessMove(startingPosition, newPosition);
            possibleMoves.add(newMove);
        }
    }

    private void subtractBlocked(){
        Collection<ChessMove> updatedMoves = new ArrayList<>();
        for (ChessMove move : possibleMoves){
            if (board.getPiece(move.getEndPosition()) == null
                    || board.getPiece(move.getEndPosition()).getTeamColor() != color){
                updatedMoves.add(move);
            }
        }
        possibleMoves = updatedMoves;
    }
}