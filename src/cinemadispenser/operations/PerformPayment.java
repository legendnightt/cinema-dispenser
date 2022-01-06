package cinemadispenser.operations;

import cinemadispenser.Multiplex;
import cinemadispenser.Operation;
import sienens.CinemaTicketDispenser;

public class PerformPayment extends Operation {

    public PerformPayment(CinemaTicketDispenser dispenser, Multiplex multiplex) {
        super(dispenser, multiplex);
    }

    /**
     * Does the operation needed in this case
     */
    @Override
    public void doOperation() {

    }

    /**
     * Gets the proper Title in this case
     * @return String title
     */
    @Override
    public String getTitle() {
        return "PerformPayment";
    }

}
