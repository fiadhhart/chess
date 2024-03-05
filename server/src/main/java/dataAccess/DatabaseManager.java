package dataAccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    static void initializeDatabaseAndTables() throws DataAccessException {
        createDatabase();
        createTables();
    }

    private static void createTables() throws DataAccessException {
        try (Connection conn = DriverManager.getConnection(connectionUrl + "/" + databaseName, user, password)) {
            // Create users table
            String createUsersTableQuery = "CREATE TABLE IF NOT EXISTS users (" +
                    "username VARCHAR(255) NOT NULL PRIMARY KEY," +
                    "password VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL" +
                    ")";
            try (var statement = conn.prepareStatement(createUsersTableQuery)) {
                statement.executeUpdate();
            }

            // Create games table
            String createGamesTableQuery = "CREATE TABLE IF NOT EXISTS games (" +
                    "gameID INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "whiteUsername VARCHAR(255)," +
                    "blackUsername VARCHAR(255)," +
                    "gameName VARCHAR(255) NOT NULL," +
                    "game LONGTEXT," +
                    "FOREIGN KEY (whiteUsername) REFERENCES users(username)," +
                    "FOREIGN KEY (blackUsername) REFERENCES users(username)" +
                    ")";
            try (var statement = conn.prepareStatement(createGamesTableQuery)) {
                statement.executeUpdate();
            }

            // Create auths table
            String createAuthsTableQuery = "CREATE TABLE IF NOT EXISTS auths (" +
                    "authToken VARCHAR(255) NOT NULL PRIMARY KEY," +
                    "username VARCHAR(255) NOT NULL," +
                    "FOREIGN KEY (username) REFERENCES users(username)" +
                    ")";
            try (var statement = conn.prepareStatement(createAuthsTableQuery)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
