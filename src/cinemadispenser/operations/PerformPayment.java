package cinemadispenser.operations;

import cinemadispenser.Multiplex;
import cinemadispenser.Operation;
import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;

import javax.naming.CommunicationException;

/**
 * PerformPayment class extends Operation
 */
public class PerformPayment extends Operation {

    /**
     * UrjcBankServer bank
     */
    private final UrjcBankServer bank;

    /**
     * PerformPayment builder
     * @param dispenser CinemaTicketDispenser dispenser
     * @param multiplex Multiplex multiplex
     */
    public PerformPayment(CinemaTicketDispenser dispenser, Multiplex multiplex) {
        super(dispenser, multiplex);
        this.bank = new UrjcBankServer();
    }

    /**
     * Does the operation needed in this case
     */
    @Override
    public void doOperation() {
        // waits for credit card
        char option = super.getDispenser().waitEvent(30);
        if (option == '1' && this.bank.comunicationAvaiable()) {
            try {
                this.bank.doOperation(super.getDispenser().getCardNumber(), super.getMultiplex().getPurchasePrice());
                super.getMultiplex().setPurchaseStatus(true);
                boolean expelled = super.getDispenser().expelCreditCard(30);
                // credit card retain if time expires
                if (!expelled) {
                    super.getDispenser().retainCreditCard(true);
                }
            } catch (CommunicationException error) {
                super.getMultiplex().setPurchaseStatus(false);
            }
        } else {
            super.getMultiplex().setPurchaseStatus(false);
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
