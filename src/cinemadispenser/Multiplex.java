package cinemadispenser;

import cinemadispenser.operations.MainMenu;
import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;

public class Multiplex {

    public void start() {
        CinemaTicketDispenser dispenser = new CinemaTicketDispenser();
        UrjcBankServer bank = new UrjcBankServer();
        MainMenu menu = new MainMenu(dispenser, bank);
    }

}
