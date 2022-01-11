package cinemadispenser.operations;

import cinemadispenser.Multiplex;
import cinemadispenser.Operation;
import cinemadispenser.state.Film;
import cinemadispenser.state.MultiplexState;
import cinemadispenser.state.Session;
import cinemadispenser.state.Theater;
import sienens.CinemaTicketDispenser;

import java.time.temporal.ChronoUnit;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Objects;

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
                generateState(serializablePath);
                System.out.println("Old State so, new state generated and serialized successfully!");
            } else {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(serializablePath));
                state = (MultiplexState) in.readObject();
                System.out.println("New state created from state.bin successfully!");
            }
        } else {
            generateState(serializablePath);
            System.out.println("No state.bin file so, New state generated and serialized successfully!");
        }
    }

    /**
     * Generates state & state.bin
     * @param serializablePath String serializablePath
     * @throws IOException if something fails inside
     */
    private void generateState(String serializablePath) throws IOException {
        state = new MultiplexState();
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(serializablePath));
        out.writeObject(state);
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
        boolean goback = false;
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
                    ArrayList<Session> totalSessionsList = this.displaySessionButtons(selectedFilm);
                    int sessionCont = -1;
                    option = super.getDispenser().waitEvent(seconds);
                    // do while ig
                    while ((int) option != 0 && !goback) {
                        System.out.println("SessionCont: " + sessionCont);
                        switch (option) {
                            // Session 1
                            case 'A':
                                if (0 < totalSessionsList.size() - 1) {
                                    sessionCont = 0;
                                }
                                break;
                            // Session 2
                            case 'B':
                                if (1 < totalSessionsList.size() - 1) {
                                    sessionCont = 1;
                                }
                                break;
                            // Session 3
                            case 'C':
                                if (2 < totalSessionsList.size() - 1) {
                                    sessionCont = 2;
                                }
                                break;
                            // Session 4
                            case 'D':
                                if (3 < totalSessionsList.size() - 1) {
                                    sessionCont = 3;
                                }
                                break;
                            // Go Back
                            case 'E':
                                goback = true;
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
                            Session selectedSession = totalSessionsList.get(sessionCont);
                        } else if ((int) option != 0 && !goback) {
                            option = super.getDispenser().waitEvent(seconds);
                        }
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
                if (goback) {
                    this.displayFilmButtons();
                    this.displayFilmInfo(filmCont, totalFilmArrayList);
                    goback = false;
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
     * @param selectedFilm Film selectedFilm
     * @return ArrayList totalSessionsList
     */
    private ArrayList<Session> displaySessionButtons (Film selectedFilm) {
        super.getDispenser().setTitle("Session Selector: " + selectedFilm.getName());
        super.getDispenser().setOption(4, "Go Back");
        // more accessible Sessions
        ArrayList<Session> totalSessionsList = new ArrayList<>();
        // searches for Theater with the Sessions, where Film is
        for (Theater theater : this.state.getTheaterList()) {
            for (Film film: theater.getFilmList()) {
                if (Objects.equals(selectedFilm.getName(), film.getName())) {
                    totalSessionsList.addAll(theater.getSessionList());
                }
            }
        }
        // displays sessions as buttons
        int cont = 0;
        for (Session session: totalSessionsList) {
            super.getDispenser().setOption(cont, session.getHour().toString());
            cont++;
        }
        // set auxiliary stuff to "empty" buttons
        if (totalSessionsList.size() < 4) {
            for (int emptybuttons = totalSessionsList.size(); emptybuttons < 4;  emptybuttons++) {
                super.getDispenser().setOption(emptybuttons, null);
            }
        }
        return totalSessionsList;
    }

    /**
     * Gets the proper title in this case
     * @return String title
     */
    @Override
    public String getTitle() {
        return "MovieTicketSale";
    }

}
