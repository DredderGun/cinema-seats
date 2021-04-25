package dev.avyguzov.api.client;

import dev.avyguzov.model.Seat;

import java.util.List;

public interface CinemaClient {
    List<Seat> getAllSeats(String url) throws Exception;
    boolean takeSeats(String url, List<Integer> seatsIds) throws Exception;
}
