package server;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.Map;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthData register(UserData user) {
        // Register
        var request = buildRequest("POST", "/user", user);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public Map deleteDB() {
        // Delete Database
        var request = buildRequest("DELETE", "/db", null);
        var response = sendRequest(request);
        return handleResponse(response, Map.class);
    }

    public AuthData login(Map<String, String> loginRequest) {
        // Login
        var request = buildRequest("POST", "/session", loginRequest);
        var response = sendRequest(request);
        return handleResponse(response, AuthData.class);
    }

    public Map logout(AuthData authData) {
        // Logout
        var request = buildRequestAuth("DELETE", "/session", authData, null);
        var response = sendRequest(request);
        return handleResponse(response, Map.class);
    }

    public GameData[] listGames(AuthData authData) {
        // List Games
        var request = buildRequestAuth("GET", "/game", authData, null);
        var response = sendRequest(request);
        JsonObject body = handleResponse(response, JsonObject.class);
        return new Gson().fromJson(body.get("games"), GameData[].class);
    }

    public int createGame(AuthData authData, String gameName) {
        // Create Game
        var request = buildRequestAuth("POST", "/game", authData, Map.of("gameName", gameName));
        var response = sendRequest(request);
        JsonObject body = handleResponse(response, JsonObject.class);
        return body.get("gameID").getAsInt();
    }

    public ChessGame joinGame(AuthData authData, String playerColor, int gameID) {
        // Join Game
        var request = buildRequestAuth("PUT", "/game", authData, Map.of(
                "playerColor", playerColor,
                "gameID", gameID
        ));
        var response = sendRequest(request);
        return handleResponse(response, ChessGame.class);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest buildRequestAuth(String method, String path, AuthData authData, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .header("Authorization", authData.getAuthToken())
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest.BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return HttpRequest.BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return HttpRequest.BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body, ResponseException.fromHttpStatusCode(status));
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
