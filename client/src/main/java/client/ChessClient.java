package client;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

public class ChessClient {
    // Create an HttpClient for making requests
    // This should be long-lived and shared, so a static final field is good here
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private String url;

    public ChessClient(String url) throws Exception {
        this.url = url;
    }

    public void run() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .timeout(java.time.Duration.ofMillis(5000))
                .GET()
                .build();
    }

    private void get(String host, int port, String path) throws Exception {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", host, port, path);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .GET()
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
            System.out.println(httpResponse.body());
        } else {
            System.out.println("Error: received status code " + httpResponse.statusCode());
        }
    }
}
