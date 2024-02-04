package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];  //remember this takes in index 0-7 not 1-8

    public ChessBoard() {
        //this.resetBoard();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) { //remember this takes in index 1-8 not 0-7
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
        //throw new RuntimeException("Not implemented");
    }

    public void removePiece(ChessPosition position) {
        squares[position.getRow() - 1][position.getColumn() - 1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) { //remember this takes in index 1-8 not 0-7
        if (squares[position.getRow() - 1][position.getColumn() - 1] == null) {
            return null;
        }else{
            return squares[position.getRow() - 1][position.getColumn() - 1];
        }
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        for (int i = 0; i < 8; ++i){
            for (int j = 0; j < 8; ++j){
                squares[i][j] = null;
            }
        }

        ChessPiece.PieceType[] initialPieceTypesOrder = {
            ChessPiece.PieceType.ROOK,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.QUEEN,
                ChessPiece.PieceType.KING,
                ChessPiece.PieceType.BISHOP,
                ChessPiece.PieceType.KNIGHT,
                ChessPiece.PieceType.ROOK
        };

        for (int i = 0; i < 8; ++i) {
            squares[0][i] = new ChessPiece(ChessGame.TeamColor.WHITE, initialPieceTypesOrder[i]);
            squares[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        for (int i = 0; i < 8; ++i){
            squares[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            squares[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, initialPieceTypesOrder[i]);
        }

        //throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    public ChessBoard cloneBoard() {
        ChessBoard clonedBoard = new ChessBoard();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece originalPiece = this.getPiece(new ChessPosition(i + 1, j + 1));

                if (originalPiece != null) {
                    ChessPiece clonedPiece = new ChessPiece(originalPiece.getTeamColor(), originalPiece.getPieceType());
                    clonedBoard.addPiece(new ChessPosition(i + 1, j + 1), clonedPiece);
                }
            }
        }

        return clonedBoard;
    }
}
