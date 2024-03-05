package dataAccess.memory;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.UserData;

public class UserMemDAO implements UserDAO {
    private Database database;

    public UserMemDAO(Database database) {
        this.database = database;
    }

    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        UserData user = new UserData(username, password, email);
        database.users.put(username, user);
    }

    @Override
    public String getUser(String username) throws DataAccessException{
        if (database.users.containsKey(username)) {
            return username;
        }
        return null;
    }

    @Override
    public String[] getUser(String username, String password) throws DataAccessException{
        if (database.users.containsKey(username)) {
            if (database.users.get(username).getPassword().equals(password)){
                return new String[]{username, password};
            }
        }
        return null;
    }

    @Override
    public void clear () throws DataAccessException{
        database.users.clear();
    }
}