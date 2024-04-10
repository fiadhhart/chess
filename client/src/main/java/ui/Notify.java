package ui;

import webSocketMessages.serverMessages.ServerMessage;

public interface Notify {
    void notify(ServerMessage notification);
}