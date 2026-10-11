package model;

import java.util.UUID;

public class AuthData {
    String authToken;
    String username;

    public AuthData(String username) {
        this.username = username;
        authToken = generateToken();
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
