package dataAccess;
import model.UserData;

public class UserDAO {

    //createUser(username, password, email)
    //insert username, password, email into user
    //no return
    void insertUser(String username, String password, String email) throws DataAccessException{


    }

    //getUser(username)
    //select username from user
    //returns the username or null
    String selectUser(String username) throws DataAccessException{


        return username;
    }

    //getUser(username, password)
    //select username, password from user
    //returns the username, password or null
    String[] selectUser(String username, String password) throws DataAccessException{

        String[] credentials = {username, password};
        return credentials;
    }

    //clear()
    //remove all from user
    //return null
    void removeUser (UserData u) throws DataAccessException{

    }

}
