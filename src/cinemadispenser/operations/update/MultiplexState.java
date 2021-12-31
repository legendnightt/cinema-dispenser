package cinemadispenser.operations.update;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MultiplexState {

    private List<Theater> theaterList = new ArrayList<Theater>();

    public MultiplexState() throws FileNotFoundException {

        File theatersDir = new File("./src/resources/theaters/");
        File moviesDir = new File("./src/resources/movies/");

        // searches theatres in path
        fileSearch(Objects.requireNonNull(theatersDir.listFiles()));
        // searches movies in path
        //fileSearch(Objects.requireNonNull(moviesDir.listFiles()));

    }

    private void fileSearch(File[] files) throws FileNotFoundException {
        for (File file: files) {
            Theater theater = new Theater(file);
            theaterList.add(theater);
        }
    }

}
