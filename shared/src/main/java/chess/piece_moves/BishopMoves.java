package chess.piece_moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 calculates (returns via getMoves method) all possible moves for a Bishop from a starting position.
 */
public class BishopMoves {
    private Collection<ChessMove> possibleMoves;
    private ChessPosition startingPosition;
    private ChessBoard board;
    private ChessGame.TeamColor color;

    private int conditionRow;
    private int addRowStep;
    private int conditionCol;
    private int addColStep;

    public BishopMoves(ChessPosition startingPosition, ChessBoard board, ChessGame.TeamColor color) {
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
        //up right
        addDiagonalMoves(true, true);
        //up left
        addDiagonalMoves(true, false);
        //down right
        addDiagonalMoves(false, true);
        //down left
        addDiagonalMoves(false, false);
    }

    private void addDiagonalMoves(boolean up, boolean right){
        if (up){conditionRow = 8;addRowStep = 1;} //up
        else{conditionRow = 1;addRowStep = -1;} //down
        if (right){conditionCol = 8;addColStep = 1;} //right
        else{conditionCol = 1;addColStep = -1;} //left

        ChessPosition newPosition = startingPosition;
        while (newPosition.getRow() != conditionRow && newPosition.getColumn() != conditionCol){
            newPosition = new ChessPosition(newPosition.getRow() + addRowStep, newPosition.getColumn() + addColStep);
            if (board.getPiece(newPosition) == null){
                ChessMove newMove = new ChessMove(startingPosition, newPosition);
                possibleMoves.add(newMove);
            }else{
                if(board.getPiece(newPosition).getTeamColor() != color){
                    ChessMove newMove = new ChessMove(startingPosition, newPosition);
                    possibleMoves.add(newMove);
                }
                break;
            }
        }
    }
}