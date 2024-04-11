package ui;

import facade.ServerFacade;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.AuthResponse;
import java.util.Scanner;

public class PreLoginUI {
    private ServerFacade serverFacade;

    public void run(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;

        System.out.println(EscapeSequences.BLACK_KING + EscapeSequences.WHITE_KING
                + "Welcome to 240 chess. Type help to get started."
                + EscapeSequences.WHITE_KING + EscapeSequences.BLACK_KING);

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.next();
            switch (command) {
                case "register":
                    //Prompts the user to input registration information.
                    // Calls the server register API to register and login the user.
                    // If successfully registered, the client should be logged in and transition to the Postlogin UI.
                    register(scanner);
                    break;
                case "login":
                    //Prompts the user to input login information.
                    // Calls the server login API to login the user.
                    // When successfully logged in, the client should transition to the Postlogin UI.
                    login(scanner);
                    break;
                case "quit":
                    //Exits the program.
                    System.out.println("Have a good day!");
                    return;
                case "help":
                    //Displays text informing the user what actions they can take.
                    help();
                    break;
                default:
                    System.out.println("invalid input in PreLoginUI");
            }
        }
    }

    private void help(){
        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\tregister <USERNAME> <PASSWORD> <EMAIL>");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - to create an account\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\tlogin <USERNAME> <PASSWORD>");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - to play chess\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\tquit");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - playing chess\n");

        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
        System.out.print("\thelp");
        System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
        System.out.print(" - with possible commands\n");

        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
    }
    private void login(Scanner scanner){
        System.out.println("Username:");
        String username = scanner.next();
        System.out.println("Password:");
        String password = scanner.next();
        LoginRequest request = new LoginRequest(username,password);

        try {
            AuthResponse response = serverFacade.loginUser(request);
            String authToken = response.getAuthToken();

            if (response.getMessage() == null) {
                new PostLoginUI().run(serverFacade, authToken);
            } else {
                System.out.println(response.getMessage());
            }

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
    private void register(Scanner scanner){
        System.out.println("Username:");
        String username = scanner.next();
        System.out.println("Password:");
        String password = scanner.next();
        System.out.println("Email:");
        String email = scanner.next();
        RegisterRequest request = new RegisterRequest(username, password, email);

        try {
            AuthResponse response = serverFacade.registerUser(request);
            String authToken = response.getAuthToken();

            if (response.getMessage() == null) {
                new PostLoginUI().run(serverFacade, authToken);
            } else {
                System.out.println(response.getMessage());
            }

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }
}