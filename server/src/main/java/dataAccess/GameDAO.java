package dataAccess;

import chess.ChessGame;

import java.util.List;

public interface GameDAO {
    Integer createGame(String gameName) throws DataAccessException;
    void updateGameUsername(ChessGame.TeamColor playerColor, Integer gameID, String username) throws DataAccessException;
    List<List<String>> listGames() throws DataAccessException;
    String getGame(String gameName) throws DataAccessException;
    Integer getGame(Integer gameID) throws DataAccessException;
    void clear() throws DataAccessException;
}
