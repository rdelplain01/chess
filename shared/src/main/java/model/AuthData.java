package model;

import java.util.Objects;
import java.util.UUID;

public class AuthData {
    String authToken;
    String username;

    public AuthData(String username) {
        this.username = username;
        authToken = generateToken();
    }

    public AuthData(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthData authData = (AuthData) o;
        return Objects.equals(authToken, authData.authToken) && Objects.equals(username, authData.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken, username);
    }
}
