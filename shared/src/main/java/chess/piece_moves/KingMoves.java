package chess.piece_moves;

import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoves {
    private Collection<ChessMove> possibleMoves;
    private ChessPosition startingPosition;

    public KingMoves(ChessPosition startingPosition) {
        this.startingPosition = startingPosition;
        this.possibleMoves = new ArrayList<>();
    }

    public Collection<ChessMove> getMoves(){
        this.calculateAll();
        return possibleMoves;
    }

    private void calculateAll(){
        if (startingPosition.getRow() != 8) { //can move up
            ChessPosition oneUp = new ChessPosition(startingPosition.getRow() + 1, startingPosition.getColumn());
            ChessMove moveUp = new ChessMove(startingPosition, oneUp);
            possibleMoves.add(moveUp);
        }
        if (startingPosition.getRow() != 1) { //can move down
            ChessPosition oneDown = new ChessPosition(startingPosition.getRow() - 1, startingPosition.getColumn());
            ChessMove moveDown = new ChessMove(startingPosition, oneDown);
            possibleMoves.add(moveDown);
        }
        if (startingPosition.getColumn() != 8) { //can move right
            ChessPosition oneRight = new ChessPosition(startingPosition.getRow(), startingPosition.getColumn() + 1);
            ChessMove moveRight = new ChessMove(startingPosition, oneRight);
            possibleMoves.add(moveRight);
        }
        if (startingPosition.getColumn() != 1) { //can move left
            ChessPosition oneLeft = new ChessPosition(startingPosition.getRow(), startingPosition.getColumn() - 1);
            ChessMove moveLeft = new ChessMove(startingPosition, oneLeft);
            possibleMoves.add(moveLeft);
        }

        if (startingPosition.getRow() != 8 && startingPosition.getColumn() != 8) { //can move up & right
            ChessPosition oneUpRight = new ChessPosition(startingPosition.getRow() + 1, startingPosition.getColumn() + 1);
            ChessMove moveUpRight = new ChessMove(startingPosition, oneUpRight);
            possibleMoves.add(moveUpRight);
        }
        if (startingPosition.getRow() != 1 && startingPosition.getColumn() != 8) { //can move down & right
            ChessPosition oneDownRight = new ChessPosition(startingPosition.getRow() - 1, startingPosition.getColumn() + 1);
            ChessMove moveDownRight = new ChessMove(startingPosition, oneDownRight);
            possibleMoves.add(moveDownRight);
        }
        if (startingPosition.getRow() != 1 && startingPosition.getColumn() != 1) { //can move down & left
            ChessPosition oneDownLeft = new ChessPosition(startingPosition.getRow() - 1, startingPosition.getColumn() - 1);
            ChessMove moveDownLeft = new ChessMove(startingPosition, oneDownLeft);
            possibleMoves.add(moveDownLeft);
        }
        if (startingPosition.getRow() != 8 && startingPosition.getColumn() != 1) { //can move up & left
            ChessPosition oneUpLeft = new ChessPosition(startingPosition.getRow() + 1, startingPosition.getColumn() - 1);
            ChessMove moveUpLeft = new ChessMove(startingPosition, oneUpLeft);
            possibleMoves.add(moveUpLeft);
        }
    }
}
