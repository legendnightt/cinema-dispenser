package cinemadispenser.operations.update;

import java.util.Objects;

public class Seat {

    private final int row;
    private final int col;
    private boolean occupied = false;

    public Seat(int row, int col) {
        this.row = row;
        this.col = col;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return row == seat.row && col == seat.col;
    }

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

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean getOccupied() {
        return occupied;
    }

}
