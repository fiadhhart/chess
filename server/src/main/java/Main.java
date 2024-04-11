import server.Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("♕ 240 Chess Server starting");

        new Server().run(3030);
    }
}