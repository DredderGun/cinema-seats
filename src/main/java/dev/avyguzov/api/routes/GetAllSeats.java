package dev.avyguzov.api.routes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.avyguzov.db.SeatDao;

import javax.inject.Inject;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class GetAllSeats extends AbstractRequestHandler<EmptyPayload> {
    private static final Logger logger = LogManager.getLogger(GetAllSeats.class);

    @Inject
    public GetAllSeats(SeatDao seatDao, ObjectMapper objectMapper) {
        super(seatDao, objectMapper, new TypeReference<>() {});
    }

    @Override
    protected Answer processImpl(EmptyPayload value) throws SQLException {
        logger.info("Request for all seats in a cinema");
        return new Answer(dataToJson(seatDao.getAllSeats()));
    }

    @Override
    protected boolean isValid(EmptyPayload value) {
        return true;
    }
}
