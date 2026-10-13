package services;

import dataaccess.DataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

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

    public static GameData getGame(int gameID) {
        // Do stuff
        return DataAccess.getGame(gameID);
    }

    public static boolean checkColor(String playerColor, GameData gameData) {
        // Do stuff
        if (Objects.equals(playerColor, "WHITE")) {
            return gameData.getWhiteUsername() != null;
        } else if(Objects.equals(playerColor, "BLACK")) {
            return gameData.getBlackUsername() != null;
        }
        return false;
    }

    public static void updateGame(GameData gameData) {
        // Do stuff
        DataAccess.updateGame(gameData);
    }
}
