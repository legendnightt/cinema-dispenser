package cinemadispenser.operations;

import cinemadispenser.Multiplex;
import cinemadispenser.Operation;
import sienens.CinemaTicketDispenser;

/**
 * PerformPayment class extends Operation
 */
public class PerformPayment extends Operation {

    /**
     * PerformPayment builder
     * @param dispenser CinemaTicketDispenser dispenser
     * @param multiplex Multiplex multiplex
     */
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
     * Gets this className
     * @return String className
     */
    @Override
    public String getTitle() {
        return this.getClass().getSimpleName();
    }

}
