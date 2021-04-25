package dev.avyguzov.api.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.avyguzov.model.Seat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class JavaHttpCinemaClient implements CinemaClient {
    private final HttpClient client = HttpClient.newHttpClient();
    protected final ObjectMapper mapper = new ObjectMapper();

    public List<Seat> getAllSeats(String url) throws Exception {
        var request = HttpRequest.newBuilder(new URI(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), new TypeReference<List<Seat>>() {});
    }

    @Override
    public boolean takeSeats(String url, List<Integer> seatsIds) throws Exception {
        var request = HttpRequest.newBuilder(new URI(url))
                .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(seatsIds)))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(), Boolean.TYPE);
    }
}
