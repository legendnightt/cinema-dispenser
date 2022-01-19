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
     * Operation payment
     */
    private final Operation payment;
    /**
     * String serializableDirectory
     */
    private final String serializableDirectory = "./src/resources/serializable";
    /**
     * String serializablePath
     */
    private final String serializablePath = serializableDirectory + "/state.bin";

    /**
     * MovieTicketSale builder
     * @param dispenser CinemaTicketDispenser dispenser
     * @param multiplex Multiplex multiplex
     * @throws IOException IO exception
     * @throws ClassNotFoundException Class not found
     */
    public MovieTicketSale(CinemaTicketDispenser dispenser, Multiplex multiplex) throws IOException, ClassNotFoundException {
        super(dispenser, multiplex);
        File directory = new File(this.serializableDirectory);
        // creates /serializable directory if it doesn't exist
        if (!directory.exists()) {
            if (directory.mkdir()) {
                System.out.println("No state directory so, created successfully!");
            }
        }
        File file = new File(this.serializablePath);
        if (file.exists()) {
            LocalDateTime now = LocalDateTime.now();
            // state.bin FileTime
            BasicFileAttributes attr = Files.readAttributes(Path.of(this.serializablePath), BasicFileAttributes.class);
            // converts FileTime into LocalDateTime
            LocalDateTime convertedFileTime = LocalDateTime.ofInstant(attr.lastModifiedTime().toInstant(), ZoneId.systemDefault());
            // if state.bin exists & is not created in the same day, new MultiplexState & it creates again
            if (ChronoUnit.DAYS.between(convertedFileTime, now) != 0) {
                this.serializeMultiplexState(this.serializablePath);
                System.out.println("Old State so, new state generated and serialized successfully!");
            } else {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.serializablePath));
                this.state = (MultiplexState) in.readObject();
                System.out.println("New state created from state.bin successfully!");
            }
        } else {
            this.serializeMultiplexState(this.serializablePath);
            System.out.println("No state.bin file so, New state generated and serialized successfully!");
        }
        // creates PerformPayment Operation
        this.payment = new PerformPayment(super.getDispenser(), super.getMultiplex());
    }

    /**
     * Serialize Multiplex State
     * @param serializablePath String serializablePath
     * @throws IOException if something fails inside
     */
    private void serializeMultiplexState(String serializablePath) throws IOException {
        if (this.state == null) {
            this.state = new MultiplexState();
        }
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(serializablePath));
        out.writeObject(this.state);
        out.flush();
        out.close();
    }

    /**
     * Deserialize Multiplex State
     * @param serializablePath String serializablePath
     */
    private void deserializeMultiplexState(String serializablePath) {
        File file = new File(serializablePath);
        if (!file.exists()) {
            if (file.delete()) {
                System.out.println("state.bin deserialize");
            }
        }
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
                Session selectedSession = this.selectSession(selectedFilm);
                if (selectedSession != null) {
                    // Seats selection
                    ArrayList<Seat> selectSeats = this.selectSeats(selectedTheater, selectedSession);
                    if (selectSeats.size() != 0) {
                        this.PerformPayment(selectedTheater, selectedFilm, selectedSession, selectSeats);
                    }
                }
            }
        }
    }

    /**
     * Displays Film info
     * @param selectedFilm Film selectedFilm
     */
    private void displayFilmInfo(Film selectedFilm) {
        super.getDispenser().setTitle(super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Film_Title") + ": " + selectedFilm.getName());
        super.getDispenser().setDescription(selectedFilm.getDescription());
        super.getDispenser().setImage(selectedFilm.getPoster());
        super.getDispenser().setOption(0, super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Film_Duration") + ": " + selectedFilm.getDuration() + "h");
        super.getDispenser().setOption(1, super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Film_Price") + ": "  + selectedFilm.getPrice() + "$");
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
        this.displayFilmInfo(totalFilmArrayList.get(0));
        int filmCont = 0;
        Film selectedFilm = null;
        char option = super.getDispenser().waitEvent(30);
        while ((int) option != 0) {
            switch (option) {
                // Next Film
                case 'C':
                    if (filmCont < totalFilmArrayList.size() - 1) {
                        filmCont += 1;
                        this.displayFilmInfo(totalFilmArrayList.get(filmCont));
                    } break;
                // Previous Film
                case 'D':
                    if (filmCont > 0) {
                        filmCont -= 1;
                        this.displayFilmInfo(totalFilmArrayList.get(filmCont));
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
            if ((int) option != 0) {
                option = super.getDispenser().waitEvent(30);
            }
        }
        return selectedFilm;
    }

    /**
     * Makes possible to select Session inside Theater
     * @param selectedFilm Film selectedFilm
     * @return Session selectedSession
     */
    private Session selectSession(Film selectedFilm) {
        // Session buttons
        super.getDispenser().setTitle(super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Session_Title") + ": " + selectedFilm.getName());
        // displays sessions as buttons
        int cont = 0;
        for (Session session : selectedFilm.getSessionList()) {
            super.getDispenser().setOption(cont, session.getHour().toString());
            cont++;
        }
        // set auxiliary stuff to "empty" buttons
        if (selectedFilm.getSessionList().size() < 30) {
            for (int emptybuttons = selectedFilm.getSessionList().size(); emptybuttons < 5;  emptybuttons++) {
                super.getDispenser().setOption(emptybuttons, null);
            }
        }
        Session selectedSession = null;
        char option = super.getDispenser().waitEvent(30);
        switch (option) {
            // Session 1
            case 'A':
                if (0 < selectedFilm.getSessionList().size()) {
                    selectedSession = selectedFilm.getSessionList().get(0);
                } break;
            // Session 2
            case 'B':
                if (1 < selectedFilm.getSessionList().size()) {
                    selectedSession = selectedFilm.getSessionList().get(1);
                } break;
            // Session 3
            case 'C':
                if (2 < selectedFilm.getSessionList().size()) {
                    selectedSession = selectedFilm.getSessionList().get(2);
                } break;
            // Session 4
            case 'D':
                if (3 < selectedFilm.getSessionList().size()) {
                    selectedSession = selectedFilm.getSessionList().get(3);
                } break;
            // Session 5
            case 'E':
                if (4 < selectedFilm.getSessionList().size()) {
                    selectedSession = selectedFilm.getSessionList().get(4);
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
     * @param selectedTheater Theater selectedTheater
     * @param selectedSession Session SelectedSession
     */
    private void displaySeats(Theater selectedTheater, Session selectedSession) {
        super.getDispenser().setTheaterMode(selectedTheater.getMaxRows(), selectedTheater.getMaxCols());
        super.getDispenser().setTitle(super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Seat_Title") + ": " + selectedSession.getHour());
        super.getDispenser().setOption(0, super.getMultiplex().getIdiomBundle().getString("Exit"));
        super.getDispenser().setOption(1, super.getMultiplex().getIdiomBundle().getString("Continue"));
        for (int row = 1; row < selectedTheater.getMaxRows() + 1; row++) {
            for (int col = 1; col < selectedTheater.getMaxCols() + 1; col++) {
                // checks if Seat is contained in Session Set occupiedSeatSet
                if (selectedSession.isOccupied(row, col)) {
                    // occupied Seat
                    super.getDispenser().markSeat(row, col, 1);
                }
                // checks if Seat is contained in Theater Set SeatSet
                else if (selectedTheater.getSeatSet().contains(new Seat(row, col))) {
                    // unoccupied Seat
                    super.getDispenser().markSeat(row, col, 2);
                }
                // not contained
                else {
                    // empty space
                    super.getDispenser().markSeat(row, col, 0);
                }
            }
        }
    }

    /**
     * Makes possible to select Seats inside Session
     * @param selectedTheater Theater selectedTheater
     * @param selectedSession Session selectedSession
     */
    private ArrayList<Seat> selectSeats(Theater selectedTheater, Session selectedSession) {
        this.displaySeats(selectedTheater, selectedSession);
        int maxSeats = 3;
        int contSeats = 0;
        ArrayList<Seat> selectedSeats = new ArrayList<>();
        char option = super.getDispenser().waitEvent(30);
        while ((int) option != 0) {
            // Exit
            if (option == 'A') {
                // exit without selecting Seats
                contSeats = 0;
                option = '\u0000'; // default char value
            }
            // Continue
            else if (option == 'B') {
                // Seats selected & continue to bank
                if (contSeats > 0) {
                    option = '\u0000'; // default char value
                } else {
                    option = super.getDispenser().waitEvent(30);
                }
            }
            // Seat Selected
            else {
                byte row = (byte)((option & 0xFF00) >> 8);
                byte col = (byte)(option & 0xFF);
                // if less than this.maxSeats selected & not occupied
                if (!selectedSession.isOccupied(row, col)) {
                    if (maxSeats > contSeats) {
                        selectedSession.ocuppiesSeat(row, col);
                        selectedSeats.add(new Seat(row, col));
                        contSeats++;
                    }
                } else {
                    selectedSession.unocuppiesSeat(row, col);
                    selectedSeats.remove(new Seat(row, col));
                    contSeats--;
                }
                this.displaySeats(selectedTheater, selectedSession);
                option = super.getDispenser().waitEvent(30);
            }
        }
        return selectedSeats;
    }

    /**
     * Computes total Seats price
     * @param selectedFilm Film selectedFilm,
     * @param contSeats int contSeats
     */
    private void computePrice(Film selectedFilm, int contSeats) {
        super.getMultiplex().setPurchasePrice(selectedFilm.getPrice() * contSeats);
    }

    /**
     * Prints Tickets with info on them
     * @param selectedTheater Theater selectedTheater
     * @param selectedFilm Film selectedFilm
     * @param selectedSession Session selectedSession
     * @param selectedSeats ArrayList selectedSeats
     */
    private void printTickets(Theater selectedTheater, Film selectedFilm, Session selectedSession, ArrayList<Seat> selectedSeats) {
        // Prints ticket for each Seat
        for (Seat seat : selectedSeats) {
            ArrayList<String> ticket = new ArrayList<>();
            ticket.add("   " + super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Tickets_Title") + " " + selectedFilm.getName());
            ticket.add("   ===================");
            ticket.add("   " + super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Tickets_Theater") + " " + selectedTheater.getNumber());
            ticket.add("   " + selectedSession.getHour().toString());
            ticket.add("   " + super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Tickets_Row") + " " + seat.getRow());
            ticket.add("   " + super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Tickets_Seat") + " " + seat.getCol());
            ticket.add("   " + super.getMultiplex().getIdiomBundle().getString("MovieTicketSale_Tickets_Price") + " " + selectedFilm.getPrice() + "$");
            super.getDispenser().print(ticket);
        }
    }

    /**
     * Makes possible to Perform Payment
     * @param selectedTheater Theater selectedTheater
     * @param selectedFilm Film selectedFilm
     * @param selectedSession Session selectedSession
     * @param selectedSeats ArrayList selectedSeats
     */
    private void PerformPayment(Theater selectedTheater, Film selectedFilm, Session selectedSession, ArrayList<Seat> selectedSeats) {
        this.computePrice(selectedFilm, selectedSeats.size());
        // prints PerformPayment info
        super.getDispenser().setMessageMode();
        super.getDispenser().setTitle(super.getMultiplex().getIdiomBundle().getString("PerformPayment_Title"));
        super.getDispenser().setDescription(selectedSeats.size() + " " + super.getMultiplex().getIdiomBundle().getString("PerformPayment_Description") + " " +  selectedFilm.getName() + "(" + selectedSession.getHour() + "): " + super.getMultiplex().getPurchasePrice() + "$");
        super.getDispenser().setOption(0, null);
        super.getDispenser().setOption(1, null);
        this.payment.doOperation();
        // checks if Purchase Operation was successfully
        if (super.getMultiplex().getPurchaseStatus()) {
            try {
                this.deserializeMultiplexState(this.serializablePath);
                this.serializeMultiplexState(this.serializablePath);
                super.getDispenser().setTitle("Printing Tickets");
                super.getDispenser().setDescription("........");
                super.getDispenser().waitEvent(5);
                this.printTickets(selectedTheater, selectedFilm, selectedSession, selectedSeats);
                super.getDispenser().setTitle("Take your Tickets");
                super.getDispenser().setDescription("Thanks for trusting us for watching best cinema movies\nHope to see you back!");
                super.getDispenser().waitEvent(10);
            } catch (IOException e) {
                e.printStackTrace();
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
