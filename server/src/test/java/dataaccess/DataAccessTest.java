package dataaccess;

import dataaccess.sql.MySqlDataAccess;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessTest {

    DataAccess dataAccess;

    @BeforeEach
    void setup() throws SQLException, DataAccessException {
        dataAccess = new MySqlDataAccess();
        dataAccess.clear();
    }

    @Test
    void clear() throws DataAccessException {
        UserData user = createDummyUser();
        assertDoesNotThrow(() -> {
            dataAccess.clear();
        });
        assertNull(dataAccess.getUser(user.getUsername()));
    }

    @Test
    void createUserAndGet() throws DataAccessException {
        UserData user = createDummyUser();
        assertNotNull(dataAccess.getUser(user.getUsername()));
    }

    @Test
    void createGameAndGet() throws DataAccessException {
        int id = dataAccess.createGame("test");
        assertNotNull(dataAccess.getGame(id));
    }

    @Test
    void listGames() throws DataAccessException {
        dataAccess.createGame("test");
        assertNotNull(dataAccess.listGames());
    }

    @Test
    void updateGame() throws DataAccessException {
        int id = dataAccess.createGame("test");
        GameData game = dataAccess.getGame(id);
        game.setWhiteUsername("whitePlayer");

        dataAccess.updateGame(game);

        GameData updated = dataAccess.getGame(id);
        assertEquals("whitePlayer", updated.getWhiteUsername());
        assertNull(updated.getBlackUsername());
    }

    @Test
    void createAuthAndGet() throws DataAccessException {
        AuthData authData = new AuthData("test");
        dataAccess.createAuth(authData);
        assertEquals(authData, dataAccess.getAuth(authData.getAuthToken()));
    }

    @Test
    void deleteAuth() throws DataAccessException {
        AuthData authData = new AuthData("test");
        dataAccess.createAuth(authData);
        dataAccess.deleteAuth(authData);
        assertNull(dataAccess.getAuth(authData.getAuthToken()));
    }

    UserData createDummyUser() throws DataAccessException {
        UserData user = new UserData("test", "test1234", "test@mail.com");
        dataAccess.createUser(user);
        return user;
    }
}