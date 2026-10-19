package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;

public interface DataAccess {
    void clear();

    void createUser(UserData userData);

    UserData getUser(String username);

    int createGame(String gameName);

    GameData getGame(int gameID);

    ArrayList<GameData> listGames();

    void updateGame(GameData gameData);

    void createAuth(AuthData authData);

    AuthData getAuth(String authToken);

    void deleteAuth(AuthData authData);
}
