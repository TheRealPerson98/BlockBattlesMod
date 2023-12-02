package com.person98.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class ApiFetcher {

    public static CompletableFuture<String> fetchPlayerStats(String apiKey, String playerName) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL("https://api.blockbattles.org/" + apiKey + "/blockbattles/stats/" + playerName);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        content.append(inputLine);
                    }
                    return content.toString();
                } finally {
                    con.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}
