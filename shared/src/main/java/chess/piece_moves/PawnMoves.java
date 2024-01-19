package chess.piece_moves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

/**
    calculates (returns via getMoves method) all possible moves for a Pawn from a starting position.
 */
public class PawnMoves {
    private Collection<ChessMove> possibleMoves;
    private ChessPosition startingPosition;
    private ChessBoard board;
    private ChessGame.TeamColor color;

    private int conditionRow;
    private int addRowStep;
    private int conditionCol;
    private int addColStep;

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
        if (color == ChessGame.TeamColor.WHITE){ //WHITE
            //up
            addRowMove(true, false);
            //two up
            ChessPosition oneUp = new ChessPosition(startingPosition.getRow() + 1, startingPosition.getColumn());
            if (startingPosition.getRow() == 2 && board.getPiece(oneUp) == null){
                addRowMove(true, true);
            }
            //up right
            addDiagonalMove(true, true);
            //up left
            addDiagonalMove(true, false);

        }else{ //BLACK
            //down
            addRowMove(false, false);
            //two down
            ChessPosition oneDown = new ChessPosition(startingPosition.getRow() - 1, startingPosition.getColumn());
            if (startingPosition.getRow() == 7 && board.getPiece(oneDown) == null){
                addRowMove(false, true);
            }
            //down right
            addDiagonalMove(false, true);
            //down left
            addDiagonalMove(false, false);
        }
    }

    private void addRowMove(boolean up, boolean two){
        if (up){conditionRow = 8;addRowStep = 1; //up
            if (two){addRowStep = 2;} //two up
        }else{conditionRow = 1;addRowStep = -1; //down
            if (two){addRowStep = -2;} //two down
        }

        ChessPosition newPosition = new ChessPosition(startingPosition.getRow() + addRowStep, startingPosition.getColumn());
        if (startingPosition.getRow() != conditionRow && board.getPiece(newPosition) == null) {
            addMove(newPosition);
        }
    }
    private void addDiagonalMove(boolean up, boolean right){
        if (up){conditionRow = 8;addRowStep = 1;} //up
        else{conditionRow = 1;addRowStep = -1;} //down
        if (right){conditionCol = 8;addColStep = 1;} //right
        else{conditionCol = 1;addColStep = -1;} //left

        ChessPosition newPosition = new ChessPosition(startingPosition.getRow() + addRowStep, startingPosition.getColumn() + addColStep);
        if (startingPosition.getRow() != conditionRow && startingPosition.getColumn() != conditionCol && board.getPiece(newPosition) != null) {
            if (board.getPiece(newPosition).getTeamColor() != color){ //can attack
                addMove(newPosition);
            }
        }
    }

    private void addMove(ChessPosition endPosition){
        ChessMove newMove = new ChessMove(startingPosition, endPosition);

        if (color == ChessGame.TeamColor.WHITE){ //WHITE
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
