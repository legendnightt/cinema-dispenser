package cinemadispenser.operations.update;

import java.util.Objects;

public class Seat {

    private boolean occupied = false;
    private final int row;
    private final int col;

    /**
     * Seat builder
     * @param row int row
     * @param col int column
     */
    public Seat(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Checks if provided Object has same seats & columns
     * @param o Object o
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return row == seat.row && col == seat.col;
    }

    /**
     * Gets hashCode
     * @return int hashCode
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    /**
     * Sets occupied attribute
     * @param occupied boolean
     */
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    /**
     * Gets boolean occupied
     * @return boolean occupied
     */
    public boolean getOccupied() {
        return occupied;
    }

    /**
     * Gets int row
     * @return int row
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets int column
     * @return int col
     */
    public int getCol() {
        return col;
    }

}
