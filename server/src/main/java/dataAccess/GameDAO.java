package dataAccess;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class GameDAO {

    //listGames()
    //select gameID, whiteUsername, blackUsername, gameName from all game
    //return list games: {gameID, whiteUsername, blackUsername, gameName}
    Collection[] selectGames() throws DataAccessException{

        int gameID = 0;
        String whiteUsername = "";
        String blackUsername = "";
        String gameName = "";
        Collection[] gameInfo = {Collections.singleton(gameID),
                                    Collections.singleton(whiteUsername),
                                    Collections.singleton(blackUsername),
                                    Collections.singleton(gameName)};
        Collection[] allGames = {List.of(gameInfo)};
        return allGames;

    }

    //getGame(gameName)
    //select gameName from game
    //return gameName or null
    String selectGame(String gameName) throws DataAccessException{
        return gameName;
    }

    //getGame(gameID)
    //select gameID from game
    //return gameID
    int selectGame(int gameID) throws DataAccessException{
        return gameID;
    }

    //createGame(gameName)
    //insert gameID, gameName, game into game
    //return gameID
    int insertGame(String gameName) throws DataAccessException{

        int gameID = 0;
        return gameID;

    }

    //updateGame(playerColor, gameID)
    //insert whiteUsername or blackUsername or observer into game
    //return null
    void insertGame_whiteUsernames(String username) throws DataAccessException{

    }


    //clear()
    //remove all from game
    //return null
    void removeGames (GameData g) throws DataAccessException{

    }


}
