package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand {
    public int gameID;
    public JoinObserverCommand(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.JOIN_OBSERVER;
        this.gameID = gameID;
    }
}