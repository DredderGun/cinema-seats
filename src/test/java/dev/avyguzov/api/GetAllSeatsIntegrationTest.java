package dev.avyguzov.api;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.avyguzov.model.Seat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class GetAllSeatsIntegrationTest extends ApiAbstractTest {
    @Test
    public void getAllSeatsPathGetData() throws InterruptedException, IOException, URISyntaxException {
        var request = HttpRequest.newBuilder(new URI(serverHost + "/get-all-seats"))
                .GET()
                .build();
        HttpResponse<String> response = request(request);

        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertEquals(mapper.readValue(response.body(), new TypeReference<List<Seat>>() {}).size(), 10);
    }
}
