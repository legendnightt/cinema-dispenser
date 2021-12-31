package cinemadispenser.operations;

import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;

public class MainMenu {

    public MainMenu(CinemaTicketDispenser dispenser, UrjcBankServer bank) {
        IdiomSelection idiom = new IdiomSelection();
        MovieTicketSale sale = new MovieTicketSale(dispenser, bank);
    }

}
