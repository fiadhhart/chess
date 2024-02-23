package dataAccess;
import chess.ChessGame;
import model.GameData;

import java.util.*;

public class GameMemDAO implements GameDAO{

    private Database database = new Database();

    //createGame(gameName)
    //insert gameID, gameName, game into game
    //return gameID
    //insertGame
    @Override
    public Integer createGame(String gameName) throws DataAccessException{

        Integer gameID = gameName.hashCode();
        while (database.games.containsKey(gameID)) {
            gameID++;
        }

        GameData game = new GameData(gameID, gameName);
        database.games.put(gameID, game);

        return gameID;
    }

    //updateGameUsername(playerColor, gameID, username)
    //insert whiteUsername or blackUsername or observer into game
    //no return
    //updateGameUsername
    @Override
    public void updateGameUsername(ChessGame.TeamColor playerColor, Integer gameID, String username) throws DataAccessException{

        if (playerColor == ChessGame.TeamColor.WHITE){
            database.games.get(gameID).setWhiteUsername(username);
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            database.games.get(gameID).setBlackUsername(username);
        } else if (playerColor == null) {
            //add as observer
        }

    }


    //listGames()
    //select gameID, whiteUsername, blackUsername, gameName from all game
    //return list games: {gameID, whiteUsername, blackUsername, gameName}
    //selectAllGames
    @Override
    public List<List<String>> listGames() throws DataAccessException{

        List<List<String>> allGames = new ArrayList<>();

        for (Map.Entry<Integer, GameData> entry : database.games.entrySet()) {
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
    @Override
    public String getGame(String gameName) throws DataAccessException{
        for (Map.Entry<Integer, GameData> entry : database.games.entrySet()) {
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
    @Override
    public Integer getGame(Integer gameID) throws DataAccessException{
        if (database.games.containsKey(gameID)) {
            return gameID;
        }
        return null;
    }

    //clear()
    //remove all from game
    //no return
    //clearGames
    @Override
    public void clear () throws DataAccessException{
        database.games.clear();
    }


}
