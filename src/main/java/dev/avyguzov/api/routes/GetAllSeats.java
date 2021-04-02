package dev.avyguzov.api.routes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.avyguzov.db.SeatDao;
import dev.avyguzov.model.Seat;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class GetAllSeats extends AbstractRequestHandler<EmptyPayload> {

    @Inject
    public GetAllSeats(SeatDao seatDao, ObjectMapper objectMapper) {
        super(seatDao, objectMapper, new TypeReference<>() {});
    }

    @Override
    protected Answer processImpl(EmptyPayload value) throws SQLException {
        return new Answer(dataToJson(seatDao.getAllSeats()));
    }

    @Override
    protected boolean isValid(EmptyPayload value) {
        return true;
    }
}
