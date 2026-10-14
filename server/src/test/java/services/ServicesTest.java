package services;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServicesTest {

    @BeforeEach
    void clearAll() {
        Services.clear();
    }

    @Test
    void clear() {
        String username = "test";
        assertDoesNotThrow(()-> {
            Services.register(username, "testPass", "test@mail.com");
        });
        assertNotNull(DataAccess.getUser(username));
        Services.clear();
        assertNull(DataAccess.getUser(username));
    }

    @Test
    void normalRegister() {
        UserData userData = new UserData("test", "testPass", "test@mail.com");
        assertDoesNotThrow(() -> {
            Services.register(userData.getUsername(), userData.getPassword(), userData.getEmail());
        });
        assertEquals(userData, DataAccess.getUser(userData.getUsername()));
    }

    @Test
    void userNameExistsRegister() {
        UserData userData = new UserData("test", "testPass", "test@mail.com");
        assertDoesNotThrow(() -> {
            Services.register(userData.getUsername(), userData.getPassword(), userData.getEmail());
        });
        assertThrows(DataAccessException.class, () -> {
            Services.register(userData.getUsername(), userData.getPassword(), userData.getEmail());
        });
    }

    UserData createDummyUser() {
        UserData userData = new UserData("test", "testPass", "test@mail.com");
        assertDoesNotThrow(() -> {
            Services.register(userData.getUsername(), userData.getPassword(), userData.getEmail());
        });
        return userData;
    }

    @Test
    void normalLogin() {
        UserData user = createDummyUser();
        assertDoesNotThrow(() -> {
            Services.login(user.getUsername(), user.getPassword());
        });
    }

    @Test
    void badUsernameLogin() {
        UserData user = createDummyUser();
        assertThrows(DataAccessException.class, () -> {
            Services.login("idk", user.getPassword());
        });
    }

    @Test
    void badPasswordLogin() {
        UserData user = createDummyUser();
        assertThrows(DataAccessException.class, () -> {
            Services.login(user.getUsername(), "idk123");
        });
    }

    @Test
    void normalLogout() throws DataAccessException {
        UserData user = createDummyUser();
        AuthData authData = Services.login(user.getUsername(), user.getPassword());
        assertDoesNotThrow(()->{
            Services.logout(authData.getAuthToken());
        });
    }

    @Test
    void badLogout() {
        assertThrows(DataAccessException.class,()->{
            Services.logout("1");
        });
    }

    @Test
    void listGames() {
    }

    @Test
    void createGame() {
    }

    @Test
    void joinGame() {
    }
}