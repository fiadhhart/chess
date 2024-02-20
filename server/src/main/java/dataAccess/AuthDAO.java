package dataAccess;
import model.AuthData;

public class AuthDAO {

    //createAuth(username)
    //insert username, authToken into auth
    //returns authToken
    String insertAuth(String username) throws DataAccessException{


        String authToken = "";
        return authToken;
    }

    //getAuth(authToken)
    //select authToken from auth
    //return authToken
    String selectAuth(String authToken) throws DataAccessException{

        return authToken;
    }

    //deleteAuth(authToken) OR clear()
    //remove username, authToken from auth
    //returns null
    void removeAuth (String authToken) throws DataAccessException{

    }
}
