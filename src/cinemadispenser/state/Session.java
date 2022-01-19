package cinemadispenser.state;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Session class implements Serializable
 */
public class Session implements Serializable {

    /**
     * Session hour
     */
    private final LocalTime hour;
    /**
     * Session Seat status ArrayList
     */
    private final Set<Seat> occupiedSeatSet = new HashSet<>();

    /**
     * Session builder
     * @param hour LocalTime hour
     */
    public Session(LocalTime hour) {
        this.hour = hour;
    }

    /**
     * Checks if a specific Seat is occupied
     * @param row Seat int row
     * @param col Seat int column
     * @return boolean, if Seat is occupied true, else false
     */
    public boolean isOccupied(int row, int col) {
        return this.occupiedSeatSet.contains(new Seat(row, col));
    }

    /**
     * Occupies a seat adding Seat to ArrayList occupiedSeatArrayList
     * @param row Seat int row
     * @param col Seat int column
     */
    public void ocuppiesSeat(int row, int col) {
        this.occupiedSeatSet.add(new Seat(row, col));
    }

    /**
     * UnOccupies a seat removing Seat to ArrayList occupiedSeatArrayList
     * @param row Seat int row
     * @param col Seat int column
     */
    public void unocuppiesSeat(int row, int col) {
        this.occupiedSeatSet.remove(new Seat(row, col));
    }

    /**
     * Gets LocalTime hour
     * @return LocalTime hour
     */
    public LocalTime getHour() {
        return this.hour;
    }

}
