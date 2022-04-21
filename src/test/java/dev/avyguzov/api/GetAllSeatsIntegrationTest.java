package dev.avyguzov.api;

import dev.avyguzov.ConfigsReader;
import dev.avyguzov.Main;
import dev.avyguzov.api.client.CinemaClient;
import dev.avyguzov.api.client.JavaHttpCinemaClient;
import dev.avyguzov.db.DatabaseDdlOperations;
import dev.avyguzov.model.Seat;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

public class GetAllSeatsIntegrationTest {
    private final CinemaClient cinemaClient = new JavaHttpCinemaClient();
    private final ConfigsReader configsReader = new ConfigsReader("application-test.properties");
    private final String serverHost = configsReader.getProperty("server.host");

    public GetAllSeatsIntegrationTest() throws IOException, URISyntaxException {
    }

    @BeforeEach
    public void setUp() {
        Main.main(new String[] {"test"});
    }

    @Test
    public void getAllSeatsPathGetData() throws Exception {
        System.out.println("Start - getAllSeatsPathGetData");
        List<Seat> seats = cinemaClient.getAllSeats(serverHost + "/get-all-seats");

        Assertions.assertEquals(seats.size(), 10);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        if (Main.globalInjector != null) {
            DatabaseDdlOperations db = Main.globalInjector.getInstance(DatabaseDdlOperations.class);
            db.clearDb();
        }
    }
}
