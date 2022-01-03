package cinemadispenser.operations;

import cinemadispenser.operations.update.MultiplexState;
import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;

import java.time.temporal.ChronoUnit;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MovieTicketSale {

    private MultiplexState state;

    /**
     * MovieTicketSale builder
     * @param dispenser CinemaTicketDispenser dispenser
     * @param bank UrjcBankServer bank
     */
    public MovieTicketSale(CinemaTicketDispenser dispenser, UrjcBankServer bank) {
        PerformPayment payment = new PerformPayment(bank);
        update(dispenser, bank);
    }

    /**
     * Updates every 24h /update
     * @param dispenser CinemaTicketDispenser dispenser
     * @param bank UrjcBankServer bank
     */
    private void update(CinemaTicketDispenser dispenser, UrjcBankServer bank) {
        String serializableDirectory = "./src/cinemadispenser/serializable";
        String serializablePath = serializableDirectory + "/state.bin";
        try {
            File directory = new File(serializableDirectory);
            // creates /serializable directory if it doesn't exist
            if (!directory.exists()) {
                directory.mkdir();
            }
            File file = new File(serializablePath);
            if (file.exists()) {
                LocalDateTime now = LocalDateTime.now();
                // state.bin FileTime
                BasicFileAttributes attr = Files.readAttributes(Path.of(serializablePath), BasicFileAttributes.class);
                // converts FileTime into LocalDateTime
                LocalDateTime convertedFileTime = LocalDateTime.ofInstant(attr.creationTime().toInstant(), ZoneId.systemDefault());
                // if state.bin exists & is not created in the same day, new MultiplexState & it creates again
                if (ChronoUnit.DAYS.between(convertedFileTime, now) != 0) {
                    generateState(dispenser, bank, serializablePath);
                } else {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(serializablePath));
                    state = (MultiplexState) in.readObject();
                    System.out.println("New state created from state.bin successfully!");
                }
            } else {
                generateState(dispenser, bank, serializablePath);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        /* Timer
        Date date = new Date();
        Timer timer = new Timer();
        // timer since program starts until 24h after
        timer.schedule(new TimerTask() {
            public void run() {

            }
        }, date, 24*60*60*1000);
         */
    }

    /**
     * Generates state & state.bin
     * @param dispenser CinemaTicketDispenser dispenser
     * @param bank UrjcBankServer bank
     * @param serializablePath String serializablePath
     * @throws IOException if something fails inside
     */
    private void generateState(CinemaTicketDispenser dispenser, UrjcBankServer bank, String serializablePath) throws IOException {
        state = new MultiplexState(dispenser, bank);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(serializablePath));
        out.writeObject(state);
        out.flush();
        out.close();
        System.out.println("New state generated and serialized successfully!");
    }

}
