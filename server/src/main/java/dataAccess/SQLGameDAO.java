package dataAccess;

import chess.ChessGame;
import responses.GameResponse;

import java.util.List;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() throws DataAccessException{
        DatabaseManager.initializeDatabaseAndTables();
    }


    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public boolean isPlayerOccupied(ChessGame.TeamColor playerColor, Integer gameID) throws DataAccessException {
        return false;
    }

    @Override
    public void updateGameUsername(ChessGame.TeamColor playerColor, Integer gameID, String username) throws DataAccessException {

    }

    @Override
    public List<GameResponse> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public String getGame(String gameName) throws DataAccessException {
        return null;
    }

    @Override
    public Integer getGame(Integer gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() throws DataAccessException {

    }
}
