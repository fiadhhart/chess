package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {
    public int gameID;
    public ResignCommand(String authToken, int gameID) {
        super(authToken);
        this.commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }
}