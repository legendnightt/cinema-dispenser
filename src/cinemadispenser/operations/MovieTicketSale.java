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
        // Theater selection with films
        Film selectedFilm = this.selectFilm();
        if (selectedFilm != null) {
            Theater selectedTheater = null;
            // gets Film Theater
            for (Theater theater : this.state.getTheaterList()) {
                if (theater.getFilmList().contains(selectedFilm)) { selectedTheater = theater; }
            }
            // Session selection
            if (selectedTheater != null) {
                Session selectedSession = this.selectSession(selectedTheater, selectedFilm);
                if (selectedSession != null) {
                    this.selectSeats(selectedTheater, selectedSession);
                }
            }
        }
    }

    /**
     * Displays Film info
     * @param filmNumber int filmNumber
     * @param totalFilmArrayList ArrayList totalFilmArrayList
     */
    private void displayFilmInfo(int filmNumber, ArrayList<Film> totalFilmArrayList) {
        Film film = totalFilmArrayList.get(filmNumber);
        super.getDispenser().setTitle(super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Film_Title") + ": " + film.getName());
        super.getDispenser().setDescription(film.getDescription());
        super.getDispenser().setImage(film.getPoster());
        super.getDispenser().setOption(0, super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Film_Duration") + ": " + film.getDuration() + "h");
        super.getDispenser().setOption(1, super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Film_Price") + ": "  + film.getPrice() + "$");
    }

    /**
     * Makes possible to select Film inside Theaters
     * @return Theater selectedTheater
     */
    private Film selectFilm() {
        super.getDispenser().setMenuMode();
        super.getDispenser().setOption(2, super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Film_NextFilm"));
        super.getDispenser().setOption(3, super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Film_PreviousFilm"));
        super.getDispenser().setOption(4, super.getMultiplex().getIdiomBundle().getString("Continue"));
        super.getDispenser().setOption(5, super.getMultiplex().getIdiomBundle().getString("Exit"));
        // more accessible Films
        ArrayList<Film> totalFilmArrayList = new ArrayList<>();
        for (Theater theater : this.state.getTheaterList()) { totalFilmArrayList.addAll(theater.getFilmList()); }
        // displays first film
        this.displayFilmInfo(0, totalFilmArrayList);
        int filmCont = 0;
        Film selectedFilm = null;
        char option = super.getDispenser().waitEvent(5);
        while ((int) option != 0) {
            switch (option) {
                // Next Film
                case 'C':
                    if (filmCont < totalFilmArrayList.size() - 1) {
                        filmCont += 1;
                        this.displayFilmInfo(filmCont, totalFilmArrayList);
                    } break;
                // Previous Film
                case 'D':
                    if (filmCont > 0) {
                        filmCont -= 1;
                        this.displayFilmInfo(filmCont, totalFilmArrayList);
                    } break;
                // Select Theater from Film, exit & returns Theater
                case 'E':
                    selectedFilm  = totalFilmArrayList.get(filmCont);
                    // exit & returns Theater
                    option = '\u0000'; // default char value
                    break;
                // Exit
                case 'F':
                    // exit & returns null
                    option = '\u0000'; // default char value
                    break;
            }
            // exit if case 'F':
            if ((int) option != 0) { option = super.getDispenser().waitEvent(5); }
        }
        return selectedFilm;
    }

    /**
     * Makes possible to select Session inside Theater
     * @param selectedTheater Theater selectedTheater
     * @return Session selectedSession
     */
    private Session selectSession(Theater selectedTheater, Film selectedFilm) {
        // Session buttons
        super.getDispenser().setTitle(super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Session_Title") + ": " + selectedFilm.getName());
        // displays sessions as buttons
        int cont = 0;
        for (Session session : selectedTheater.getSessionList()) {
            super.getDispenser().setOption(cont, session.getHour().toString());
            cont++;
        }
        // set auxiliary stuff to "empty" buttons
        if (selectedTheater.getSessionList().size() < 5) {
            for (int emptybuttons = selectedTheater.getSessionList().size(); emptybuttons < 5;  emptybuttons++) {
                super.getDispenser().setOption(emptybuttons, null);
            }
        }
        Session selectedSession = null;
        char option = super.getDispenser().waitEvent(5);
        switch (option) {
            // Session 1
            case 'A':
                if (0 < selectedTheater.getSessionList().size()) {
                    selectedSession = selectedTheater.getSessionList().get(0);
                } break;
            // Session 2
            case 'B':
                if (1 < selectedTheater.getSessionList().size()) {
                    selectedSession = selectedTheater.getSessionList().get(1);
                } break;
            // Session 3
            case 'C':
                if (2 < selectedTheater.getSessionList().size()) {
                    selectedSession = selectedTheater.getSessionList().get(2);
                } break;
            // Session 4
            case 'D':
                if (3 < selectedTheater.getSessionList().size()) {
                    selectedSession = selectedTheater.getSessionList().get(3);
                } break;
            // Session 5
            case 'E':
                if (4 < selectedTheater.getSessionList().size()) {
                    selectedSession = selectedTheater.getSessionList().get(4);
                } break;
            // Exit
            case 'F':
                // exits with selectedSession = null
                break;
        }
        return selectedSession;
    }

    /**
     * Display Theater Session seats
     * @param theater Theater selectedTheater
     * @param session Session SelectedSession
     */
    private void displaySeats(Theater theater, Session session) {
        super.getDispenser().setTheaterMode(theater.getMaxRows(), theater.getMaxCols());
        super.getDispenser().setTitle(super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Seat_Title") + ": " + session.getHour().toString());
        super.getDispenser().setOption(0, super.getMultiplex().getIdiomBundle().getString("Exit"));
        super.getDispenser().setOption(1, super.getMultiplex().getIdiomBundle().getString("Continue"));
        for (int row = 1; row < theater.getMaxRows() + 1; row++) {
            for (int col = 1; col < theater.getMaxCols() + 1; col++) {
                // checks if Seat is contained in Session occupiedSeatArrayList
                if (session.isContained(row, col)) {
                    // unoccupied Seat
                    if (!session.isOccupied(row, col)) { super.getDispenser().markSeat(row, col, 2); }
                    // occupied Seat
                    else { super.getDispenser().markSeat(row, col, 1); }
                }
                // empty space
                else { super.getDispenser().markSeat(row, col, 0); }
            }
        }
    }

    /**
     * Makes possible to select Seats inside Session
     * @param selectedTheater Theater selectedTheater
     * @param selectedSession Session selectedSession
     */
    private void selectSeats(Theater selectedTheater, Session selectedSession) {
        this.displaySeats(selectedTheater, selectedSession);
        int maxSeats = 3;
        int contSeats = 0;
        char option = super.getDispenser().waitEvent(5);
        while ((int) option != 0) {
            // Exit
            if (option == 'A') {
                // exit without selecting Seats
                option = '\u0000'; // default char value
            }
            // Continue
            else if (option == 'B') {
                // Seats selected & continue to bank
                if (contSeats > 0) {

                    option = '\u0000'; // default char value
                } else { option = super.getDispenser().waitEvent(5); }
            }
            // Seat Selected
            else {
                byte row = (byte)((option & 0xFF00) >> 8);
                byte col = (byte)(option & 0xFF);
                // if less than this.maxSeats selected & not occupied
                if (!selectedSession.isOccupied(row, col)) {
                    if (maxSeats > contSeats) {
                        selectedSession.ocuppiesSeat(row, col);
                        contSeats++;
                    }
                } else {
                    selectedSession.unocuppiesSeat(row, col);
                    contSeats--;
                }
                this.displaySeats(selectedTheater, selectedSession);
                option = super.getDispenser().waitEvent(5);
            }
        }
    }

    /**
     * Gets this className
     * @return String className
     */
    @Override
    public String getTitle() { return this.getClass().getSimpleName(); }

}
