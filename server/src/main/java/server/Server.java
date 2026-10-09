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

        javalin.delete("/db", context -> {});
        javalin.post("/user", Server::register);
    }


    private static void register(Context ctx) {
        System.out.println(ctx.body());
        Services.getUser("");
        Services.createUser("");
        Services.createAuth("");
        var result = Map.of("username", "", "authToken", "");
        ctx.result(new Gson().toJson(result));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
