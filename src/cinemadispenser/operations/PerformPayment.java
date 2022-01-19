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
        // checks bank communication
        if (this.bank.comunicationAvaiable()) {
            // waits for de credit card
            char option = super.getDispenser().waitEvent(30);
            // credit card inserted
            if (option == '1') {
                try {
                    // retain credit card 5 seconds to make operation
                    super.getDispenser().retainCreditCard(false);
                    super.getDispenser().setTitle(super.getMultiplex().getIdiomBundle().getString("PerformPayment_ProcessingPayment"));
                    super.getDispenser().setDescription("........");
                    super.getDispenser().waitEvent(5);
                    // tries to charge Amount
                    super.getMultiplex().setPurchaseStatus(this.bank.doOperation(super.getDispenser().getCardNumber(), super.getMultiplex().getPurchasePrice()));
                    // waits 30 seconds with credit card spelled
                    super.getDispenser().setTitle(super.getMultiplex().getIdiomBundle().getString("PerformPayment_CreditCardExpelled_Title"));
                    super.getDispenser().setDescription(super.getMultiplex().getIdiomBundle().getString("PerformPayment_CreditCardExpelled_Description"));
                    boolean expelled = super.getDispenser().expelCreditCard(30);
                    // credit card is retain definitely if time expires
                    if (!expelled) {
                        super.getDispenser().retainCreditCard(true);
                        super.getDispenser().setTitle(super.getMultiplex().getIdiomBundle().getString("PerformPayment_CreditCardRetained_Title"));
                        super.getDispenser().setDescription(super.getMultiplex().getIdiomBundle().getString("PerformPayment_CreditCardRetained_Description"));
                        // waits 5s for reading message
                        super.getDispenser().waitEvent(10);
                    }
                } catch (CommunicationException error) {
                    super.getMultiplex().setPurchaseStatus(false);
                }
            } else {
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
