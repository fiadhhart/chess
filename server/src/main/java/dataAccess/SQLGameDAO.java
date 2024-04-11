package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import requests.BaseRequest;
import responses.GameResponse;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() throws DataAccessException{
        DatabaseManager.initializeDatabaseAndTables();
    }


    @Override
    public Integer createGame(String gameName) throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        chessGame.setBoard(board);
        chessGame.setTeamTurn(ChessGame.TeamColor.WHITE);

        Gson gson = new Gson();
        String jsonChessGame = gson.toJson(chessGame);

        String sql = "INSERT INTO games (gameName, game) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, gameName);
            preparedStatement.setString(2, jsonChessGame);
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new DataAccessException("Error creating game, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating game: " + e.getMessage());
        }
    }

    @Override
    public boolean isPlayerOccupied(ChessGame.TeamColor playerColor, Integer gameID) throws DataAccessException {
        String usernameColumn;
        if (playerColor == ChessGame.TeamColor.WHITE) {usernameColumn = "whiteUsername";}
        else if (playerColor == ChessGame.TeamColor.BLACK) {usernameColumn = "blackUsername";}
        else if (playerColor == null) {return false;}
        else{throw new DataAccessException("playerColor is not type ChessGame.TeamColor");}

        String sql = "SELECT * FROM games WHERE gameID = ? AND " + usernameColumn + " IS NOT NULL";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, gameID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error checking player occupancy: " + e.getMessage());
        }
    }

    @Override
    public void updateGameUsername(ChessGame.TeamColor playerColor, Integer gameID, String username) throws DataAccessException {
        String usernameColumn;
        if (playerColor == ChessGame.TeamColor.WHITE) {usernameColumn = "whiteUsername";}
        else if (playerColor == ChessGame.TeamColor.BLACK) {usernameColumn = "blackUsername";}
        else if (playerColor == null) {usernameColumn = null;}
        else{throw new DataAccessException("playerColor is not type ChessGame.TeamColor");}

        if(usernameColumn != null){
            String sql = "UPDATE games SET " + usernameColumn + " = ? WHERE gameID = ?";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error updating game username: " + e.getMessage());
            }
        }else{
            //add as observer
        }
    }

    @Override
    public List<GameResponse> listGames() throws DataAccessException {
        List<GameResponse> allGames = new ArrayList<>();

        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName FROM games";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int gameID = resultSet.getInt("gameID");
                String whiteUsername = resultSet.getString("whiteUsername");
                String blackUsername = resultSet.getString("blackUsername");
                String gameName = resultSet.getString("gameName");
                allGames.add(new GameResponse(gameID, whiteUsername, blackUsername, gameName));
            }
            return allGames;
        } catch (SQLException e) {
            throw new DataAccessException("Error listing games: " + e.getMessage());
        }
    }

    @Override
    public String getGame(String gameName) throws DataAccessException {
        String sql = "SELECT gameName FROM games WHERE gameName = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, gameName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("gameName");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting game: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Integer getGame(Integer gameID) throws DataAccessException {
        String sql = "SELECT gameID FROM games WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, gameID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("gameID");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting game: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM games";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing games: " + e.getMessage());
        }
    }

    public ChessGame getChessGame(Integer gameID) throws DataAccessException{
        String sql = "SELECT game FROM games WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, gameID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String jsonChessGame = resultSet.getString("game");

                    Gson gson = new Gson();
                    return gson.fromJson(jsonChessGame, ChessGame.class);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting chess game: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String getPlayer(ChessGame.TeamColor playerColor, Integer gameID) throws DataAccessException {
        String username;
        if (playerColor == ChessGame.TeamColor.WHITE) {username = "whiteUsername";}
        else if (playerColor == ChessGame.TeamColor.BLACK) {username = "blackUsername";}
        else {throw new DataAccessException("Invalid player color");}

        String sql = "SELECT " + username + " FROM games WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, gameID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString(username);
                } else {
                    // No username found for the specified player color
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting player: " + e.getMessage());
        }
    }

    @Override
    public void setGame(Integer gameID, ChessGame game) throws DataAccessException {
        Gson gson = new Gson();
        String jsonChessGame = gson.toJson(game);

        String sql = "UPDATE games SET game = ? WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, jsonChessGame);
            preparedStatement.setInt(2, gameID);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new DataAccessException("Failed to update game with ID " + gameID + ". Game not found.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error updating game in the database: " + e.getMessage());
        }
    }

    @Override
    public void removeUser(ChessGame.TeamColor playerColor, Integer gameID) throws DataAccessException {
        String usernameColumn;
        if (playerColor == ChessGame.TeamColor.WHITE) {
            usernameColumn = "whiteUsername";
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            usernameColumn = "blackUsername";
        } else {
            // Handle case where playerColor is null or an invalid TeamColor
            throw new DataAccessException("Invalid player color provided.");
        }

        String sql = "UPDATE games SET " + usernameColumn + " = NULL WHERE gameID = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, gameID);
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated == 0) {
                // Handle case where no rows were updated
                throw new DataAccessException("Failed to remove user from game with ID " + gameID + ".");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error removing user from game: " + e.getMessage());
        }
    }
}
