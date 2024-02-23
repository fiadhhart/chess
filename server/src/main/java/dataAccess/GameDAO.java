package dataAccess;
import chess.ChessGame;
import model.GameData;

import java.util.*;

public class GameDAO {

    //createGame(gameName)
    //insert gameID, gameName, game into game
    //return gameID
    //insertGame
    public static Integer createGame(String gameName) throws DataAccessException{

        Integer gameID = gameName.hashCode();
        while (Database.games.containsKey(gameID)) {
            gameID++;
        }

        GameData game = new GameData(gameID, gameName);
        Database.games.put(gameID, game);

        return gameID;
    }

    //updateGameUsername(playerColor, gameID, username)
    //insert whiteUsername or blackUsername or observer into game
    //no return
    //updateGameUsername
    public static void updateGameUsername(ChessGame.TeamColor playerColor, Integer gameID, String username) throws DataAccessException{

        if (playerColor == ChessGame.TeamColor.WHITE){
            Database.games.get(gameID).setWhiteUsername(username);
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            Database.games.get(gameID).setBlackUsername(username);
        } else if (playerColor == null) {
            //add as observer
        }

    }


    //listGames()
    //select gameID, whiteUsername, blackUsername, gameName from all game
    //return list games: {gameID, whiteUsername, blackUsername, gameName}
    //selectAllGames
    public static List<List<String>> listGames() throws DataAccessException{

        List<List<String>> allGames = new ArrayList<>();

        for (Map.Entry<Integer, GameData> entry : Database.games.entrySet()) {
            GameData game = entry.getValue();

            List<String> gameInfo = new ArrayList<>();

            gameInfo.add(String.valueOf(game.getGameID()));
            gameInfo.add(game.getWhiteUsername());
            gameInfo.add(game.getBlackUsername());
            gameInfo.add(game.getGameName());

            allGames.add(gameInfo);
        }
        return allGames;
    }

    //getGame(gameName)
    //select gameName from game
    //return gameName or null
    //selectGame
    public static String getGame(String gameName) throws DataAccessException{
        for (Map.Entry<Integer, GameData> entry : Database.games.entrySet()) {
            GameData game = entry.getValue();
            if (game.getGameName().equals(gameName)) {
                return gameName;
            }
        }
        return null;
    }

    //getGame(gameID)
    //select gameID from game
    //return gameID or null
    //selectGame
    public static Integer getGame(Integer gameID) throws DataAccessException{
        if (Database.games.containsKey(gameID)) {
            return gameID;
        }
        return null;
    }

    //clear()
    //remove all from game
    //no return
    //clearGames
    public static void clear () throws DataAccessException{
        Database.games.clear();
    }


}
