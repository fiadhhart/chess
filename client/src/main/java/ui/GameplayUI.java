package ui;

import chess.*;
import facade.ServerFacade;

import java.util.*;

public class GameplayUI {
    private ServerFacade serverFacade;
    private ChessGame.TeamColor turn;  //null if observer
    private ChessGame game;
    private DrawBoardTool drawBoardTool = new DrawBoardTool();

    //public void run(ServerFacade serverFacade, ChessGame.TeamColor playerColor, ChessGame game) {
    public void run(ServerFacade serverFacade, ChessGame.TeamColor playerColor) throws InvalidMoveException {
        this.serverFacade = serverFacade;
        this.turn = playerColor;
        //this.game = game;

        //
        System.out.println("In gameplayUI");

        ChessBoard board = new ChessBoard();
        board.resetBoard();
        ChessGame game = new ChessGame();
        game.setBoard(board);
        game.setTeamTurn(ChessGame.TeamColor.WHITE);
        this.game = game;

        this.drawBoardTool.drawWhiteBoard(this.game.getBoard(), null);
        this.drawBoardTool.drawBlackBoard(this.game.getBoard(), null);
        //

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.next();
            switch (command) {
                case "help":
                    //Displays text informing the user what actions they can take.
                    help();
                    break;
                case "redraw":
                    //Redraws the chess board upon the user’s request.
                    redraw();
                    break;
                case "leave":
                    //Removes the user from the game (whether they are playing or observing the game).
                    //The client transitions back to the Post-Login UI.
                    leave();
                    return;
                case "move":
                    //Allow the user to input what move they want to make.
                    // The board is updated to reflect the result of the move,
                    // and the board automatically updates on all clients involved in the game.
                    move(scanner);
                    break;
                case "resign":
                    //	Prompts the user to confirm they want to resign.
                    // If they do, the user forfeits the game and the game is over.
                    // Does not cause the user to leave the game.
                    resign(scanner);
                    break;
                case "highlight":
                    //Allows the user to input what piece for which they want to highlight legal moves.
                    // The selected piece’s current square and all squares it can legally move to are highlighted.
                    // This is a local operation and has no effect on remote users’ screens.
                    highlight(scanner);
                    break;
                default:
                    System.out.println("invalid input in GameplayUI");
            }
        }
    }

    private void help() {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\thelp");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - with possible commands\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\tredraw");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - the chess board\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\tleave");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - the game\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\tmove");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - a piece\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\tresign");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - the game\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\thighlight");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - legal moves\n");

        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
    }

    private void redraw(){
        if (this.turn == ChessGame.TeamColor.BLACK){
            this.drawBoardTool.drawBlackBoard(this.game.getBoard(), null);
        }else{  //white or null(observer)
            this.drawBoardTool.drawWhiteBoard(this.game.getBoard(), null);
        }
    }
    private void leave(){
        ///FIXME
    }

    private void move(Scanner scanner) throws InvalidMoveException {
        System.out.println("Enter the position of the piece you would like to move (i.e. d4)");
        String startInput = scanner.next();
        System.out.println("Enter the position you would like to move this piece to (i.e. d4)");
        String endInput = scanner.next();

        ChessPosition startPosition = parsePosition(startInput);
        ChessPosition endPosition = parsePosition(endInput);

        ChessPiece.PieceType promotionChoice = null;
        ChessMove move = null;
        if (this.game.getBoard().getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN){
            if ((this.turn == ChessGame.TeamColor.WHITE
                    && startPosition.getRow() == 7)
             || (this.turn == ChessGame.TeamColor.BLACK
                    && startPosition.getRow() == 2) ){

                System.out.println("Enter the type you would like to promote to [KNIGHT|ROOK|QUEEN|BISHOP]:");
                String promoteInput = scanner.next();
                switch (promoteInput) {
                    case "KNIGHT" -> promotionChoice = ChessPiece.PieceType.KNIGHT;
                    case "ROOK" -> promotionChoice = ChessPiece.PieceType.ROOK;
                    case "QUEEN" -> promotionChoice = ChessPiece.PieceType.QUEEN;
                    case "BISHOP" -> promotionChoice = ChessPiece.PieceType.BISHOP;
                    case null, default -> {
                        System.out.println("not a valid promotion option");
                        return;
                    }
                }
            }

            move = new ChessMove(startPosition, endPosition, promotionChoice);
        }else{
            move = new ChessMove(startPosition, endPosition);
        }

        this.game.makeMove(move);

        boolean isInCheck = this.game.isInCheck(this.turn);
        boolean isInCheckmate = this.game.isInCheckmate(this.turn);
        boolean isInStalemate = this.game.isInStalemate(this.turn);
        ///FIXME

    }
    public ChessPosition parsePosition(String position) {
        int column = position.charAt(0) - 'a' + 1; // Convert letter to column number (1-indexed)
        int row = Character.getNumericValue(position.charAt(1)); // Get numeric value of row
        return new ChessPosition(row, column);
    }


    private void highlight(Scanner scanner){
        System.out.println("Enter the position of the piece (i.e. d4)");
        String positionInput = scanner.next();
        ChessPosition position = parsePosition(positionInput);

        Collection<ChessMove> validMoves = this.game.validMoves(position);
        List<ChessPosition> endPositions = new ArrayList<>();
        for (ChessMove move : validMoves) {
            endPositions.add(move.getEndPosition());
        }

        if (this.turn == ChessGame.TeamColor.BLACK){
            this.drawBoardTool.drawBlackBoard(this.game.getBoard(), endPositions);
        }else{  //white or null(observer)
            this.drawBoardTool.drawWhiteBoard(this.game.getBoard(), endPositions);
        }

    }

    private void resign(Scanner scanner){
        System.out.println("Confirm you want to resign [Yes|No]");
        String confirmation = scanner.next();

        if (Objects.equals(confirmation, "Yes")){
            System.out.println("Resigned");
            ////


        } else if (Objects.equals(confirmation, "No")) {
            System.out.println("Did not resign");
        } else{
            System.out.println("invalid input");
        }
    }

}