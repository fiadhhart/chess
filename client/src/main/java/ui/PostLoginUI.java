package ui;

import chess.ChessGame;
import facade.ServerFacade;
import requests.AccessGameRequest;
import requests.BaseRequest;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import responses.*;
import java.util.*;

public class PostLoginUI {
    private String authToken;
    private ServerFacade serverFacade;
    private Map<Integer, Integer> lastListedGamesIDs = new HashMap<>();

    public void run(ServerFacade serverFacade, String authToken) {
        this.serverFacade = serverFacade;
        this.authToken = authToken;

        System.out.println("Successfully logged in. Type help for more information.");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.next();
            switch (command) {
                case "create":
                    //Allows the user to input a name for the new game.
                    // Calls the server create API to create the game.
                    // This does not join the player to the created game;
                    // it only creates the new game in the server.
                    create(scanner);
                    break;
                case "list":
                    //Lists all the games that currently exist on the server.
                    // Calls the server list API to get all the game data,
                    // and displays the games in a numbered list, including
                    // the game name and players (not observers) in the game.
                    // The numbering for the list should be independent of the game IDs.
                    list();
                    break;
                case "join":
                    //Allows the user to specify which game they want to join and
                    //what color they want to play. They should be able to enter
                    // the number of the desired game. Your client will need to keep track
                    // of which number corresponds to which game from the last time it listed the games.
                    // Calls the server join API to join the user to the game.
                    join(scanner);
                    break;
                case "observe":
                    //Allows the user to specify which game they want to observe.
                    // They should be able to enter the number of the desired game.
                    // Your client will need to keep track of which number corresponds
                    // to which game from the last time it listed the games.
                    // Calls the server join API to verify that the specified game exists.
                    observe(scanner);
                    break;
                case "logout":
                    //Logs out the user. Calls the server logout API to logout the user.
                    // After logging out with the server, the client should transition to the Prelogin UI.
                    logout();
                    return;
                case "help":
                    //Displays text informing the user what actions they can take.
                    help();
                    break;
                default:
                    System.out.println("invalid input in PostLoginUI");
            }
        }
    }

    private void help(){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\tcreate <NAME>");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - a game\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\tlist");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - games\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\tjoin <ID> [WHITE|BLACK]");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - a game\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\tobserve <ID>");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - a game\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\tlogout");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - when you are done\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\thelp");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - with possible commands\n");

        System.out.print(EscapeSequences.RESET_TEXT_COLOR);

    }
    private void logout(){
        BaseRequest request = new BaseRequest();

        try {
            BaseResponse response = serverFacade.logoutUser(request, this.authToken);

            if (response.getMessage() == null) {
                System.out.println("logged out");
                return;
            } else {
                System.out.println(response.getMessage());
            }

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
    private void create(Scanner scanner){
        System.out.println("Game Name:");
        String gameName = scanner.next();
        CreateGameRequest request = new CreateGameRequest(gameName);

        try {
            CreateGameResponse response = serverFacade.createGame(request, authToken);

            if (response.getMessage() == null) {
                System.out.println("Created game: " + gameName);
            } else {
                System.out.println(response.getMessage());
            }

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
    private void list(){
        BaseRequest request = new BaseRequest();

        try {
            ListGamesResponse response = serverFacade.listGames(request, authToken);

            if (response.getMessage() == null) {
                var games = response.getGames();

                for (int i = 0; i < games.size(); ++i) {
                    lastListedGamesIDs.put(i + 1, games.get(i).getGameID());

                    System.out.println((i + 1) + ". Game Name: " + games.get(i).getGameName());
                    System.out.println("   White User: " + games.get(i).getWhiteUsername());
                    System.out.println("   Black User: " + games.get(i).getBlackUsername());
                }

            } else {
                System.out.println(response.getMessage());
            }

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

    }
    private void join(Scanner scanner){
        System.out.println("Game Number:");
        Integer gameNum = Integer.parseInt(scanner.next());
        Integer gameID = lastListedGamesIDs.get(gameNum);

        System.out.println("Player Color [WHITE|BLACK]:");
        String playerColorString = scanner.next();
        ChessGame.TeamColor playerColor;
        if (Objects.equals(playerColorString, "WHITE")){
            playerColor = ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(playerColorString, "BLACK")){
            playerColor = ChessGame.TeamColor.BLACK;
        } else {
            System.out.println("not a valid player color");
            return;
        }

        JoinGameRequest request = new JoinGameRequest(playerColor, gameID);

        try {
            /*AccessGameRequest accessGameRequest = new AccessGameRequest(gameID);
            AccessGameResponse accessGameResponse = serverFacade.getChessGame(accessGameRequest, authToken);
            ChessGame game = accessGameResponse.getGame();
             */

            BaseResponse response = serverFacade.joinGame(request, authToken);

            if (response.getMessage() == null) {
                new GameplayUI().run(this.authToken, gameID, playerColor);
            } else {
                System.out.println(response.getMessage());
            }

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
    private void observe(Scanner scanner){
        System.out.println("Game Number:");
        Integer gameNum = Integer.parseInt(scanner.next());
        Integer gameID = lastListedGamesIDs.get(gameNum);

        JoinGameRequest request = new JoinGameRequest(gameID);

        try {
            /*
            AccessGameRequest accessGameRequest = new AccessGameRequest(gameID);
            AccessGameResponse accessGameResponse = serverFacade.getChessGame(accessGameRequest, authToken);
            ChessGame game = accessGameResponse.getGame();
            */

            BaseResponse response = serverFacade.joinGame(request, authToken);

            if (response.getMessage() == null) {
                new GameplayUI().run(this.authToken, gameID, null);
            } else {
                System.out.println(response.getMessage());
            }

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}