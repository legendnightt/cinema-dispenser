package cinemadispenser.operations;

import cinemadispenser.Multiplex;
import cinemadispenser.Operation;
import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;

import javax.naming.CommunicationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * PerformPayment class extends Operation
 */
public class PerformPayment extends Operation {

    /**
     * UrjcBankServer bank
     */
    private final UrjcBankServer bank;
    /**
     * List socioList
     */
    private final List<Long> socioList = new ArrayList<>();

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
            // loads socios File
            this.loadSocios();
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
                    // discount for socios 30%
                    if (this.socioList.contains(super.getDispenser().getCardNumber())) {
                        super.getMultiplex().setPurchasePrice((int) (super.getMultiplex().getPurchasePrice() - (super.getMultiplex().getPurchasePrice() * 0.3)));
                        this.getMultiplex().setSocioStatus(true);
                        super.getDispenser().setTitle("Discount for Socios!");
                        super.getDispenser().setDescription("You get a discount for being socio so your import is now: " + super.getMultiplex().getPurchasePrice() + "$");
                        super.getDispenser().waitEvent(5);
                    }
                    // tries to charge Amount
                    super.getMultiplex().setPurchaseStatus(this.bank.doOperation(super.getDispenser().getCardNumber(), super.getMultiplex().getPurchasePrice()));
                    // waits 30 seconds with credit card spelled
                    super.getDispenser().setTitle(super.getMultiplex().getIdiomBundle().getString("PerformPayment_CreditCardExpelled_Title"));
                    super.getDispenser().setDescription(super.getMultiplex().getIdiomBundle().getString("PerformPayment_CreditCardExpelled_Description"));
                    this.retainCreditCard();
                } catch (CommunicationException error) {
                    super.getMultiplex().setPurchaseStatus(false);
                    this.displayCommunicationError();
                    this.retainCreditCard();
                }
            } else {
                super.getMultiplex().setPurchaseStatus(false);
            }
        } else {
            super.getMultiplex().setPurchaseStatus(false);
            this.displayCommunicationError();
        }
    }

    /**
     * Displays communication error message
     */
    private void displayCommunicationError() {
        super.getDispenser().setTitle(super.getMultiplex().getIdiomBundle().getString("PerformPayment_!comunicationAvaiable_Title"));
        super.getDispenser().setDescription(super.getMultiplex().getIdiomBundle().getString("PerformPayment_!comunicationAvaiable_Description"));
        // waits 10s for reading message
        super.getDispenser().waitEvent(10);
    }

    /**
     * Waits for credit card expelled, if time goes to 0 then retains Credit Card definitely & displays message
     */
    private void retainCreditCard() {
        boolean expelled = super.getDispenser().expelCreditCard(30);
        // credit card is retain definitely if time expires
        if (!expelled) {
            super.getDispenser().retainCreditCard(true);
            super.getDispenser().setTitle(super.getMultiplex().getIdiomBundle().getString("PerformPayment_CreditCardRetained_Title"));
            super.getDispenser().setDescription(super.getMultiplex().getIdiomBundle().getString("PerformPayment_CreditCardRetained_Description"));
            // waits 10s for reading message
            super.getDispenser().waitEvent(10);
        }
    }

    /**
     * Makes possible to read & load Socios txt
     */
    private void loadSocios() {
        File sociosFile = new File("./src/resources/socios/socios.txt");
        try {
            Scanner scSocio = new Scanner(new FileReader(Objects.requireNonNull(sociosFile)));
            while (scSocio.hasNextLine()) {
                String sociosLine = scSocio.nextLine();
                StringBuilder socioCard = new StringBuilder();
                while (!sociosLine.isEmpty()) {
                    socioCard.append(sociosLine, 0, 4);
                    // exit when last number of credit card is readed
                    if (!(sociosLine.length() == 4)) {
                        sociosLine = sociosLine.substring(sociosLine.indexOf(" ") + 1).trim();
                    } else {
                        break;
                    }
                }
                this.socioList.add(Long.parseLong(String.valueOf(socioCard)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
