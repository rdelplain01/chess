package services;

import dataaccess.DataAccess;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServicesTest {

    @Test
    void clear() {
        String username = "test";
        Services.register(username, "testPass", "test@mail.com");
        assertNotNull(DataAccess.getUser(username));
        Services.clear();
        assertNull(DataAccess.getUser(username));
    }

    @Test
    void normalRegister() {
        UserData userData = new UserData("test", "testPass", "test@mail.com");
        Services.register(userData.getUsername(), userData.getPassword(), userData.getEmail());
        assertEquals(userData, DataAccess.getUser(userData.getUsername()));
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