package services;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
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

    private static AuthData isAuth(String authToken) throws DataAccessException {
        AuthData authData = DataAccess.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Error: Invalid Permissions");
        }
        return authData;
    }

    private static boolean verifyPassword(String password, UserData userData) {
        // Do stuff
        return Objects.equals(password, userData.getPassword());
    }

    private static boolean checkColor(String playerColor, GameData gameData) throws DataAccessException {
        if (Objects.equals(playerColor, "WHITE")) {
            return gameData.getWhiteUsername() != null;
        } else if(Objects.equals(playerColor, "BLACK")) {
            return gameData.getBlackUsername() != null;
        }
        throw new DataAccessException("Error: Player color does not exist");
    }

    public static AuthData register(String username, String password, String email) throws DataAccessException {
        UserData userData = new UserData(username, password, email);

        if (DataAccess.getUser(username) != null) {
            throw new DataAccessException("Error: username already taken");
        }
        DataAccess.createUser(userData);

        AuthData authData = new AuthData(username);

        DataAccess.createAuth(authData);

        return authData;
    }

    public static AuthData login(String username, String password) throws DataAccessException {
        UserData user = DataAccess.getUser(username);

        if (user == null) {
            throw new DataAccessException("Error: username or password is incorrect");
        }

        if (!verifyPassword(password, user)) {
            throw new DataAccessException("Error: username or password is incorrect");
        }

        AuthData authData = new AuthData(user.getUsername());

        DataAccess.createAuth(authData);

        return authData;
    }

    public static void logout(String authToken) throws DataAccessException {
        AuthData authData = isAuth(authToken);
        DataAccess.deleteAuth(authData);
    }

    public static ArrayList<GameData> listGames(String authToken) throws DataAccessException {
        isAuth(authToken);
        ArrayList<GameData> gameList = DataAccess.listGames();

        return gameList;
    }

    public static int createGame(String authToken, String gameName) throws DataAccessException {
        isAuth(authToken);
        int gameID = DataAccess.createGame(gameName);

        return gameID;
    }

    public static void joinGame(String authToken, int gameID, String playerColor) throws DataAccessException {
        AuthData authData = isAuth(authToken);
        GameData gameData;
        try {
            gameData = DataAccess.getGame(gameID);
        } catch (Exception e) {
            throw new DataAccessException("Error: Game ID does not exist");
        }

        try {
            if (checkColor(playerColor, gameData)) {
                throw new DataAccessException("Error: game taken");
            }
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }

        if (Objects.equals(playerColor, "WHITE")) {
            gameData.setWhiteUsername(authData.getUsername());
        } else if(Objects.equals(playerColor, "BLACK")) {
            gameData.setBlackUsername(authData.getUsername());
        }

        DataAccess.updateGame(gameData);
    }
}
