package server;

import dataAccess.*;
import dataAccess.memory.AuthMemDAO;
import dataAccess.memory.Database;
import dataAccess.memory.GameMemDAO;
import dataAccess.memory.UserMemDAO;
import handler.*;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        //create DB & DAOs
        UserDAO userDAO;
        GameDAO gameDAO;
        AuthDAO authDAO;

        boolean useMemory = false;
        if(useMemory){
            Database database = new Database();
            userDAO = new UserMemDAO(database);
            gameDAO = new GameMemDAO(database);
            authDAO = new AuthMemDAO(database);
        }else{
            try {
                userDAO = new SQLUserDAO();
                gameDAO = new SQLGameDAO();
                authDAO = new SQLAuthDAO();
            } catch (DataAccessException e) {
                throw new RuntimeException(e);
            }
        }

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", (req, res) -> (new RegisterHandler(userDAO, gameDAO, authDAO)).handle(req, res));
        Spark.post("/session", (req, res) -> (new LoginHandler(userDAO, gameDAO, authDAO)).handle(req, res));
        Spark.delete("/session", (req, res) -> (new LogoutHandler(userDAO, gameDAO, authDAO)).handle(req, res));
        Spark.get("/game", (req, res) -> (new ListGamesHandler(userDAO, gameDAO, authDAO)).handle(req, res));
        Spark.post("/game", (req, res) -> (new CreateGameHandler(userDAO, gameDAO, authDAO)).handle(req, res));
        Spark.put("/game", (req, res) -> (new JoinGameHandler(userDAO, gameDAO, authDAO)).handle(req, res));
        Spark.delete("/db", (req, res) -> (new ClearHandler(userDAO, gameDAO, authDAO)).handle(req, res));

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}