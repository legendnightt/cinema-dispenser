package cinemadispenser.operations;

import cinemadispenser.operations.update.MultiplexState;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MovieTicketSale {

    public MovieTicketSale() {
        PerformPayment payment = new PerformPayment();
        update();
    }

    private void update() {

        Date date = new Date();
        Timer timer = new Timer();

        // timer since program starts until 24h after
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    MultiplexState state = new MultiplexState();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, date, 24*60*60*1000);

    }

}
