package server;

import com.google.gson.Gson;
import dataaccess.LocalDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import service.Services;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Server {

    private final Javalin javalin;
    private final Services services = new Services(new LocalDataAccess());

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", this::deleteDB);
        javalin.post("/user", this::register);
        javalin.post("/session", this::login);
        javalin.delete("/session", this::logout);
        javalin.get("/game", this::listGames);
        javalin.post("/game", this::createGame);
        javalin.put("/game", this::joinGame);
    }

    private void throwError(int code, String message, Context ctx) {
        ctx.status(code);
        var result = Map.of("message", message);
        ctx.result(new Gson().toJson(result));
    }

    private void register(Context ctx) {
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
            authData = services.register(username, password, email);
        } catch (Exception e) {
            throwError(403, e.getMessage(), ctx);
            return;
        }

        var result = Map.of("username", username, "authToken", authData.getAuthToken());
        ctx.result(new Gson().toJson(result));
    }

    private void deleteDB(Context ctx) {
        // Delete Database
        services.clear();
    }

    private void login(Context ctx) {
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
            authData = services.login(username, password);
        } catch (Exception e) {
            throwError(401, e.getMessage(), ctx);
            return;
        }

        var result = Map.of("username", authData.getUsername(), "authToken", authData.getAuthToken());
        ctx.result(new Gson().toJson(result));
    }

    private void logout(Context ctx) {
        // Logout
        String authToken = ctx.header("Authorization");

        try {
            services.logout(authToken);
        } catch (Exception e) {
            throwError(401, e.getMessage(), ctx);
            return;
        }

        ctx.result("{ }");
    }

    private void listGames(Context ctx) {
        // List Games
        String authToken = ctx.header("Authorization");
        ArrayList<GameData> gameList;

        try {
            gameList = services.listGames(authToken);
        } catch (Exception e) {
            throwError(401, e.getMessage(), ctx);
            return;
        }

        var result = Map.of("games", gameList);
        ctx.result(new Gson().toJson(result));
    }

    private void createGame(Context ctx) {
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
            gameID = services.createGame(authToken, gameName);
        } catch (Exception e) {
            throwError(401, e.getMessage(), ctx);
            return;
        }

        var result = Map.of("gameID", gameID);
        ctx.result(new Gson().toJson(result));
    }

    private void joinGame(Context ctx) {
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
            services.joinGame(authToken, gameID, playerColor);
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
