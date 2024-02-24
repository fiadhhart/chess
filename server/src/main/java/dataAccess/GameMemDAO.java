package dataAccess;
import chess.ChessGame;
import model.GameData;

import java.util.*;

public class GameMemDAO implements GameDAO{
    private Database database = new Database();

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

    @Override
    public Integer getGame(Integer gameID) throws DataAccessException{
        if (database.games.containsKey(gameID)) {
            return gameID;
        }
        return null;
    }

    @Override
    public void clear () throws DataAccessException{
        database.games.clear();
    }
}
