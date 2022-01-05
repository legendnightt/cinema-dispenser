package cinemadispenser;

import sienens.CinemaTicketDispenser;

public abstract class Operation {

    private final CinemaTicketDispenser dispenser;
    private final Multiplex multiplex;

    public abstract void doOperation();
    public abstract String getTitle();

    /**
     * Operation builder
     * @param dispenser CinemaTicketDispenser dispenser
     * @param multiplex Multiplex multiplex
     */
    public Operation(CinemaTicketDispenser dispenser, Multiplex multiplex) {
        this.dispenser = dispenser;
        this.multiplex = multiplex;
    }

    /**
     * Gets CinemaTicketDispenser dispenser
     * @return CinemaTicketDispenser dispenser
     */
    public CinemaTicketDispenser getDispenser() {
        return dispenser;
    }

    /**
     * Gets Multiplex multiplex
     * @return Multiplex multiplex
     */
    public Multiplex getMultiplex() {
        return multiplex;
    }

}
