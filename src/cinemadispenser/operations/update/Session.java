package cinemadispenser.operations.update;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Set;

public class Session {

    private final LocalTime hour;
    ArrayList<Seat> occupiedSeatArrayList;

    /**
     * Session builder
     * @param hour LocalTime hour
     * @param seatSet Set<Seat> seatSet
     */
    public Session(LocalTime hour, Set<Seat> seatSet) {
        this.hour = hour;
        occupiedSeatArrayList = new ArrayList<>(seatSet);
    }

    /**
     * Checks if a specific Seat is occupied
     * @param row Seat int row
     * @param col Seat int column
     * @return boolean, if Seat is occupied true, else false
     */
    public boolean isOccupied(int row, int col) {
        // uses index of new Seat(row, col) Object, for getting Seat Object inside occupiedSeatArrayList & checking if it's occupied
        return occupiedSeatArrayList.get(occupiedSeatArrayList.indexOf(new Seat(row, col))).getOccupied();
    }

    /**
     * Occupies a seat setting occupied to true
     * @param row Seat int row
     * @param col Seat int column
     */
    public void ocuppiesSeat(int row, int col) {
        // uses index of new Seat(row, col) Object, for getting Seat Object inside occupiedSeatArrayList & sets occupied
        occupiedSeatArrayList.get(occupiedSeatArrayList.indexOf(new Seat(row, col))).setOccupied(true);
    }

    /**
     * UnOccupies a seat setting occupied to false
     * @param row Seat int row
     * @param col Seat int column
     */
    public void unocuppiesSeat(int row, int col) {
        // uses index of new Seat(row, col) Object, for getting Seat Object inside occupiedSeatArrayList & sets unoccupied
        occupiedSeatArrayList.get(occupiedSeatArrayList.indexOf(new Seat(row, col))).setOccupied(false);
    }

    /**
     * Gets LocalTime hour
     * @return LocalTime hour
     */
    public LocalTime getHour() {
        return hour;
    }

}
