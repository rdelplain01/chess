package client;

import client.ChessClient;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;


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

    @Test
    public void simpleRegister() {
        UserData user = new UserData("test", "testPass", "test@mail.com");
        Assertions.assertDoesNotThrow(() -> {
            client.register(user);
        });
    }

}
