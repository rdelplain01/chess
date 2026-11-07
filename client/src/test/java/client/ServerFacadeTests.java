package client;

import client.ChessClient;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.util.Map;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade client;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        client = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    public void delete() {
        client.deleteDB();
    }

    private UserData registerDummyUser() {
        UserData user = new UserData("test", "testPass", "test@mail.com");
        client.register(user);
        return user;
    }

    @Test
    public void simpleRegister() {
        UserData user = new UserData("test", "testPass", "test@mail.com");
        Assertions.assertDoesNotThrow(() -> {
            client.register(user);
        });
    }

    @Test
    public void simpleLogin() {
        UserData user = registerDummyUser();
        Map<String, String> loginRequest = Map.of(
                "username", user.getUsername(),
                "password", user.getPassword()
        );
        Assertions.assertDoesNotThrow(() -> {
            client.login(loginRequest);
        });
    }

}
