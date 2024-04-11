package ui;

import chess.*;
import com.google.gson.Gson;
import webSocket.WebSocketClient;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.JoinPlayerCommand;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class GameplayUI implements Notify{
    private ChessGame.TeamColor userColor;  //null if observer
    private Integer gameID;
    private ChessGame game;
    private DrawBoardTool drawBoardTool = new DrawBoardTool();
    private WebSocketClient webSocketClient;

    public void run(String authToken, Integer gameID, ChessGame.TeamColor playerColor) throws InvalidMoveException, IOException, DeploymentException, URISyntaxException {
        this.webSocketClient = new WebSocketClient(authToken, gameID, playerColor, this);
        this.userColor = playerColor;
        this.gameID = gameID;


        System.out.println("Now playing/observing the game\n");
        //FIXME: notify joined
        //FIXME: notify observed

        /*
        //FIXME: game from database
        ChessGame game = new ChessGame();
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        game.setBoard(board);
        game.setTeamTurn(ChessGame.TeamColor.WHITE);
        this.game = game;
        //


        redraw(null);
        */

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
                    redraw(null);
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

    private void redraw(List<ChessPosition> highlights){
        if (this.userColor == ChessGame.TeamColor.BLACK){
            this.drawBoardTool.drawBlackBoard(this.game.getBoard(), highlights);
        }else{  //white or null(observer)
            this.drawBoardTool.drawWhiteBoard(this.game.getBoard(), highlights);
        }
    }
    private void leave(){
        System.out.println("Leaving game");
        ///FIXME: websocket notify left
    }

    private void move(Scanner scanner) {
        System.out.println("Enter the position of the piece you would like to move (i.e. d4)");
        String startInput = scanner.next();
        System.out.println("Enter the position you would like to move this piece to (i.e. d4)");
        String endInput = scanner.next();

        ChessPosition startPosition = null;
        ChessPosition endPosition = null;
        try{
            startPosition = parsePosition(startInput);
            endPosition = parsePosition(endInput);
        }catch(Exception e){
            System.out.println("invalid position");
            return;
        }

        ChessPiece.PieceType promotionChoice = null;
        ChessMove move = null;
        try{
            if (this.game.getBoard().getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN){
                if ((this.game.getTeamTurn() == ChessGame.TeamColor.WHITE
                        && startPosition.getRow() == 7)
                 || (this.game.getTeamTurn() == ChessGame.TeamColor.BLACK
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

        }catch(InvalidMoveException e){
            System.out.println("invalid move");
            return;
        }catch(Exception e){
            System.out.println("error creating move");
            return;
        }

        ///FIXME: websocket notify moved and update others

        boolean isInCheck = this.game.isInCheck(this.game.getTeamTurn());
        boolean isInCheckmate = this.game.isInCheckmate(this.game.getTeamTurn());
        boolean isInStalemate = this.game.isInStalemate(this.game.getTeamTurn());
        //FIXME: websocket notify in check
        //FIXME: websocket notify in checkmate

    }
    private ChessPosition parsePosition(String position) {
        int column = position.charAt(0) - 'a' + 1; // Convert letter to column number (1-indexed)
        int row = Character.getNumericValue(position.charAt(1)); // Get numeric value of row
        return new ChessPosition(row, column);
    }


    private void resign(Scanner scanner){
        System.out.println("Confirm you want to resign [Yes|No]");
        String confirmation = scanner.next();

        if (Objects.equals(confirmation, "Yes")){
            System.out.println("Resigned. Game over.");
            ////FIXME: websocket notify resigned


        } else if (Objects.equals(confirmation, "No")) {
            System.out.println("Did not resign");
        } else{
            System.out.println("invalid input");
        }
    }

    private void highlight(Scanner scanner){
        System.out.println("Enter the position of the piece to see its options (i.e. d4)");
        String positionInput = scanner.next();

        ChessPosition position = null;
        List<ChessPosition> endPositions = new ArrayList<>();
        try {
            position = parsePosition(positionInput);
            Collection<ChessMove> validMoves = this.game.validMoves(position);
            endPositions.add(position);
            for (ChessMove move : validMoves) {
                endPositions.add(move.getEndPosition());
            }
        }catch(Exception e){
            System.out.println("invalid position");
            return;
        }

        redraw(endPositions);
    }


    @Override
    public void notify(ServerMessage notification, String msg) {
        System.out.println("got to notify");

        switch(notification.getServerMessageType()){
            case NOTIFICATION:
                NotificationMessage notificationMessage = new Gson().fromJson(msg, NotificationMessage.class);
                String message = notificationMessage.message;
                System.out.println("Notification: " + message);
                break;

            case ERROR:
                ErrorMessage errorMessage = new Gson().fromJson(msg, ErrorMessage.class);
                String error = errorMessage.errorMessage;
                System.out.println("Error: " + error);
                break;

            case LOAD_GAME:
                LoadGameMessage loadGameMessage = new Gson().fromJson(msg, LoadGameMessage.class);
                ChessGame game = loadGameMessage.game;
                this.game = game;
                redraw(null);
                break;
        }

    }
}