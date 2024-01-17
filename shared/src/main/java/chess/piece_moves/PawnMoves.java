package chess.piece_moves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

/**
    calculates (returns via getMoves method) all possible moves for a Pawn from a starting position.
    DOES take into account other pieces
 */
public class PawnMoves {
    private Collection<ChessMove> possibleMoves;
    private ChessPosition startingPosition;
    private ChessBoard board;
    private ChessGame.TeamColor color;

    public PawnMoves(ChessPosition startingPosition, ChessBoard board, ChessGame.TeamColor color) {
        this.startingPosition = startingPosition;
        this.possibleMoves = new HashSet<>();
        this.board = board;
        this.color = color;
    }

    public Collection<ChessMove> getMoves(){
        this.calculateAll();
        return possibleMoves;
    }

    private void calculateAll(){

        if (color == ChessGame.TeamColor.WHITE){
            ChessPosition oneUp = new ChessPosition(startingPosition.getRow() + 1, startingPosition.getColumn());
            ChessPosition twoUp = new ChessPosition(startingPosition.getRow() + 2, startingPosition.getColumn());
            ChessPosition oneUpRight = new ChessPosition(startingPosition.getRow() + 1, startingPosition.getColumn() + 1);
            ChessPosition oneUpLeft = new ChessPosition(startingPosition.getRow() + 1, startingPosition.getColumn() - 1);

            if (startingPosition.getRow() != 8 && board.getPiece(oneUp) == null) { //can move one up
                addMove(oneUp);
            }
            if (startingPosition.getRow() == 2 && board.getPiece(oneUp) == null && board.getPiece(twoUp) == null) { //can move two up
                addMove(twoUp);
            }
            if (startingPosition.getRow() != 8 && startingPosition.getColumn() != 8 && board.getPiece(oneUpRight) != null) { //can move up & right
                if (board.getPiece(oneUpRight).getTeamColor() != color){ //can attack
                    addMove(oneUpRight);
                }
            }
            if (startingPosition.getRow() != 8 && startingPosition.getColumn() != 1 && board.getPiece(oneUpLeft) != null) { //can move up & left
                if (board.getPiece(oneUpLeft).getTeamColor() != color){ //can attack
                    addMove(oneUpLeft);
                }
            }

        }else{ //BLACK
            ChessPosition oneDown = new ChessPosition(startingPosition.getRow() - 1, startingPosition.getColumn());
            ChessPosition twoDown = new ChessPosition(startingPosition.getRow() - 2, startingPosition.getColumn());
            ChessPosition oneDownRight = new ChessPosition(startingPosition.getRow() - 1, startingPosition.getColumn() + 1);
            ChessPosition oneDownLeft = new ChessPosition(startingPosition.getRow() - 1, startingPosition.getColumn() - 1);

            if (startingPosition.getRow() != 1 && board.getPiece(oneDown) == null) { //can move one down
                addMove(oneDown);
            }
            if (startingPosition.getRow() == 7 && board.getPiece(oneDown) == null && board.getPiece(twoDown) == null) { //can move two down
                addMove(twoDown);
            }
            if (startingPosition.getRow() != 1 && startingPosition.getColumn() != 8 && board.getPiece(oneDownRight) != null) { //can move down & right
                if (board.getPiece(oneDownRight).getTeamColor() != color){ //can attack
                    addMove(oneDownRight);
                }
            }
            if (startingPosition.getRow() != 1 && startingPosition.getColumn() != 1 && board.getPiece(oneDownLeft) != null) { //can move down & left
                if (board.getPiece(oneDownLeft).getTeamColor() != color){ //can attack
                    addMove(oneDownLeft);
                }
            }
        }
    }

    private void addMove(ChessPosition endPosition){
        ChessMove newMove = new ChessMove(startingPosition, endPosition);

        if (color == ChessGame.TeamColor.WHITE){
            if (startingPosition.getRow() == 7)  {promotion(newMove);}
            else{possibleMoves.add(newMove);}

        }else{ //BLACK
            if (startingPosition.getRow() == 2)  {promotion(newMove);}
            else{possibleMoves.add(newMove);}
        }
    }

    private void promotion(ChessMove move){
        ChessPiece.PieceType[] promotionOptions = {
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.BISHOP
        };

        for (int i = 0; i < promotionOptions.length; ++i){
            ChessMove newMove = new ChessMove(startingPosition, move.getEndPosition(), promotionOptions[i]);
            possibleMoves.add(newMove);
        }
    }
}
