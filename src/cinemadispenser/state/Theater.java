package cinemadispenser.state;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.*;

/**
 * Theater class implements Serializable
 */
public class Theater implements Serializable {

    /**
     * Theater number
     */
    private int number;
    /**
     * Theater max rows
     */
    private int maxrows = 1;
    /**
     * Theater max columns
     */
    private int maxcols = 1;
    /**
     * Theater Seat Set
     */
    private final Set<Seat> seatSet = new HashSet<>();
    /**
     * Theater Film List
     */
    private final List<Film> filmList = new ArrayList<>();

    /**
     * Theater builder
     * @param theaterFile File theaterFile
     * @param moviesFiles File moviesFiles
     */
    public Theater(File theaterFile, File[] moviesFiles) {
        // Sets theater number, searching the corresponding one in the txt file
        for (char ch: theaterFile.toString().toCharArray()) {
            if (Character.isDigit(ch)) {
                this.number = Character.getNumericValue(ch);
            }
        }
        try {
            // Creates Seat Set with theaterFile directory, also maxrows & maxcols
            Scanner scTheater = new Scanner(new FileReader(theaterFile));
            int row = 1;
            int col = 1;
            while (scTheater.hasNextLine()) {
                String line = scTheater.nextLine();
                for (char ch: line.toCharArray()) {
                    if (ch == '*') {
                        this.seatSet.add(new Seat(row, col));
                    }
                    col += 1;
                }
                if (this.maxcols < col - 1) {
                    this.maxcols = col;
                }
                if (this.maxrows < row ) {
                    this.maxrows = row;
                }
                col = 1;
                row += 1;
            }
            // Creates corresponding films to Theater & adds them into filmList
            for (File filmFile: moviesFiles) {
                Scanner scFilm = new Scanner(new FileReader(filmFile));
                // while for getting Theatre Films
                while (scFilm.hasNextLine()) {
                    String line = scFilm.nextLine();
                    if (line.startsWith("Theatre: ")) {
                        for (char ch : line.toCharArray()) {
                            if (Character.isDigit(ch) && this.number == Character.getNumericValue(ch)) {
                                this.filmList.add(new Film(filmFile));
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets int number
     * @return int number
     */
    public int getNumber() {
        return this.number;
    }

    /**
     * Gets int maxrows
     * @return int maxrows
     */
    public int getMaxRows() {
        return this.maxrows;
    }

    /**
     * Gets max columns
     * @return int maxcols
     */
    public int getMaxCols() {
        return this.maxcols;
    }

    /**
     * Gets Set seatSet
     * @return Set seatSet
     */
    public Set<Seat> getSeatSet() {
        return this.seatSet;
    }

    /**
     * Gets List filmList
     * @return List filmList
     */
    public List<Film> getFilmList() {
        return this.filmList;
    }

}
