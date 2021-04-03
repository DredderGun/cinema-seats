package dev.avyguzov.api.routes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import dev.avyguzov.db.SeatDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.IOException;
import java.io.StringWriter;

public abstract class AbstractRequestHandler<V> implements Route {
    private static final Logger logger = LogManager.getLogger(AbstractRequestHandler.class);

    protected static final int HTTP_BAD_REQUEST = 400;
    protected static final int OK = 200;

    protected final TypeReference<V> typeReference;
    protected final SeatDao seatDao;
    protected final ObjectMapper objectMapper;

    public AbstractRequestHandler(SeatDao seatDao, ObjectMapper objectMapper, TypeReference<V> typeReference) {
        this.objectMapper = objectMapper;
        this.seatDao = seatDao;
        this.typeReference = typeReference;
    }

    public String dataToJson(Object data) {
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            StringWriter sw = new StringWriter();
            objectMapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e){
            logger.error(e.getStackTrace());
            throw new RuntimeException("IOException from a StringWriter?");
        }
    }

    public final Answer process(V value) {
        if (!isValid(value)) {
            logger.info("Validation was failed for value: {}", value);
            Answer answer = new Answer(HTTP_BAD_REQUEST);
            answer.setPayload("Validation failed!");
            return answer;
        } else {
            try {
                return processImpl(value);
            } catch (Exception ex) {
                logger.error(ex.getStackTrace());
                Answer answer = new Answer(HTTP_BAD_REQUEST);
                answer.setPayload(ex.getMessage());
                ex.printStackTrace();
                return answer;
            }
        }
    }

    protected abstract Answer processImpl(V value) throws Exception;
    protected abstract boolean isValid(V value);

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String body = request.body();
        if (request.body() == null || request.body().isEmpty()) {
            body = "{\"payload\": \"\"}";
        }
        V value = objectMapper.readValue(body, typeReference);
        Answer answer = process(value);
        response.status(answer.getCode());
        response.type("application/json");
        response.body(answer.getPayload());
        return answer.getPayload();
    }

}