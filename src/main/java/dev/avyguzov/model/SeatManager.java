package dev.avyguzov.model;

import java.util.List;

public class SeatManager {
    private final List<Seat> seats;

    public SeatManager(List<Seat> seats) {
        this.seats = seats;
    }

    private boolean isBooked(Integer seatId) {
        final Seat seat = seats.get(seatId);
        synchronized (seat) {
            return seat.isOccupied();
        }
    }


    public void bookSeat(Integer seatId) {
        if (isBooked(seatId)) {
            return;
        }

        final Seat seat = seats.get(seatId);
        synchronized (seat) {
            seat.setOccupied(true);
        }
    }
}
