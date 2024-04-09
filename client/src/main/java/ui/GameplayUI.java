package ui;

import chess.ChessBoard;
import chess.ChessGame;
import facade.ServerFacade;

import java.util.Objects;
import java.util.Scanner;

public class GameplayUI {
    private ServerFacade serverFacade;
    private ChessGame.TeamColor side;  //null if observer
    private ChessGame game;

    public void run(ServerFacade serverFacade, ChessGame.TeamColor playerColor, ChessGame game) {
        this.serverFacade = serverFacade;
        this.side = playerColor;
        this.game = game;


        //
        System.out.println("In gameplayUI");

        //ChessBoard board = new ChessBoard();
        //board.resetBoard();

        DrawBoardTool drawBoardTool = new DrawBoardTool();
        drawBoardTool.drawWhiteBoard(game.getBoard());
        drawBoardTool.drawBlackBoard(game.getBoard());
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


    }
    private void leave(){

    }
    private void move(Scanner scanner){

    }
    private void highlight(Scanner scanner){

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