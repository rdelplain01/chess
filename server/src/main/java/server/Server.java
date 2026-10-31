package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.LocalDataAccess;
import dataaccess.MySqlDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import org.jetbrains.annotations.NotNull;
import service.Services;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Server {

    private final Javalin javalin;
    private final Services services;

    public Server() {
        DataAccess dataAccess;
        try {
            dataAccess = new MySqlDataAccess();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        services = new Services(dataAccess);

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.delete("/db", this::deleteDB);
        javalin.post("/user", this::register);
        javalin.post("/session", this::login);
        javalin.delete("/session", this::logout);
        javalin.get("/game", this::listGames);
        javalin.post("/game", this::createGame);
        javalin.put("/game", this::joinGame);
        javalin.exception(DataAccessException.class, this::exceptionHandler);
    }

    private void exceptionHandler(DataAccessException ex, Context ctx) {
        throwError(ex.getStatusCode(), ex.getMessage(), ctx);
    }

    private void throwError(int code, String message, Context ctx) {
        ctx.status(code);
        var result = Map.of("message", message);
        ctx.result(new Gson().toJson(result));
    }

    private void register(Context ctx) throws DataAccessException {
        Map body = new Gson().fromJson(ctx.body(), Map.class);

        if (body.get("username") == null || body.get("password") == null || body.get("email") == null) {
            throwError(400, "Error: Please provide a username, password, or email", ctx);
            return;
        }

        String username = body.get("username").toString();
        String password = body.get("password").toString();
        String email = body.get("email").toString();
        AuthData authData;

        authData = services.register(username, password, email);

        var result = Map.of("username", username, "authToken", authData.getAuthToken());
        ctx.result(new Gson().toJson(result));
    }

    private void deleteDB(Context ctx) throws DataAccessException {
        // Delete Database
        services.clear();
    }

    private void login(Context ctx) throws DataAccessException {
        // Login
        Map body = new Gson().fromJson(ctx.body(), Map.class);

        if (body.get("username") == null || body.get("password") == null) {
            throwError(400, "Error: Please provide a username, password, or email", ctx);
            return;
        }

        String username = body.get("username").toString();
        String password = body.get("password").toString();
        AuthData authData;

        authData = services.login(username, password);

        var result = Map.of("username", authData.getUsername(), "authToken", authData.getAuthToken());
        ctx.result(new Gson().toJson(result));
    }

    private void logout(Context ctx) throws DataAccessException {
        // Logout
        String authToken = ctx.header("Authorization");

        services.logout(authToken);

        ctx.result("{ }");
    }

    private void listGames(Context ctx) throws DataAccessException {
        // List Games
        String authToken = ctx.header("Authorization");
        ArrayList<GameData> gameList;

        gameList = services.listGames(authToken);

        var result = Map.of("games", gameList);
        ctx.result(new Gson().toJson(result));
    }

    private void createGame(Context ctx) throws DataAccessException {
        // Create Game
        String authToken = ctx.header("Authorization");
        Map body = new Gson().fromJson(ctx.body(), Map.class);

        if (body.get("gameName") == null) {
            throwError(400, "Error: Game name can not be empty", ctx);
            return;
        }

        String gameName = body.get("gameName").toString();
        int gameID;

        gameID = services.createGame(authToken, gameName);

        var result = Map.of("gameID", gameID);
        ctx.result(new Gson().toJson(result));
    }

    private void joinGame(Context ctx) throws DataAccessException {
        // Join Game
        String authToken = ctx.header("Authorization");
        Map body = new Gson().fromJson(ctx.body(), Map.class);

        if (body.get("gameID") == null || body.get("playerColor") == null) {
            ctx.status(400);
            var result = Map.of("message", "Error: Game name can not be empty");
            ctx.result(new Gson().toJson(result));
            return;
        }

        String playerColor = body.get("playerColor").toString();
        int gameID = ((Number) body.get("gameID")).intValue();

        services.joinGame(authToken, gameID, playerColor);

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
