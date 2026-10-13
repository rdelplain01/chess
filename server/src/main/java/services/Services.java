package services;

import dataaccess.DataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;

public class Services {
    public static void clearUsers() {
        // Clear Users
        DataAccess.clear();
    }

    public static void clearGames() {
        // Clear Games
    }

    public static void clearAuth() {
        // Clear Auth
    }

    public static UserData getUser(String username) {
        // Get Users
        return DataAccess.getUser(username);
    }

    public static void createUser(UserData userData) {
        // Create User
        DataAccess.createUser(userData);
    }

    public static void createAuth(AuthData authData) {
        // Create Auth
        DataAccess.createAuth(authData);
    }

    public static boolean verifyPassword(String password, UserData userData) {
        // Do stuff
        return true;
    }

    public static AuthData isAuth(String authToken) {
        // Do stuff
        return DataAccess.getAuth(authToken);
    }

    public static void deleteAuth(AuthData authData) {
        // Do stuff
        DataAccess.deleteAuth(authData);
    }

    public static ArrayList<GameData> listGames() {
        // Do stuff
        return DataAccess.listGames();
    }

    public static int createGame(String gameName) {
        // Do stuff
        return DataAccess.createGame(gameName);
    }

    public static void getGame(String gameID) {
        // Do stuff
    }

    public static void checkColor(String playerColor) {
        // Do stuff
    }

    public static void updateGame(String gameData) {
        // Do stuff
    }
}
