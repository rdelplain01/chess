package services;

import dataaccess.DataAccess;
import model.AuthData;
import model.UserData;

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

    public static void verifyPassword(String password, String userData) {
        // Do stuff
    }

    public static void isAuth(String authToken) {
        // Do stuff
    }

    public static void deleteAuth(String authData) {
        // Do stuff
    }

    public static void listGames() {
        // Do stuff
    }

    public static void createGame() {
        // Do stuff
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
