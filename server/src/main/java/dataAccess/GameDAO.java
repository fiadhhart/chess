package dataAccess;

import chess.ChessGame;
import responses.GameResponse;

import java.util.List;

public interface GameDAO {
    Integer createGame(String gameName) throws DataAccessException;
    boolean isPlayerOccupied(ChessGame.TeamColor playerColor, Integer gameID) throws DataAccessException;
    void updateGameUsername(ChessGame.TeamColor playerColor, Integer gameID, String username) throws DataAccessException;
    List<GameResponse> listGames() throws DataAccessException;
    String getGame(String gameName) throws DataAccessException;
    Integer getGame(Integer gameID) throws DataAccessException;
    void clear() throws DataAccessException;

    ChessGame getChessGame(Integer gameID) throws DataAccessException;
    String getPlayer(ChessGame.TeamColor playerColor, Integer gameID) throws DataAccessException;
    void setGame(Integer gameID, ChessGame game) throws DataAccessException;
    void removeUser(ChessGame.TeamColor playerColor, Integer gameID) throws DataAccessException;
}