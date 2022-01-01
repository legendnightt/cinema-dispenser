package cinemadispenser.operations.update;

import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MultiplexState {

    private final List<Theater> theaterList = new ArrayList<>();
    private final List<Film> filmList = new ArrayList<>();

    public MultiplexState(CinemaTicketDispenser dispenser, UrjcBankServer bank) throws FileNotFoundException, ParseException {
        File moviesDir = new File("./src/resources/movies/txt/");
        File theatersDir = new File("./src/resources/theaters/");
        // searches movies in path
        movieSearch(Objects.requireNonNull(moviesDir.listFiles()));
        // searches theatres in path
        theaterSearch(Objects.requireNonNull(theatersDir.listFiles()));
    }

    private void movieSearch(File[] files) throws FileNotFoundException, ParseException {
        for (File file: files) {
            filmList.add(new Film(file));
        }
    }

    private void theaterSearch(File[] files) throws FileNotFoundException {
        for (File file: files) {
            theaterList.add(new Theater(file));
        }
    }

}
