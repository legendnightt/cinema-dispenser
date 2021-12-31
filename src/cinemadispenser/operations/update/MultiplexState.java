package cinemadispenser.operations.update;

import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MultiplexState {

    private List<Theater> theaterList = new ArrayList<>();
    private List<Film> filmList = new ArrayList<>();

    public MultiplexState(CinemaTicketDispenser dispenser, UrjcBankServer bank) throws FileNotFoundException {

        File moviesDir = new File("./src/resources/movies/txt/");
        File theatersDir = new File("./src/resources/theaters/");

        // searches movies in path
        this.movieSearch(Objects.requireNonNull(moviesDir.listFiles()));
        // searches theatres in path
        this.theaterSearch(Objects.requireNonNull(theatersDir.listFiles()));

    }

    private void movieSearch(File[] files) throws FileNotFoundException {
        for (File file: files) {
            Film film = new Film(file);
            filmList.add(film);
        }
    }

    private void theaterSearch(File[] files) throws FileNotFoundException {
        for (File file: files) {
            Theater theater = new Theater(file);
            theaterList.add(theater);
        }
    }

}
