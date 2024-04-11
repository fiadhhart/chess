package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {
    public final String errorMessage;
    public ErrorMessage(String msg) {
        super(ServerMessageType.ERROR);
        this.errorMessage = msg;
    }
}