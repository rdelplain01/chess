package services;

import dataaccess.DataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class Services {
    public static void clear() {
        // Clear Users
        DataAccess.clear();
    }

    private static boolean verifyPassword(String password, UserData userData) {
        // Do stuff
        return true;
    }

    private static boolean checkColor(String playerColor, GameData gameData) {
        if (Objects.equals(playerColor, "WHITE")) {
            return gameData.getWhiteUsername() != null;
        } else if(Objects.equals(playerColor, "BLACK")) {
            return gameData.getBlackUsername() != null;
        }
        return false;
    }

    public static AuthData register(String username, String password, String email) {
        UserData userData = new UserData(username, password, email);

        if (DataAccess.getUser(username) != null) {
            // Throw exception
        }
        DataAccess.createUser(userData);

        AuthData authData = new AuthData(username);

        DataAccess.createAuth(authData);

        return authData;
    }

    public static AuthData login(String username, String password) {
        UserData user = DataAccess.getUser(username);

        if (user == null) {
            // throw exception
        }

        if (!verifyPassword(password, user)) {
            // Throw Exception
        }

        AuthData authData = new AuthData(user.getUsername());

        DataAccess.createAuth(authData);

        return authData;
    }

    public static void logout(String authToken) {
        AuthData authData = DataAccess.getAuth(authToken);
        if (authData == null) {
            // Throw Exception
        }
        DataAccess.deleteAuth(authData);
    }

    public static ArrayList<GameData> listGames(String authToken) {
        AuthData authData = DataAccess.getAuth(authToken);
        if (authData == null) {
            // Throw Exception
        }
        ArrayList<GameData> gameList = DataAccess.listGames();

        return gameList;
    }

    public static int createGame(String authToken, String gameName) {
        AuthData authData = DataAccess.getAuth(authToken);
        if (authData == null) {
            // Throw Exception
        }
        int gameID = DataAccess.createGame(gameName);

        return gameID;
    }

    public static void joinGame(String authToken, int gameID, String playerColor) {
        AuthData authData = DataAccess.getAuth(authToken);
        if (authData == null) {
            // Throw Exception
        }
        GameData gameData = DataAccess.getGame(gameID);
        if (gameData == null) {
            // Throw Exception
        }

        if (!checkColor(playerColor, gameData)) {
            // throw Exception
        }

        if (Objects.equals(playerColor, "WHITE")) {
            gameData.setWhiteUsername(authData.getUsername());
        } else if(Objects.equals(playerColor, "BLACK")) {
            gameData.setBlackUsername(authData.getUsername());
        }

        DataAccess.updateGame(gameData);
    }
}
