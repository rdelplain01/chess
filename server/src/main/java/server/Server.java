package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.GameData;
import model.UserData;
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
        String username = body.get("username").toString();
        String password = body.get("password").toString();
        String email = body.get("email").toString();

        // Maybe should be in services

        UserData userData = new UserData(username, password, email);

        if (Services.getUser(username) != null) {
            // Throw exception
        }
        Services.createUser(userData);

        AuthData authData = new AuthData(username);

        Services.createAuth(authData);

        //---

        var result = Map.of("username", username, "authToken", authData.getAuthToken());
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
        Map body = new Gson().fromJson(ctx.body(), Map.class);
        String username = body.get("username").toString();
        String password = body.get("password").toString();

        UserData user = Services.getUser(username);

        if (user == null) {
            // throw exception
        }

        if (!Services.verifyPassword(password, user)) {
            // Throw Exception
        }

        AuthData authData = new AuthData(user.getUsername());

        Services.createAuth(authData);

        var result = Map.of("username", user.getUsername(), "authToken", authData.getAuthToken());
        ctx.result(new Gson().toJson(result));
    }

    private static void logout(Context ctx) {
        // Logout
        String authToken = ctx.header("Authorization");

        AuthData authData = Services.isAuth(authToken);
        if (authData == null) {
            // Throw Exception
        }
        Services.deleteAuth(authData);
        ctx.result("{ }");
    }

    private static void listGames(Context ctx) {
        // List Games
        String authToken = ctx.header("Authorization");

        AuthData authData = Services.isAuth(authToken);
        if (authData == null) {
            // Throw Exception
        }
        ArrayList<GameData> gameList = Services.listGames();

        var result = Map.of("games", gameList);
        ctx.result(new Gson().toJson(result));
    }

    private static void createGame(Context ctx) {
        // Create Game
        String authToken = ctx.header("Authorization");
        Map body = new Gson().fromJson(ctx.body(), Map.class);
        String gameName = body.get("gameName").toString();

        AuthData authData = Services.isAuth(authToken);
        if (authData == null) {
            // Throw Exception
        }
        int gameID = Services.createGame(gameName);

        var result = Map.of("gameID", gameID);
        ctx.result(new Gson().toJson(result));
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
