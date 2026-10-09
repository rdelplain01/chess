package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import services.Services;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", Server::deleteDB);
        javalin.post("/user", Server::register);
        javalin.post("/session", Server::login);
        javalin.delete("/session", Server::logout);
        javalin.get("/game", Server::listGames);
        javalin.post("/game", Server::createGame);
        javalin.put("/game", Server::joinGame);
    }


    private static void register(Context ctx) {
        System.out.println(ctx.body());
        Services.getUser("");
        Services.createUser("");
        Services.createAuth("");
        var result = Map.of("username", "", "authToken", "");
        ctx.result(new Gson().toJson(result));
    }

    private static void deleteDB(Context ctx) {
        // Delete Database
        Services.clearUsers();
        Services.clearGames();
        Services.clearAuth();
    }

    private static void login(Context ctx) {
        // Login
        Services.getUser("");
        Services.verifyPassword("", "");
        Services.createAuth("");
    }

    private static void logout(Context ctx) {
        // Logout
        Services.isAuth("");
        Services.deleteAuth("");
    }

    private static void listGames(Context ctx) {
        // List Games
        Services.isAuth("");
        Services.listGames();
    }

    private static void createGame(Context ctx) {
        // Create Game
        Services.isAuth("");
        Services.createGame();
    }

    private static void joinGame(Context ctx) {
        // Join Game
        Services.isAuth("");
        Services.getGame("");
        Services.checkColor("");
        Services.updateGame("");
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
