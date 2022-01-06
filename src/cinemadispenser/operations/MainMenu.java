package cinemadispenser.operations;

import cinemadispenser.Multiplex;
import cinemadispenser.Operation;
import sienens.CinemaTicketDispenser;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends Operation {

    public List<Operation> operationList = new ArrayList<>();

    public MainMenu(CinemaTicketDispenser dispenser, Multiplex multiplex) {
        super(dispenser, multiplex);
        // adding operations
        operationList.add(this);
        operationList.add(new IdiomSelection(dispenser, multiplex));
        operationList.add(new MovieTicketSale(dispenser, multiplex));
        operationList.add(new PerformPayment(dispenser, multiplex));
    }

    /**
     * Does the operation needed in this case
     */
    @Override
    public void doOperation() {
        super.getDispenser().setMenuMode();
        super.getDispenser().setTitle(getTitle());
    }

    /**
     * Gets the proper Title in this case
     * @return String title
     */
    @Override
    public String getTitle() {
        return "MainMenu";
    }

}
