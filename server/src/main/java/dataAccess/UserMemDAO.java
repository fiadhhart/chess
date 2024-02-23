package dataAccess;
import model.UserData;

public class UserMemDAO implements UserDAO{

    //createUser(username, password, email)
    //insert username, password, email into user
    //no return
    //insertUser
    @Override
    public void createUser(String username, String password, String email) throws DataAccessException{
        UserData user = new UserData(username, password, email);
        Database.users.put(username, user);
    }

    //getUser(username)
    //select username from user
    //returns the username or null
    //selectUser
    public static String getUser(String username) throws DataAccessException{
        if (Database.users.containsKey(username)) {
            return username;
        }
        return null;
    }

    //getUser(username, password)
    //select username, password from user
    //returns the username, password or null
    //selectUser
    public static String[] getUser(String username, String password) throws DataAccessException{
        if (Database.users.containsKey(username)) {
            if (Database.users.get(username).getPassword().equals(password)){
                return new String[]{username, password};
            }
        }
        return null;
    }

    //clear()
    //remove all from user
    //no return
    //clearUsers
    public static void clear () throws DataAccessException{
        Database.users.clear();
    }

}
