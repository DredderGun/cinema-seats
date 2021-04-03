package dev.avyguzov.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.avyguzov.Main;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

import static spark.Spark.stop;

public class ApiAbstractTest {
    protected String serverHost = "http://localhost:4567";
    protected ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setUp() throws Exception {
        Main.main(new String[] {"test"});
    }

    @AfterAll
    public static void tearDown() {
        stop();
    }

    protected HttpResponse<String> request(HttpRequest request) throws IOException, InterruptedException {
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected CompletableFuture<HttpResponse<String>> asyncRequest(HttpRequest request) {
        return HttpClient.newHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString());
    }
}
