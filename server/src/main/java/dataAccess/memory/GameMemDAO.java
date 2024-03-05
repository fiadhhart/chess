package dataAccess.memory;
import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;
import responses.GameResponse;

import java.util.*;

public class GameMemDAO implements GameDAO {
    private Database database;

    public GameMemDAO(Database database) {
        this.database = database;
    }

    @Override
    public Integer createGame(String gameName) throws DataAccessException {

        Integer gameID = database.getGamesSize() +1;

        GameData game = new GameData(gameID, gameName);
        database.games.put(gameID, game);

        return gameID;
    }

    @Override
    public boolean isPlayerOccupied(ChessGame.TeamColor playerColor, Integer gameID) throws DataAccessException{
        if (playerColor == ChessGame.TeamColor.WHITE){
            String whiteUsername = database.games.get(gameID).getWhiteUsername();
            if(whiteUsername == null){
                return false;
            }
            return true;

        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            String blackUsername = database.games.get(gameID).getBlackUsername();
            if(blackUsername == null){
                return false;
            }
            return true;

        } else if (playerColor == null) {
            return false;
        }else{
            throw new DataAccessException("playerColor is not type ChessGame.TeamColor");
        }
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
    public List<GameResponse> listGames() throws DataAccessException{

        List<GameResponse> allGames = new ArrayList<>();

        for (Map.Entry<Integer, GameData> entry : database.games.entrySet()) {
            GameData game = entry.getValue();

            int gameID = game.getGameID();
            String whiteUsername = game.getWhiteUsername();
            String blackUsername = game.getBlackUsername();
            String gameName = game.getGameName();

            GameResponse gameInfo = new GameResponse(gameID, whiteUsername, blackUsername, gameName);

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