package ui;

import facade.ServerFacade;

public class GameplayUI {
    private ServerFacade serverFacade;
    public void run(ServerFacade serverFacade) {
        this.serverFacade = serverFacade;

        System.out.println("In gameplayUI");

    }
}
