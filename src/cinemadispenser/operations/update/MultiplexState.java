package cinemadispenser.operations.update;

import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MultiplexState {

    private final List<Theater> theaterList = new ArrayList<>();
    private final List<Film> filmList = new ArrayList<>();

    public MultiplexState(CinemaTicketDispenser dispenser, UrjcBankServer bank) throws FileNotFoundException {
        File moviesDir = new File("./src/resources/movies/txt/");
        File theatersDir = new File("./src/resources/theaters/");
        // searches theatres in path & adds movie list path
        theaterSearch(Objects.requireNonNull(theatersDir.listFiles()), Objects.requireNonNull(moviesDir.listFiles()));
    }

    private void theaterSearch(File[] theaterFiles, File[] moviesFiles) throws FileNotFoundException {
        for (File theaterFile: theaterFiles) {
            theaterList.add(new Theater(theaterFile, moviesFiles));
        }
    }

}
