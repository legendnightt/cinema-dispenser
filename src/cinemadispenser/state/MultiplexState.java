package cinemadispenser.state;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MultiplexState class implements Serializable
 */
public class MultiplexState implements Serializable {

    /**
     * Theater List
     */
    private final List<Theater> theaterList = new ArrayList<>();

    /**
     * MultiplexState builder
     * @throws FileNotFoundException  if theaterFile or moviesFiles doesn't exist
     */
    public MultiplexState() throws FileNotFoundException {
        File moviesDir = new File("./src/resources/movies/txt/");
        File theatersDir = new File("./src/resources/theaters/");
        // searches theatres in path & adds movie list path
        theaterSearch(Objects.requireNonNull(theatersDir.listFiles()), Objects.requireNonNull(moviesDir.listFiles()));
    }

    /**
     * Searches in File theaterFiles & creates new Theater(theaterFile, moviesFiles) inside theaterList
     * @param theaterFiles File theaterFiles
     * @param moviesFiles File moviesFiles
     * @throws FileNotFoundException if theaterFile or moviesFiles doesn't exist
     */
    private void theaterSearch(File[] theaterFiles, File[] moviesFiles) throws FileNotFoundException {
        for (File theaterFile: theaterFiles) {
            theaterList.add(new Theater(theaterFile, moviesFiles));
        }
    }

    /**
     * Gets provided int number Theater
     * @param number int number
     * @return Theater theater
     */
    public Theater getTheater(int number) {
        for (Theater theater: theaterList) {
            if (number == theater.getNumber()) {
                return theater;
            }
        }
        throw new RuntimeException("Theater number " + number + " not found");
    }

    /**
     * Gets number of Theaters
     * @return int size theaterList
     */
    public int getNumberOfTheaters() {
        return theaterList.size();
    }

    /**
     * Gets List TheaterList
     * @return List theaterList
     */
    public List<Theater> getTheaterList() {
        return theaterList;
    }

}
