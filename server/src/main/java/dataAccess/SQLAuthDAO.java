package dataAccess;

import java.sql.*;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{

    public SQLAuthDAO() throws DataAccessException{
        DatabaseManager.initializeDatabaseAndTables();
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        String sql = "INSERT INTO auths (authToken, username) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, authToken);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
            return authToken;
        } catch (SQLException e) {
            throw new DataAccessException("Error creating auth: " + e.getMessage());
        }
    }

    @Override
    public String getAuth(String authToken) throws DataAccessException {
        String sql = "SELECT authToken FROM auths WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, authToken);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("authToken");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting auth: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String getUsername(String authToken) throws DataAccessException {
        String sql = "SELECT username FROM auths WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, authToken);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("username");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting username: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        String sql = "DELETE FROM auths WHERE authToken = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, authToken);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error deleting auth: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM auths";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing auths: " + e.getMessage());
        }
    }
}
