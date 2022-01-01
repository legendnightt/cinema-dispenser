package cinemadispenser.operations;

import cinemadispenser.operations.update.MultiplexState;
import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MovieTicketSale {

    public MovieTicketSale(CinemaTicketDispenser dispenser, UrjcBankServer bank) {
        PerformPayment payment = new PerformPayment(bank);
        this.update(dispenser, bank);
    }

    private void update(CinemaTicketDispenser dispenser, UrjcBankServer bank) {

        Date date = new Date();
        Timer timer = new Timer();

        // timer since program starts until 24h after
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    MultiplexState state = new MultiplexState(dispenser, bank);
                } catch (FileNotFoundException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }, date, 24*60*60*1000);

    }

}
