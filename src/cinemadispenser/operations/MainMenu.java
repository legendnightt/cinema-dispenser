package cinemadispenser.operations;

import cinemadispenser.Operation;
import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;

import java.util.ArrayList;
import java.util.List;

public class MainMenu {

    public List<Operation> operationList = new ArrayList<>();

    public MainMenu(CinemaTicketDispenser dispenser, UrjcBankServer bank) {
        IdiomSelection idiom = new IdiomSelection();
        MovieTicketSale sale = new MovieTicketSale(dispenser, bank);
    }

}
