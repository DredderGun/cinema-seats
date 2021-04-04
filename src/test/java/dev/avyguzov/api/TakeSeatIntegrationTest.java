package dev.avyguzov.api;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

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
    void a() throws InterruptedException {
        //test plan:
        //create all requests
        //create all callable
        //List<Features> features = executor.invokeAll(callables)
        //iterate over all features and call get()
        //analyze the results

        List<HttpRequest> requests = new ArrayList<>();

        var crossSeat = "3";
        final var random = new Random();
        for (int i = 0; i < 1000; i++) {
            final var nextValue = random.nextInt(100);
            final var request = HttpRequest.newBuilder(new URI(serverHost + "/take-seats"))
                    .POST(HttpRequest.BodyPublishers.ofString("[" + crossSeat + "," + nextValue + "]"))
                    .build();

            requests.add(request);
        }

        final var executors = Executors.newCachedThreadPool();

        List<Future<HttpResponse>> results = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            final HttpRequest request = requests.get(i);
            final Callable<HttpResponse> httpResponseCallable = () -> {
                final HttpResponse<String> response1 = request(request);
                return response1;
            };
            final Future<HttpResponse> response = executors.submit(httpResponseCallable);

            results.add(response);
        }
        //
        executors.invokeAll()
        results.forEach(r -> r.get());

    }

    @Test
    public void dontAllowTakeAlreadyOccupiedSeats() throws URISyntaxException, InterruptedException {
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

        // todo how to check async answer from server correctly??
        Thread.sleep(1000);
        Assertions.assertEquals(deque.stream().filter((el) -> el.equals(true)).count(), 1);
        Assertions.assertEquals(deque.stream().filter((el) -> el.equals(false)).count(), 1);
    }

}
