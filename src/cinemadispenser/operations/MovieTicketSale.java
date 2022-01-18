package cinemadispenser.operations;

import cinemadispenser.Multiplex;
import cinemadispenser.Operation;
import cinemadispenser.state.*;
import sienens.CinemaTicketDispenser;

import java.time.temporal.ChronoUnit;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

/**
 * MovieTicketSale class extends Operation
 */
public class MovieTicketSale extends Operation {

    /**
     * MultiplexState state
     */
    private MultiplexState state;

    /**
     * MovieTicketSale builder
     * @param dispenser CinemaTicketDispenser dispenser
     * @param multiplex Multiplex multiplex
     * @throws IOException IO exception
     * @throws ClassNotFoundException Class not found
     */
    public MovieTicketSale(CinemaTicketDispenser dispenser, Multiplex multiplex) throws IOException, ClassNotFoundException {
        super(dispenser, multiplex);
        String serializableDirectory = "./src/resources/serializable";
        String serializablePath = serializableDirectory + "/state.bin";
        File directory = new File(serializableDirectory);
        // creates /serializable directory if it doesn't exist
        if (!directory.exists()) {
            if (directory.mkdir()) {
                System.out.println("No state directory so, created successfully!");
            }
        }
        File file = new File(serializablePath);
        if (file.exists()) {
            LocalDateTime now = LocalDateTime.now();
            // state.bin FileTime
            BasicFileAttributes attr = Files.readAttributes(Path.of(serializablePath), BasicFileAttributes.class);
            // converts FileTime into LocalDateTime
            LocalDateTime convertedFileTime = LocalDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());
            // if state.bin exists & is not created in the same day, new MultiplexState & it creates again
            if (ChronoUnit.DAYS.between(convertedFileTime, now) != 0) {
                this.generateState(serializablePath);
                System.out.println("Old State so, new state generated and serialized successfully!");
            } else {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(serializablePath));
                this.state = (MultiplexState) in.readObject();
                System.out.println("New state created from state.bin successfully!");
            }
        } else {
            this.generateState(serializablePath);
            System.out.println("No state.bin file so, New state generated and serialized successfully!");
        }
    }

    /**
     * Generates state & state.bin
     * @param serializablePath String serializablePath
     * @throws IOException if something fails inside
     */
    private void generateState(String serializablePath) throws IOException {
        this.state = new MultiplexState();
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(serializablePath));
        out.writeObject(this.state);
        out.flush();
        out.close();
    }

    /**
     * Does the operation needed in this case
     */
    @Override
    public void doOperation() {
        super.getDispenser().setMenuMode();
        int seconds = 5;
        // more accessible Films
        ArrayList<Film> totalFilmArrayList = new ArrayList<>();
        for (Theater theater : this.state.getTheaterList()) {
            totalFilmArrayList.addAll(theater.getFilmList());
        }
        this.displayFilmButtons();
        // displays first film
        this.displayFilmInfo(0, totalFilmArrayList);
        int filmCont = 0;
        boolean gobackFilm = false;
        char option = super.getDispenser().waitEvent(seconds);
        // if time is over, goes back to main loop & MainMenu
        while ((int) option != 0) {
            System.out.println("FilmCont: " + filmCont);
            switch (option) {
                // Next Film
                case 'C':
                    if (filmCont < totalFilmArrayList.size() - 1) {
                        filmCont += 1;
                        this.displayFilmInfo(filmCont, totalFilmArrayList);
                    }
                    break;
                // Previous Film
                case 'D':
                    if (filmCont > 0) {
                        filmCont -= 1;
                        this.displayFilmInfo(filmCont, totalFilmArrayList);
                    }
                    break;
                // Continue to Session select
                case 'E':
                    // continue so, enters the selected movie to select Session
                    Film selectedFilm  = totalFilmArrayList.get(filmCont);
                    Theater selectedTheater = null;
                    for (Theater theater : this.state.getTheaterList()) {
                        if (theater.getFilmList().contains(selectedFilm)) {
                            selectedTheater = theater;
                        }
                    }
                    assert selectedTheater != null;
                    this.displaySessionButtons(selectedTheater, selectedFilm);
                    int sessionCont = -1;
                    boolean gobackSession = false;
                    option = super.getDispenser().waitEvent(seconds);
                    // do while ig
                    while ((int) option != 0 && !gobackFilm) {
                        switch (option) {
                            // Session 1
                            case 'A':
                                if (0 < selectedTheater.getSessionList().size()) {
                                    sessionCont = 0;
                                }
                                break;
                            // Session 2
                            case 'B':
                                if (1 < selectedTheater.getSessionList().size()) {
                                    sessionCont = 1;
                                }
                                break;
                            // Session 3
                            case 'C':
                                if (2 < selectedTheater.getSessionList().size()) {
                                    sessionCont = 2;
                                }
                                break;
                            // Session 4
                            case 'D':
                                if (3 < selectedTheater.getSessionList().size()) {
                                    sessionCont = 3;
                                }
                                break;
                            // Go Back
                            case 'E':
                                gobackFilm = true;
                                break;
                            // Exit
                            case 'F':
                                // exit so, goes back to main loop & MainMenu
                                option = '\u0000'; // default char value
                                break;
                        }
                        // continues if session was selected
                        if (sessionCont != -1) {
                            System.out.println("Session selected: " + sessionCont);
                            Session selectedSession = selectedTheater.getSessionList().get(sessionCont);
                            this.displaySeats(selectedTheater, selectedSession);
                            option = super.getDispenser().waitEvent(seconds);
                            int maxSeats = 3;
                            int contSeats = 0;
                            while ((int) option != 0 && !gobackSession) {
                                // Go Back
                                if (option == 'A') {
                                    gobackSession = true;
                                }
                                // Exit
                                else if (option == 'B') {
                                    // exit so, goes back to main loop & MainMenu
                                    option = '\u0000'; // default char value
                                }
                                // Seat Selected
                                else {
                                    byte row = (byte)((option & 0xFF00) >> 8);
                                    byte col = (byte)(option & 0xFF);
                                    // if less than this.maxSeats selected & not occupied
                                    if (!selectedSession.isOccupied(row, col)) {
                                        /**
                                         * max Seats can be bought 1 time
                                         */
                                        if (maxSeats > contSeats) {
                                            selectedSession.ocuppiesSeat(row, col);
                                            contSeats++;
                                        }
                                    } else {
                                        selectedSession.unocuppiesSeat(row, col);
                                        contSeats--;
                                    }
                                    this.displaySeats(selectedTheater, selectedSession);
                                    option = super.getDispenser().waitEvent(seconds);
                                }
                            }
                        }
                        if ((int) option != 0) {
                            if (gobackSession) {
                                super.getDispenser().setMenuMode();
                                this.displaySessionButtons(selectedTheater, selectedFilm);
                                option = super.getDispenser().waitEvent(seconds);
                            }
                        }
                        sessionCont = -1;
                    }
                    break;
                // Exit
                case 'F':
                    // exit so, goes back to main loop & MainMenu
                    option = '\u0000'; // default char value
                    break;
            }
            // exit if case 'F':
            if ((int) option != 0) {
                // if go back to Film selector from Session selector
                if (gobackFilm) {
                    this.displayFilmButtons();
                    this.displayFilmInfo(filmCont, totalFilmArrayList);
                    gobackFilm = false;
                }
                option = super.getDispenser().waitEvent(seconds);
            }
        }
        System.out.println("EXIT");
    }

    /**
     * Displays Film options buttons
     */
    private void displayFilmButtons() {
        super.getDispenser().setOption(2, "Next Film");
        super.getDispenser().setOption(3, "Previous Film");
        super.getDispenser().setOption(4, "Continue");
        super.getDispenser().setOption(5, "Exit");
    }

    /**
     * Displays Film info
     * @param filmNumber int filmNumber
     * @param totalFilmArrayList ArrayList totalFilmArrayList
     */
    private void displayFilmInfo(int filmNumber, ArrayList<Film> totalFilmArrayList) {
        if (totalFilmArrayList.size() > filmNumber) {
            Film film = totalFilmArrayList.get(filmNumber);
            super.getDispenser().setTitle("Film Selector: " + film.getName());
            super.getDispenser().setDescription(film.getDescription());
            super.getDispenser().setImage(film.getPoster());
            super.getDispenser().setOption(0, "Duration: " + film.getDuration() + "h");
            super.getDispenser().setOption(1, "Price: "  + film.getPrice() + "$");
        }
    }

    /**
     * Displays Session buttons
     * @param theater Theater selectedTheater
     * @param film Film selectedFilm
     */
    private void displaySessionButtons (Theater theater, Film film) {
        super.getDispenser().setTitle("Session Selector: " + film.getName());
        super.getDispenser().setOption(4, "Go Back");
        // displays sessions as buttons
        int cont = 0;
        for (Session session : theater.getSessionList()) {
            super.getDispenser().setOption(cont, session.getHour().toString());
            cont++;
        }
        // set auxiliary stuff to "empty" buttons
        if (theater.getSessionList().size() < 4) {
            for (int emptybuttons = theater.getSessionList().size(); emptybuttons < 4;  emptybuttons++) {
                super.getDispenser().setOption(emptybuttons, null);
            }
        }
    }

    /**
     * Display Theater Session seats
     * @param theater Theater selectedTheater
     * @param session Session SelectedSession
     */
    private void displaySeats(Theater theater, Session session) {
        super.getDispenser().setTheaterMode(theater.getMaxRows(), theater.getMaxCols());
        super.getDispenser().setTitle("Seat Selector for: " + session.getHour().toString());
        super.getDispenser().setOption(0, "Go Back");
        super.getDispenser().setOption(1, "Continue");
        for (int row = 1; row < theater.getMaxRows() + 1; row++) {
            for (int col = 1; col < theater.getMaxCols() + 1; col++) {
                // checks if Seat is contained in Session occupiedSeatArrayList
                if (session.isContained(row, col)) {
                    if (!session.isOccupied(row, col)) {
                        // unoccupied Seat
                        super.getDispenser().markSeat(row, col, 2);
                    } else {
                        // occupied Seat
                        super.getDispenser().markSeat(row, col, 1);
                    }
                } else {
                    // empty space
                    super.getDispenser().markSeat(row, col, 0);
                }
            }
        }
    }

    /**
     * Gets this className
     * @return String className
     */
    @Override
    public String getTitle() {
        return this.getClass().getSimpleName();
    }

}
