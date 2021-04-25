package dev.avyguzov.api;

import dev.avyguzov.ConfigsReader;
import dev.avyguzov.Main;
import dev.avyguzov.api.client.CinemaClient;
import dev.avyguzov.api.client.JavaHttpCinemaClient;
import dev.avyguzov.model.Seat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class GetAllSeatsIntegrationTest {
    private final CinemaClient cinemaClient = new JavaHttpCinemaClient();
    private final ConfigsReader configsReader = new ConfigsReader("application-test.properties");
    private final String serverHost = configsReader.getProperty("server.host");

    public GetAllSeatsIntegrationTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        Main.main(new String[] {"test"});
    }

    @Test
    public void getAllSeatsPathGetData() throws Exception {
        List<Seat> seats = cinemaClient.getAllSeats(serverHost + "/get-all-seats");

        Assertions.assertEquals(seats.size(), 10);
    }
}
