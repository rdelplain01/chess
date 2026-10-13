package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class DataAccess {

    private static ArrayList<AuthData> authDataList = new ArrayList<>();
    private static ArrayList<UserData> userDataList = new ArrayList<>();
    private static ArrayList<GameData> gameDataList = new ArrayList<>();

    public static void clear() {
        // clear
        authDataList.clear();
        userDataList.clear();
        gameDataList.clear();
    }

    public static void createUser(UserData userData) {
        // create user
        userDataList.add(userData);
    }

    public static UserData getUser(String username) {
        // get User
        for (var user : userDataList) {
            if (Objects.equals(user.getUsername(), username)) {
                return user;
            }
        }
        return null;
    }

    public static void createGame() {
        // create Game
    }

    public static void getGame() {
        // get Game
    }

    public static void listGames() {
        // list games
    }

    public static void updateGame() {
        // update Game
    }

    public static void createAuth(AuthData authData) {
        // create Auth
        authDataList.add(authData);
    }

    public static AuthData getAuth(String authToken) {
        // get auth
        for (var auth : authDataList) {
            if (Objects.equals(auth.getAuthToken(), authToken)) {
                return auth;
            }
        }
        return null;
    }

    public static void deleteAuth(AuthData authData) {
        // delete Auth
        authDataList.remove(authData);
    }
}
