package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class LocalDataAccess implements DataAccess {

    private final ArrayList<AuthData> authDataList = new ArrayList<>();
    private final ArrayList<UserData> userDataList = new ArrayList<>();
    private final ArrayList<GameData> gameDataList = new ArrayList<>();

    @Override
    public void clear() {
        // clear
        authDataList.clear();
        userDataList.clear();
        gameDataList.clear();
    }

    @Override
    public void createUser(UserData userData) {
        // create user
        userDataList.add(userData);
    }

    @Override
    public UserData getUser(String username) {
        // get User
        for (var user : userDataList) {
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public int createGame(String gameName) {
        // create Game
        GameData game = new GameData(gameName, gameDataList.toArray().length+1);
        gameDataList.add(game);
        return game.getGameID();
    }

    @Override
    public GameData getGame(int gameID) {
        // get Game
        return gameDataList.get(gameID - 1);
    }

    @Override
    public ArrayList<GameData> listGames() {
        // list games
        return gameDataList;
    }

    @Override
    public void updateGame(GameData gameData) {
        // update Game
        gameDataList.set(gameData.getGameID() - 1, gameData);
    }

    @Override
    public void createAuth(AuthData authData) {
        // create Auth
        authDataList.add(authData);
    }

    @Override
    public AuthData getAuth(String authToken) {
        // get auth
        for (var auth : authDataList) {
            if (Objects.equals(auth.getAuthToken(), authToken)) {
                return auth;
            }
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) {
        // delete Auth
        authDataList.remove(authData);
    }
}
