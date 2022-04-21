package dev.avyguzov.api;

import dev.avyguzov.ConfigsReader;
import dev.avyguzov.Main;
import dev.avyguzov.api.client.CinemaClient;
import dev.avyguzov.api.client.JavaHttpCinemaClient;
import dev.avyguzov.db.DatabaseDdlOperations;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class TakeSeatIntegrationTest {
    private final ConfigsReader configsReader = new ConfigsReader("application-test.properties");
    private final String serverHost = configsReader.getProperty("server.host");
    private final CinemaClient cinemaClient = new JavaHttpCinemaClient();

    public TakeSeatIntegrationTest() throws IOException, URISyntaxException {
    }

    @BeforeAll
    public static void setUp() {
        Main.main(new String[] {"test"});
    }

    @Test
    public void dontAllowTakeAlreadyOccupiedSeats() throws InterruptedException, ExecutionException {
        System.out.println("Start - dontAllowTakeAlreadyOccupiedSeats");

        final var parallelRequestsCount = 1000;
        final List<Callable<Boolean>> requests = new ArrayList<>();
        final var executorService = Executors.newCachedThreadPool();

        final var random = new Random();
        var crossSeat = 3;
        for (int i = 0; i < parallelRequestsCount; i++) {
            Callable<Boolean> callable = () -> cinemaClient.takeSeats(
                    serverHost + "/take-seats",
                    Arrays.asList(crossSeat, random.nextInt(9) + 1));

            requests.add(callable);
        }

        var results = executorService.invokeAll(requests);
        Assertions.assertEquals(parallelRequestsCount, results.stream().filter(Future::isDone).count());

        var successRequestsCount = 0;
        for (Future<Boolean> currResult : results) {
            if (currResult.get()) {
                successRequestsCount++;
            }
        }
        Assertions.assertEquals(1, successRequestsCount);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (Main.globalInjector != null) {
            DatabaseDdlOperations db = Main.globalInjector.getInstance(DatabaseDdlOperations.class);
            db.clearDb();
        }
    }

}
