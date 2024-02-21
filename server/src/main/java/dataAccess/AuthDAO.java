package dataAccess;
import model.AuthData;

import java.util.UUID;

public class AuthDAO {

    //createAuth(username)
    //insert username, authToken into auth
    //returns authToken
    public static String insertAuth(String username) throws DataAccessException{
        String authToken = UUID.randomUUID().toString();

        AuthData auth = new AuthData(authToken, username);
        Database.auths.put(authToken, auth);

        return authToken;
    }

    //getAuth(authToken)
    //select authToken from auth
    //return authToken or null
    public static String selectAuth(String authToken) throws DataAccessException{
        if (Database.auths.containsKey(authToken)) {
            return authToken;
        }
        return null;
    }

    //getUsername(authToken)
    //select username from auth
    //return username
    public static String selectAuthUsername(String authToken) throws DataAccessException{
        if (Database.auths.containsKey(authToken)) {
            return Database.auths.get(authToken).getUsername();
        }
        return null;
    }

    //deleteAuth(authToken)
    //remove username, authToken from auth
    //no return
    public static void removeAuth (String authToken) throws DataAccessException{
        Database.auths.remove(authToken);
    }

    //clear()
    //remove all from auth
    //no return
    public static void clearAuths () throws DataAccessException{
        Database.auths.clear();
    }
}
