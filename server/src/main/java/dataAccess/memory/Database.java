package dataAccess.memory;

import model.UserData;
import model.GameData;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;


public class Database {
    public Map<String, UserData> users = new HashMap<>();     //key is username
    protected Map<Integer, GameData> games = new HashMap<>();    //key is gameID
    protected Map<String, AuthData> auths = new HashMap<>();     //key is authToken


    //methods for testing purposes
    public void clearDatabase(){
        users.clear();
        games.clear();
        auths.clear();
    }

    public int getUsersSize(){
        return users.size();
    }
    public int getGamesSize(){
        return games.size();
    }
    public int getAuthsSize(){
        return auths.size();
    }
}