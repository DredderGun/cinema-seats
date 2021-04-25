package dev.avyguzov.db;

import com.google.inject.ImplementedBy;
import dev.avyguzov.model.Seat;

import java.util.List;

@ImplementedBy(JdbcSeatsDao.class)
public interface SeatDao {
    List<Seat> getAllSeats() throws Exception;
    boolean takeSeats(List<Integer> seat) throws Exception;
}
