package dataAccess;

import model.UserData;
import model.GameData;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;


public class Database {
    protected static Map<String, UserData> users = new HashMap<>();     //key is username
    protected static Map<Integer, GameData> games = new HashMap<>();    //key is gameID
    protected static Map<String, AuthData> auths = new HashMap<>();     //key is authToken

}