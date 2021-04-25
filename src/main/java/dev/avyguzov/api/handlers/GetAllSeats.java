package dev.avyguzov.api.handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.avyguzov.api.config.HandlerPath;
import dev.avyguzov.api.handlers.model.Answer;
import dev.avyguzov.api.handlers.model.EmptyPayload;
import dev.avyguzov.db.SeatDao;

import javax.inject.Inject;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@HandlerPath(method = "GET", value = "/get-all-seats")
public class GetAllSeats extends AbstractRequestHandler<EmptyPayload> {
    private static final Logger logger = LogManager.getLogger(GetAllSeats.class);

    @Inject
    public GetAllSeats(SeatDao seatDao, ObjectMapper objectMapper) {
        super(seatDao, objectMapper, new TypeReference<>() {});
    }

    @Override
    protected Answer processImpl(EmptyPayload value) throws Exception {
        logger.info("Request for all seats in a cinema");
        return new Answer(dataToJson(seatDao.getAllSeats()));
    }

    @Override
    protected boolean isValid(EmptyPayload value) {
        return true;
    }
}
