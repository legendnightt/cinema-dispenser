package cinemadispenser.operations;

import cinemadispenser.Multiplex;
import cinemadispenser.Operation;
import cinemadispenser.state.Film;
import cinemadispenser.state.MultiplexState;
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
        // more accessible Films
        ArrayList<Film> totalFilmArrayList = new ArrayList<>();
        for (Theater theater : this.state.getTheaterList()) {
            totalFilmArrayList.addAll(theater.getFilmList());
        }
        super.getDispenser().setOption(2, "Next Film");
        super.getDispenser().setOption(3, "Previous Film");
        super.getDispenser().setOption(4, "Continue");
        super.getDispenser().setOption(5, "Exit");
        // displays first film
        displayFilmInfo(0, totalFilmArrayList);
        int filmCont = 0;
        char option = super.getDispenser().waitEvent(5);
        // if time is over, goes back to main loop & MainMenu
        while ((int) option != 0) {
            System.out.println(filmCont);
            switch (option) {
                // Next Film
                case 'C':
                    if (filmCont < totalFilmArrayList.size()) {
                        filmCont += 1;
                        displayFilmInfo(filmCont, totalFilmArrayList);
                    }
                    break;
                // Previous Film
                case 'D':
                    if (filmCont > 0) {
                        filmCont -= 1;
                        displayFilmInfo(filmCont, totalFilmArrayList);
                    }
                    break;
                case 'E':
                    // continue so, enters the selected movie to select Theater

                    break;
                case 'F':
                    // cancel so, goes back to main loop & MainMenu
                    option = '\u0000';
                    break;
            }
            // exit if case 'F':
            if ((int) option == 0) {
                break;
            } else {
                option = super.getDispenser().waitEvent(5);
            }
        }
        System.out.println("EXIT");
    }

    /**
     * Displays Film info
     * @param filmNumber int filmNumber
     * @param totalFilmArrayList ArrayList totalFilmArrayList
     */
    private void displayFilmInfo(int filmNumber, ArrayList<Film> totalFilmArrayList) {
        if (totalFilmArrayList.size() > filmNumber) {
            super.getDispenser().setTitle("Film Selector: " + totalFilmArrayList.get(filmNumber).getName());
            super.getDispenser().setDescription(totalFilmArrayList.get(filmNumber).getDescription());
            super.getDispenser().setImage(totalFilmArrayList.get(filmNumber).getPoster());
            super.getDispenser().setOption(0, "Duration: " + totalFilmArrayList.get(filmNumber).getDuration() + "h");
            super.getDispenser().setOption(1, "Price: "  + totalFilmArrayList.get(filmNumber).getPrice() + "$");
        }
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
