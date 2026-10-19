package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import services.Services;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

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

    private static void throwError(int code, String message, Context ctx) {
        ctx.status(code);
        var result = Map.of("message", message);
        ctx.result(new Gson().toJson(result));
    }

    private static void register(Context ctx) {
        Map body = new Gson().fromJson(ctx.body(), Map.class);

        if (body.get("username") == null || body.get("password") == null || body.get("email") == null) {
            throwError(400, "Error: Please provide a username, password, or email", ctx);
            return;
        }

        String username = body.get("username").toString();
        String password = body.get("password").toString();
        String email = body.get("email").toString();
        AuthData authData;

        try {
            authData = Services.register(username, password, email);
        } catch (Exception e) {
            throwError(403, e.getMessage(), ctx);
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
            throwError(400, "Error: Please provide a username, password, or email", ctx);
            return;
        }

        String username = body.get("username").toString();
        String password = body.get("password").toString();
        AuthData authData;

        try {
            authData = Services.login(username, password);
        } catch (Exception e) {
            throwError(401, e.getMessage(), ctx);
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
            throwError(401, e.getMessage(), ctx);
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
            throwError(401, e.getMessage(), ctx);
            return;
        }

        var result = Map.of("games", gameList);
        ctx.result(new Gson().toJson(result));
    }

    private static void createGame(Context ctx) {
        // Create Game
        String authToken = ctx.header("Authorization");
        Map body = new Gson().fromJson(ctx.body(), Map.class);

        if (body.get("gameName") == null) {
            throwError(400, "Error: Game name can not be empty", ctx);
            return;
        }

        String gameName = body.get("gameName").toString();
        int gameID;

        try {
            gameID = Services.createGame(authToken, gameName);
        } catch (Exception e) {
            throwError(401, e.getMessage(), ctx);
            return;
        }

        var result = Map.of("gameID", gameID);
        ctx.result(new Gson().toJson(result));
    }

    private static void joinGame(Context ctx) {
        // Join Game
        String authToken = ctx.header("Authorization");
        Map body = new Gson().fromJson(ctx.body(), Map.class);

        if (body.get("gameID") == null || body.get("playerColor") == null ) {
            ctx.status(400);
            var result = Map.of("message", "Error: Game name can not be empty");
            ctx.result(new Gson().toJson(result));
            return;
        }

        String playerColor = body.get("playerColor").toString();
        int gameID = ((Number) body.get("gameID")).intValue();

        try {
            Services.joinGame(authToken, gameID, playerColor);
        } catch (Exception e) {
            if (Objects.equals(e.getMessage(), "Error: Player color does not exist")) {
                throwError(400, e.getMessage(), ctx);
                return;
            }
            if (Objects.equals(e.getMessage(), "Error: game taken")) {
                throwError(403, e.getMessage(), ctx);
                return;
            }
            throwError(401, e.getMessage(), ctx);
            return;
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
