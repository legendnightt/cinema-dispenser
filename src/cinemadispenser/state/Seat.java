package cinemadispenser.state;

import java.io.Serializable;
import java.util.Objects;

/**
 * Seat class implements Serializable
 */
public class Seat implements Serializable {

    /**
     * Seat row
     */
    private final int row;
    /**
     * Seat column
     */
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
     * Checks if provided Object has same seats and columns
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
