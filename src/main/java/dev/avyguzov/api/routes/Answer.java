package dev.avyguzov.api.routes;

import static dev.avyguzov.api.routes.AbstractRequestHandler.OK;

public class Answer {
    private String payload = "";
    private Integer code = OK;

    public Answer(Integer code) {
        this.code = code;
    }

    public Answer(String payload) {
        this.payload = payload;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }
}
