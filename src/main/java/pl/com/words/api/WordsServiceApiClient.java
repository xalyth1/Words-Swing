package pl.com.words.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.com.words.model.Word;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


public class WordsServiceApiClient {

    public static void main(String[] args) {
        WordsServiceApiClient client = new WordsServiceApiClient();
        String response = client.getDefinitions("delve");
        System.out.println(client.prepareExtendedWordDefinitions(response));
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


    /**
     * definition of word containing full definitions (all)
     * @return  formattedMultipleDefinitions
     */
    public String prepareExtendedWordDefinitions(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        StringBuilder formattedMultipleDefinitions = new StringBuilder();
        try {
            // Parse JSON string to User object
            List<String> list = objectMapper.readValue(json, new TypeReference<ArrayList<String>>() {});
            list.stream().forEach(definition -> formattedMultipleDefinitions.append(definition).append("\n"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedMultipleDefinitions.toString();
    }

    /**
     * Only one first definition for simplicity of learning
     */
    public String prepareSimplifiedDefinition(String json) {
        return "";
    }



    public String getDefinitions(String headword) {
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
        /**
         *
         * todo test and rethink
         */
        try {
            ObjectMapper om = new ObjectMapper();
            w = om.readValue(json, Word.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return w;
    }


}
