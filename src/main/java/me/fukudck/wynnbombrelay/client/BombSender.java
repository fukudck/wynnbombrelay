package me.fukudck.wynnbombrelay.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BombSender {

    // HttpClient dùng chung, tối ưu
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    /**
     *
     * @param bombType  BOMB_TYPE
     * @param thrower   Người ném bomb
     * @param server    Tên server
     * @param durationMs Thời gian bomb (ms)
     */
    public static void send(String bombType, String thrower, String server, long durationMs) {
        try {
            URI apiUri = new URI("https://bombrelay-vercel.vercel.app/api/bombs");

            String jsonBody = String.format(
                    "{\"BOMB_TYPE\":\"%s\",\"THROWER\":\"%s\",\"SERVER\":\"%s\",\"DURATION\":%d}",
                    bombType, thrower, server, durationMs
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(apiUri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> System.out.println("[BombSender] Response: " + response.statusCode()))
                    .exceptionally(e -> {
                        e.printStackTrace();
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
