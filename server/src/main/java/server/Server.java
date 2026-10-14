package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import services.Services;

import java.util.ArrayList;
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
        Map body = new Gson().fromJson(ctx.body(), Map.class);

        if (body.get("username") == null || body.get("password") == null || body.get("email") == null) {
            ctx.status(400);
            var result = Map.of("message", "Error: Please provide a username, password, or email");
            ctx.result(new Gson().toJson(result));
            return;
        }

        String username = body.get("username").toString();
        String password = body.get("password").toString();
        String email = body.get("email").toString();
        AuthData authData;

        try {
            authData = Services.register(username, password, email);
        } catch (Exception e) {
            ctx.status(403);
            var result = Map.of("message", e.getMessage());
            ctx.result(new Gson().toJson(result));
            return;
        }

        var result = Map.of("username", username, "authToken", authData.getAuthToken());
        ctx.result(new Gson().toJson(result));
    }

    private static void deleteDB(Context ctx) {
        // Delete Database
        Services.clear();
    }

    private static void login(Context ctx) {
        // Login
        Map body = new Gson().fromJson(ctx.body(), Map.class);

        if (body.get("username") == null || body.get("password") == null) {
            ctx.status(400);
            var result = Map.of("message", "Error: Please provide a username, password, or email");
            ctx.result(new Gson().toJson(result));
            return;
        }

        String username = body.get("username").toString();
        String password = body.get("password").toString();
        AuthData authData;

        try {
            authData = Services.login(username, password);
        } catch (Exception e) {
            ctx.status(401);
            var result = Map.of("message", e.getMessage());
            ctx.result(new Gson().toJson(result));
            return;
        }


        var result = Map.of("username", authData.getUsername(), "authToken", authData.getAuthToken());
        ctx.result(new Gson().toJson(result));
    }

    private static void logout(Context ctx) {
        // Logout
        String authToken = ctx.header("Authorization");

        try {
            Services.logout(authToken);
        } catch (Exception e) {
            return;
        }

        ctx.result("{ }");
    }

    private static void listGames(Context ctx) {
        // List Games
        String authToken = ctx.header("Authorization");
        ArrayList<GameData> gameList;

        try {
            gameList = Services.listGames(authToken);
        } catch (Exception e) {
            return;
        }

        var result = Map.of("games", gameList);
        ctx.result(new Gson().toJson(result));
    }

    private static void createGame(Context ctx) {
        // Create Game
        String authToken = ctx.header("Authorization");
        Map body = new Gson().fromJson(ctx.body(), Map.class);
        String gameName = body.get("gameName").toString();
        int gameID;

        try {
            gameID = Services.createGame(authToken, gameName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        var result = Map.of("gameID", gameID);
        ctx.result(new Gson().toJson(result));
    }

    private static void joinGame(Context ctx) {
        // Join Game
        String authToken = ctx.header("Authorization");
        Map body = new Gson().fromJson(ctx.body(), Map.class);
        String playerColor = body.get("playerColor").toString();
        int gameID = ((Number) body.get("gameID")).intValue();

        try {
            Services.joinGame(authToken, gameID, playerColor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ctx.result("{ }");
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
