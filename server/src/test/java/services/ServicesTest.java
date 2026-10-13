package services;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
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

    @Test
    void login() {
    }

    @Test
    void logout() {
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