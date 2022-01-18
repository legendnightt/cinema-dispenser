package cinemadispenser.state;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private final ArrayList<Seat> occupiedSeatArrayList;

    /**
     * Session builder
     * @param hour LocalTime hour
     * @param seatSet Set seatSet
     */
    public Session(LocalTime hour, Set<Seat> seatSet) {
        this.hour = hour;
        this.occupiedSeatArrayList = new ArrayList<>();
        this.occupiedSeatArrayList.addAll(seatSet);
    }

    /**
     * Checks if a specific Seat is contained in occupiedSeatArrayList
     * @param row Seat int row
     * @param col Seat int column
     * @return boolean, if Seat exits true, else false
     */
    public boolean isContained(int row, int col) {
        return this.occupiedSeatArrayList.contains(new Seat(row, col));
    }

    /**
     * Checks if a specific Seat is occupied
     * @param row Seat int row
     * @param col Seat int column
     * @return boolean, if Seat is occupied true, else false
     */
    public boolean isOccupied(int row, int col) {
        // uses index of new Seat(row, col) Object, for getting Seat Object inside occupiedSeatArrayList & checking if it's occupied
        return this.occupiedSeatArrayList.get(this.occupiedSeatArrayList.indexOf(new Seat(row, col))).getOccupied();
    }

    /**
     * Occupies a seat setting occupied to true
     * @param row Seat int row
     * @param col Seat int column
     */
    public void ocuppiesSeat(int row, int col) {
        // uses index of new Seat(row, col) Object, for getting Seat Object inside occupiedSeatArrayList & sets occupied
        this.occupiedSeatArrayList.get(this.occupiedSeatArrayList.indexOf(new Seat(row, col))).setOccupied(true);
    }

    /**
     * UnOccupies a seat setting occupied to false
     * @param row Seat int row
     * @param col Seat int column
     */
    public void unocuppiesSeat(int row, int col) {
        // uses index of new Seat(row, col) Object, for getting Seat Object inside occupiedSeatArrayList & sets unoccupied
        this.occupiedSeatArrayList.get(this.occupiedSeatArrayList.indexOf(new Seat(row, col))).setOccupied(false);
    }

    /**
     * Gets LocalTime hour
     * @return LocalTime hour
     */
    public LocalTime getHour() {
        return this.hour;
    }

}
