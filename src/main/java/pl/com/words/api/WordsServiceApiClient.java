package pl.com.words.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.com.words.model.Word;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WordsServiceApiClient {

    public static void main(String[] args) {
        WordsServiceApiClient client = new WordsServiceApiClient();
        String response = client.get("cool");
        System.out.println(response);
    }

    public WordsServiceApiClient() {

    }


    public InputStream getPronunciation(String headword) {
        String apiUrl = "http://localhost:8080/pronunciation/" + headword;
        InputStream mp3 = null;
        try {
            mp3 = fetchPronunciationFromApi(apiUrl);
        } catch (Exception e) {
            System.out.println("Some exception in getPronunciation(headword)");
            e.printStackTrace();
        }
        return mp3;
    }

    private InputStream fetchPronunciationFromApi(String url)
    throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<InputStream> response =
                client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        return new BufferedInputStream(response.body());
    }


    public String get(String headword) {
        String apiUrl = "http://localhost:8080/word/" + headword;
        String jsonResponse = null;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Print the JSON response (just for testing)
                jsonResponse = response.toString();
                System.out.println("JSON Response: " + jsonResponse);
            } else {
                System.out.println("GET request failed. Response Code: " + responseCode);
            }
        } catch (IOException e) {
            System.out.println("Cannot connect to Words-Service");
            e.printStackTrace();
        }
        return jsonResponse;
    }

    public Word convertJsonToWordObject(String json) {
        Word w = null;
        try {
            ObjectMapper om = new ObjectMapper();
            w = om.readValue(json, Word.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return w;
    }


}
