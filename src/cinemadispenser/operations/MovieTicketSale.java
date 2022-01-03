package cinemadispenser.operations;

import cinemadispenser.operations.update.MultiplexState;
import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MovieTicketSale {

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
     * Updates every 24h all /update
     * @param dispenser CinemaTicketDispenser dispenser
     * @param bank UrjcBankServer bank
     */
    private void update(CinemaTicketDispenser dispenser, UrjcBankServer bank) {
        Date date = new Date();
        Timer timer = new Timer();
        // timer since program starts until 24h after
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    MultiplexState state = new MultiplexState(dispenser, bank);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, date, 24*60*60*1000);
    }

}
