package dev.avyguzov.api;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TakeSeatIntegrationTest extends ApiAbstractTest {

    @Test
    public void successfullyTakeAllSpecifiedSeats() throws URISyntaxException, IOException, InterruptedException {
        var jsonPayload = "[2, 3, 4]";
        var request = HttpRequest.newBuilder(new URI(serverHost + "/take-seats"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        var resp = request(request);
        Assertions.assertEquals(resp.statusCode(), 200);
        Assertions.assertTrue(mapper.readValue(resp.body(), new TypeReference<Boolean>() {}));
    }

    @Test
    public void dontAllowToTakeSeatMultipleTimes() throws URISyntaxException, IOException, InterruptedException {
        var jsonPayload = "[2, 3, 4]";
        var request = HttpRequest.newBuilder(new URI(serverHost + "/take-seats"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        var response = request(request);

        jsonPayload = "[3, 10, 5]";
        request = HttpRequest.newBuilder(new URI(serverHost + "/take-seats"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        Assertions.assertEquals(response.statusCode(), 200);
        Assertions.assertFalse(mapper.readValue(response.body(), new TypeReference<Boolean>() {}));
    }

    @Test
    public void oneOfRequestShouldReceiveFalse() throws URISyntaxException, IOException, InterruptedException, ExecutionException {
        var jsonPayload = "[3]";
        var request = HttpRequest.newBuilder(new URI(serverHost + "/take-seats"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        var secondJsonPayload = "[3, 10, 5]";
        var secondRequest = HttpRequest.newBuilder(new URI(serverHost + "/take-seats"))
                .POST(HttpRequest.BodyPublishers.ofString(secondJsonPayload))
                .build();

        ConcurrentLinkedDeque<Boolean> deque = new ConcurrentLinkedDeque<>();

        asyncRequest(request).thenApply(answer -> {
            deque.add(Boolean.valueOf(answer.body()));
            return answer;
        });
        asyncRequest(secondRequest).thenApply(answer -> {
            deque.add(Boolean.valueOf(answer.body()));
            return answer;
        });

        // todo test for callback response

    }

}
