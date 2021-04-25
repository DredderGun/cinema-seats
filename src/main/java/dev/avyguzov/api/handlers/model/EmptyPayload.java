package dev.avyguzov.api.handlers.model;

public class EmptyPayload {
    public String getPayload() {
        return payload;
    }

    public void setPayload(String data) {
        this.payload = data;
    }

    private String payload = "";
}
