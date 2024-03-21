import chess.*;
import server.Server;

public class Main {
    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Server starting");

        new Server().run(8080);
    }
}