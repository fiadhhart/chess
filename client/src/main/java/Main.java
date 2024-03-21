import chess.*;
import facade.ServerFacade;
import server.Server;
import ui.PreLoginUI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("♕ 240 Chess Client starting");

        //Server server = new Server();
        //server.run(8080);
        ServerFacade serverFacade = new ServerFacade("http://localhost:" + 8080);

        //serverFacade.clearDatabase(null);

        new PreLoginUI().run(serverFacade);

        //server.stop();
    }
}