package chess.piece_moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 calculates (returns via getMoves method) all possible moves for a Rook from a starting position.
 */
public class RookMoves {
    private Collection<ChessMove> possibleMoves;
    private ChessPosition startingPosition;
    private ChessBoard board;
    private ChessGame.TeamColor color;

    private int conditionRow;
    private int addRowStep;
    private int conditionCol;
    private int addColStep;

    public RookMoves(ChessPosition startingPosition, ChessBoard board, ChessGame.TeamColor color) {
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
        addRowMoves(true);
        //down
        addRowMoves(false);
        //right
        addColMoves(true);
        //left
        addColMoves(false);
    }

    private void addRowMoves(boolean up){
        if (up){conditionRow = 8;addRowStep = 1;} //up
        else{conditionRow = 1;addRowStep = -1;} //down

        ChessPosition newPosition = startingPosition;
        while (newPosition.getRow() != conditionRow){
            newPosition = new ChessPosition(newPosition.getRow() + addRowStep, newPosition.getColumn());
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
    private void addColMoves(boolean right){
        if (right){conditionCol = 8;addColStep = 1;} //right
        else{conditionCol = 1;addColStep = -1;} //left

        ChessPosition newPosition = startingPosition;
        while (newPosition.getColumn() != conditionCol){
            newPosition = new ChessPosition(newPosition.getRow(), newPosition.getColumn() + addColStep);
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
