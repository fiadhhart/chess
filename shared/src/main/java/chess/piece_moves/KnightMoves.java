package chess.piece_moves;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

/**
 calculates (returns via getMoves method) all possible moves for a Knight from a starting position.
 */
public class KnightMoves {
    private Collection<ChessMove> possibleMoves;
    private ChessPosition startingPosition;
    private ChessBoard board;
    private ChessGame.TeamColor color;

    private ArrayList conditionRow = new ArrayList<>();
    private int addRowStep;
    private ArrayList conditionCol = new ArrayList<>();
    private int addColStep;

    public KnightMoves(ChessPosition startingPosition, ChessBoard board, ChessGame.TeamColor color) {
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
        //up 1 right 2
        addMove(true, true, false);
        //up 2 right 1
        addMove(true, true, true);

        //up 1 left 2
        addMove(true, false, false);
        //up 2 left 1
        addMove(true, false, true);

        //down 1 right 2
        addMove(false, true, false);
        //down 2 right 1
        addMove(false, true, true);

        //down 1 left 2
        addMove(false, false, false);
        //down 2 left 1
        addMove(false, false, true);

        subtractBlocked();
    }

    private void addMove(boolean up, boolean right, boolean tall){
        conditionCol.clear();
        conditionRow.clear();
        if (tall){ //tall
            if (up){conditionRow.add(8); conditionRow.add(7);addRowStep = 2;} //up 2
            else{conditionRow.add(1); conditionRow.add(2);addRowStep = -2;} //down 2
            if (right){conditionCol.add(8);addColStep = 1;} //right 1
            else{conditionCol.add(1);addColStep = -1;} //left 1
        } else{ //long
            if (up){conditionRow.add(8);addRowStep = 1;} //up 1
            else{conditionRow.add(1);addRowStep = -1;} //down 1
            if (right){conditionCol.add(8); conditionCol.add(7);addColStep = 2;} //right 2
            else{conditionCol.add(1); conditionCol.add(2);addColStep = -2;} //left 2
        }

        if (!conditionRow.contains(startingPosition.getRow()) && !conditionCol.contains(startingPosition.getColumn())) {
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
