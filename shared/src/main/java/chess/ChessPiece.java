package chess;

import chess.piece_moves.KingMoves;
import chess.piece_moves.PawnMoves;
import chess.piece_moves.RookMoves;
import chess.piece_moves.BishopMoves;
import chess.piece_moves.QueenMoves;
import chess.piece_moves.KnightMoves;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        switch (this.type){
            case KING:
                KingMoves allKingMoves = new KingMoves(myPosition, board, this.pieceColor);
                return allKingMoves.getMoves();
            case QUEEN:
                QueenMoves allQueenMoves = new QueenMoves(myPosition, board, this.pieceColor);
                return allQueenMoves.getMoves();
            case BISHOP:
                BishopMoves allBishopMoves = new BishopMoves(myPosition, board, this.pieceColor);
                return allBishopMoves.getMoves();
            case KNIGHT:
                KnightMoves allKnightMoves = new KnightMoves(myPosition, board, this.pieceColor);
                return allKnightMoves.getMoves();
            case ROOK:
                RookMoves allRookMoves = new RookMoves(myPosition, board, this.pieceColor);
                return allRookMoves.getMoves();
            case PAWN:
                PawnMoves allPawnMoves = new PawnMoves(myPosition, board, this.pieceColor);
                return allPawnMoves.getMoves();
        }
        return null;
        //throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
