package service;

import dataaccess.DataAccess;
import dataaccess.LocalDataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServicesTest {

    DataAccess dataAccess;
    Services services;

    @BeforeEach
    void setup() {
        dataAccess = new LocalDataAccess();
        services = new Services(dataAccess);
    }

    @Test
    void clear() {
        String username = "test";
        assertDoesNotThrow(()-> {
            services.register(username, "testPass", "test@mail.com");
        });
        assertNotNull(dataAccess.getUser(username));
        services.clear();
        assertNull(dataAccess.getUser(username));
    }

    @Test
    void normalRegister() {
        UserData userData = new UserData("test", "testPass", "test@mail.com");
        assertDoesNotThrow(() -> {
            services.register(userData.getUsername(), userData.getPassword(), userData.getEmail());
        });
        assertEquals(userData, dataAccess.getUser(userData.getUsername()));
    }

    @Test
    void userNameExistsRegister() {
        UserData userData = new UserData("test", "testPass", "test@mail.com");
        assertDoesNotThrow(() -> {
            services.register(userData.getUsername(), userData.getPassword(), userData.getEmail());
        });
        assertThrows(DataAccessException.class, () -> {
            services.register(userData.getUsername(), userData.getPassword(), userData.getEmail());
        });
    }

    UserData createDummyUser() {
        UserData userData = new UserData("test", "testPass", "test@mail.com");
        assertDoesNotThrow(() -> {
            services.register(userData.getUsername(), userData.getPassword(), userData.getEmail());
        });
        return userData;
    }

    @Test
    void normalLogin() {
        UserData user = createDummyUser();
        assertDoesNotThrow(() -> {
            services.login(user.getUsername(), user.getPassword());
        });
    }

    @Test
    void badUsernameLogin() {
        UserData user = createDummyUser();
        assertThrows(DataAccessException.class, () -> {
            services.login("idk", user.getPassword());
        });
    }

    @Test
    void badPasswordLogin() {
        UserData user = createDummyUser();
        assertThrows(DataAccessException.class, () -> {
            services.login(user.getUsername(), "idk123");
        });
    }

    @Test
    void normalLogout() throws DataAccessException {
        UserData user = createDummyUser();
        AuthData authData = services.login(user.getUsername(), user.getPassword());
        assertDoesNotThrow(()->{
            services.logout(authData.getAuthToken());
        });
    }

    @Test
    void badLogout() {
        assertThrows(DataAccessException.class,()->{
            services.logout("1");
        });
    }

    @Test
    void normalListGames() throws DataAccessException {
        UserData user = createDummyUser();
        AuthData authData = services.login(user.getUsername(), user.getPassword());
        services.createGame(authData.getAuthToken(), "test" );
        assertDoesNotThrow(() -> {
            services.listGames(authData.getAuthToken());
        });
    }

    @Test
    void badListGames() throws DataAccessException {
        UserData user = createDummyUser();
        AuthData authData = services.login(user.getUsername(), user.getPassword());
        services.createGame(authData.getAuthToken(), "test" );
        assertThrows(DataAccessException.class,() -> {
            services.listGames("1");
        });
    }

    @Test
    void normalCreateGame() throws DataAccessException {
        UserData user = createDummyUser();
        AuthData authData = services.login(user.getUsername(), user.getPassword());
        assertDoesNotThrow(() -> {
            services.createGame(authData.getAuthToken(), "test" );
        });
    }

    @Test
    void badCreateGame() {
        createDummyUser();
        assertThrows(DataAccessException.class,() -> {
            services.createGame("1", "test" );
        });
    }

    @Test
    void normalWhiteJoinGame() throws DataAccessException {
        UserData user = createDummyUser();
        AuthData authData = services.login(user.getUsername(), user.getPassword());
        String gameName = "test";
        int gameID = services.createGame(authData.getAuthToken(), gameName);
        assertDoesNotThrow(() -> {
            services.joinGame(authData.getAuthToken(), gameID, "WHITE");
        });
    }

    @Test
    void normalBlackJoinGame() throws DataAccessException {
        UserData user = createDummyUser();
        AuthData authData = services.login(user.getUsername(), user.getPassword());
        String gameName = "test";
        int gameID = services.createGame(authData.getAuthToken(), gameName);
        assertDoesNotThrow(() -> {
            services.joinGame(authData.getAuthToken(), gameID, "BLACK");
        });
    }

    @Test
    void badAuthJoinGame() throws DataAccessException {
        UserData user = createDummyUser();
        AuthData authData = services.login(user.getUsername(), user.getPassword());
        String gameName = "test";
        int gameID = services.createGame(authData.getAuthToken(), gameName);
        assertThrows(DataAccessException.class,() -> {
            services.joinGame("1", gameID, "WHITE");
        });
    }

    @Test
    void badGameIDJoinGame() throws DataAccessException {
        UserData user = createDummyUser();
        AuthData authData = services.login(user.getUsername(), user.getPassword());
        String gameName = "test";
        services.createGame(authData.getAuthToken(), gameName);
        assertThrows(DataAccessException.class,() -> {
            services.joinGame(authData.getAuthToken(), 15, "WHITE");
        });
    }

    @Test
    void badColorJoinGame() throws DataAccessException {
        UserData user = createDummyUser();
        AuthData authData = services.login(user.getUsername(), user.getPassword());
        String gameName = "test";
        int gameID = services.createGame(authData.getAuthToken(), gameName);
        services.joinGame(authData.getAuthToken(), gameID, "WHITE");
        assertThrows(DataAccessException.class,() -> {
            services.joinGame(authData.getAuthToken(), gameID, "WHITE");
        });
    }

    @Test
    void badColorDoesNotExistJoinGame() throws DataAccessException {
        UserData user = createDummyUser();
        AuthData authData = services.login(user.getUsername(), user.getPassword());
        String gameName = "test";
        int gameID = services.createGame(authData.getAuthToken(), gameName);
        services.joinGame(authData.getAuthToken(), gameID, "WHITE");
        assertThrows(DataAccessException.class,() -> {
            services.joinGame(authData.getAuthToken(), gameID, "GREEN");
        });
    }
}