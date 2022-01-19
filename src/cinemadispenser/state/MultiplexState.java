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
     */
    public MultiplexState() {
        File moviesDir = new File("./src/resources/movies/txt/");
        File theatersDir = new File("./src/resources/theaters/");
        // Searches in File theaterFiles & creates new Theater(theaterFile, moviesFiles) inside theaterList
        for (File theaterFile: Objects.requireNonNull(theatersDir.listFiles())) {
            this.theaterList.add(new Theater(theaterFile, Objects.requireNonNull(moviesDir.listFiles())));
        }
    }

    /**
     * Gets List TheaterList
     * @return List theaterList
     */
    public List<Theater> getTheaterList() {
        return theaterList;
    }

}
