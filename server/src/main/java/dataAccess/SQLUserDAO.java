package dataAccess;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO() throws DataAccessException{
        DatabaseManager.initializeDatabaseAndTables();
    }


    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);

        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error creating user: " + e.getMessage());
        }
    }

    @Override
    public String getUser(String username) throws DataAccessException {
        String sql = "SELECT username FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("username");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting user: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String[] getUser(String username, String password) throws DataAccessException {
        String sql = "SELECT username, password FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    if (encoder.matches(password, storedPassword)) {
                        return new String[]{resultSet.getString("username"), storedPassword};
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error getting user: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM users";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error clearing users: " + e.getMessage());
        }
    }
}
