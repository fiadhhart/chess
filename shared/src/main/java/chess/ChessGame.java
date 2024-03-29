package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor teamTurn;


    public ChessGame() {}


    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Makes a move in a chess game
     *      Receives a given move and executes it, provided it is a legal move.
     *      A move is illegal if:
     *         the chess piece cannot move there,
     *         the move leaves the team’s king in danger,
     *         it’s not the corresponding team's turn.
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        if(isValidMove(move)){
            ChessPiece piece = this.board.getPiece(move.getStartPosition());
            if(move.getPromotionPiece() != null){
                piece.setPieceType(move.getPromotionPiece());
            }

            this.board.addPiece(move.getEndPosition(), piece);
            this.board.removePiece(move.getStartPosition());

            if(this.teamTurn == TeamColor.WHITE){
                this.teamTurn = TeamColor.BLACK;
            }else{
                this.teamTurn = TeamColor.WHITE;
            }

        }else{
            throw new InvalidMoveException();
        }
    }

    private boolean isValidMove(ChessMove move) {

        ChessPiece testPiece = this.board.getPiece(move.getStartPosition());

        if(testPiece == null){  //no piece to move in the first place
            return false;
        }

        if(testPiece.getTeamColor() != this.teamTurn && this.teamTurn != null){ //it’s not the corresponding team's turn
            return false;
        }

        Collection<ChessMove> testPossibleMoves = testPiece.pieceMoves(this.board, move.getStartPosition());
        if(!testPossibleMoves.contains(move)){  //the chess piece cannot move there
            return false;
        }

        ChessBoard testBoard = this.board.cloneBoard();
        ChessPiece piece = testBoard.getPiece(move.getStartPosition());
        if(move.getPromotionPiece() != null){
            piece.setPieceType(move.getPromotionPiece());
        }
        testBoard.addPiece(move.getEndPosition(), piece);
        testBoard.removePiece(move.getStartPosition());
        ChessGame testGame = new ChessGame();
        testGame.setBoard(testBoard);
        testGame.setTeamTurn(this.teamTurn);
        if(testGame.isInCheck(testPiece.getTeamColor())){   //the move leaves the team’s king in danger
            return false;
        }

        return true;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *      Takes as input a position on the chessboard and returns all moves the piece there can legally make.
     *      If there is no piece at that location, this method returns null.
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        ChessPiece piece = this.board.getPiece(startPosition);
        if(piece == null){  //no piece there
            return null;
        }

        Collection<ChessMove> allMoves = piece.pieceMoves(this.board, startPosition);
        Collection<ChessMove> validMoves = new HashSet<>();
        for (ChessMove move : allMoves){
            if(isValidMove(move)){
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    private ChessPosition findKingPosition(TeamColor teamColor){
        for (int i = 1; i <= 8; ++i){
            for (int j = 1; j <= 8; ++j){
                ChessPosition testPosition = new ChessPosition(i,j);
                if (this.board.getPiece(testPosition) != null &&
                        this.board.getPiece(testPosition).getPieceType() == ChessPiece.PieceType.KING &&
                        this.board.getPiece(testPosition).getTeamColor() == teamColor){
                    return testPosition;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in check
     *      Returns true if the specified team’s King could be captured by an opposing piece.
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor);

        for (int i = 1; i <= 8; ++i){
            for (int j = 1; j <= 8; ++j){
                ChessPosition currPosition = new ChessPosition(i,j);
                ChessPiece currPiece = this.board.getPiece(currPosition);

                if (currPiece != null && currPiece.getTeamColor() != teamColor){
                    Collection<ChessMove> possibleMoves = currPiece.pieceMoves(this.board, currPosition);
                    for (ChessMove move : possibleMoves){
                        if (move.getEndPosition().equals(kingPosition)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *      Returns true if the given team has no way to protect their king from being captured.
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        for (int i = 1; i <= 8; ++i){
            for (int j = 1; j <= 8; ++j){
                ChessPosition currPosition = new ChessPosition(i,j);
                ChessPiece currPiece = this.board.getPiece(currPosition);

                if (currPiece != null && currPiece.getTeamColor() == teamColor){
                    Collection<ChessMove> possibleMoves = currPiece.pieceMoves(this.board, currPosition);
                    for (ChessMove move : possibleMoves){
                        try{
                            ChessGame testGame = new ChessGame();
                            ChessBoard testBoard = this.board.cloneBoard();
                            testGame.setBoard(testBoard);
                            testGame.setTeamTurn(teamColor);

                            testGame.makeMove(move);

                            if(!testGame.isInCheck(teamColor)){
                                return false;
                            }
                        } catch (InvalidMoveException e){/*continue the loop*/}
                    }
                }
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *      Returns true if the given team has no legal moves, and it is currently that team’s turn.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        if(this.teamTurn != teamColor){
            return false;
        }

        for (int i = 1; i <= 8; ++i){
            for (int j = 1; j <= 8; ++j){
                ChessPosition currPosition = new ChessPosition(i,j);
                ChessPiece currPiece = this.board.getPiece(currPosition);

                if (currPiece != null && currPiece.getTeamColor() == teamColor){
                    if(!validMoves(currPosition).isEmpty()){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}