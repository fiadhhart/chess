import facade.ServerFacade;
import ui.PreLoginUI;

public class Main {
    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Client starting");

        ServerFacade serverFacade = new ServerFacade("http://localhost:" + 3030);
        new PreLoginUI().run(serverFacade);
    }
}