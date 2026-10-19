package services;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Objects;

public class Services {

    private final DataAccess dataAccess;

    public Services(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public void clear() {
        // Clear Users
        dataAccess.clear();
    }

    private AuthData isAuth(String authToken) throws DataAccessException {
        AuthData authData = dataAccess.getAuth(authToken);
        if (authData == null) {
            throw new DataAccessException("Error: Invalid Permissions");
        }
        return authData;
    }

    private boolean verifyPassword(String password, UserData userData) {
        // Do stuff
        return Objects.equals(password, userData.getPassword());
    }

    private boolean checkColor(String playerColor, GameData gameData) throws DataAccessException {
        if (Objects.equals(playerColor, "WHITE")) {
            return gameData.getWhiteUsername() != null;
        } else if(Objects.equals(playerColor, "BLACK")) {
            return gameData.getBlackUsername() != null;
        }
        throw new DataAccessException("Error: Player color does not exist");
    }

    public AuthData register(String username, String password, String email) throws DataAccessException {
        UserData userData = new UserData(username, password, email);

        if (dataAccess.getUser(username) != null) {
            throw new DataAccessException("Error: username already taken");
        }
        dataAccess.createUser(userData);

        AuthData authData = new AuthData(username);

        dataAccess.createAuth(authData);

        return authData;
    }

    public AuthData login(String username, String password) throws DataAccessException {
        UserData user = dataAccess.getUser(username);

        if (user == null) {
            throw new DataAccessException("Error: username or password is incorrect");
        }

        if (!verifyPassword(password, user)) {
            throw new DataAccessException("Error: username or password is incorrect");
        }

        AuthData authData = new AuthData(user.getUsername());

        dataAccess.createAuth(authData);

        return authData;
    }

    public void logout(String authToken) throws DataAccessException {
        AuthData authData = isAuth(authToken);
        dataAccess.deleteAuth(authData);
    }

    public ArrayList<GameData> listGames(String authToken) throws DataAccessException {
        isAuth(authToken);
        return dataAccess.listGames();
    }

    public int createGame(String authToken, String gameName) throws DataAccessException {
        isAuth(authToken);
        return dataAccess.createGame(gameName);
    }

    public void joinGame(String authToken, int gameID, String playerColor) throws DataAccessException {
        AuthData authData = isAuth(authToken);
        GameData gameData;
        try {
            gameData = dataAccess.getGame(gameID);
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

        dataAccess.updateGame(gameData);
    }
}
