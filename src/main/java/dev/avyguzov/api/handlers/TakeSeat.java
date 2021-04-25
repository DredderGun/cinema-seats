package dev.avyguzov.api.handlers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.avyguzov.api.config.HandlerPath;
import dev.avyguzov.api.handlers.model.Answer;
import dev.avyguzov.db.SeatDao;

import javax.inject.Inject;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@HandlerPath(method = "POST", value = "/take-seats")
public class TakeSeat extends AbstractRequestHandler<List<Integer>> {
    private static final Logger logger = LogManager.getLogger(TakeSeat.class);

    @Inject
    public TakeSeat(SeatDao seatDao, ObjectMapper objectMapper) {
        super(seatDao, objectMapper, new TypeReference<>() {});
    }

    @Override
    protected Answer processImpl(List<Integer> seatIds) throws Exception {
        logger.info("Request for a seat reservation with ids ={}", seatIds.toString());
        return new Answer(String.valueOf(seatDao.takeSeats(seatIds)));
    }

    @Override
    protected boolean isValid(List<Integer> value) {
        return value.size() != 0;
    }

}
