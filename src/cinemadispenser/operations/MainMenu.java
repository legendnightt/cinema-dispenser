package cinemadispenser.operations;

import cinemadispenser.Multiplex;
import cinemadispenser.Operation;
import sienens.CinemaTicketDispenser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * MainMenu class extends Operation
 */
public class MainMenu extends Operation {

    private final List<Operation> operationList = new ArrayList<>();

    /**
     * MainMenu builder
     * @param dispenser CinemaTicketDispenser dispenser
     * @param multiplex Multiplex multiplex
     */
    public MainMenu(CinemaTicketDispenser dispenser, Multiplex multiplex) {
        super(dispenser, multiplex);
        // adding operations
        operationList.add(this);
        operationList.add(new IdiomSelection(dispenser, multiplex));
        operationList.add(new MovieTicketSale(dispenser, multiplex));
        operationList.add(new PerformPayment(dispenser, multiplex));
    }

    /**
     * Gets List operationList
     * @return List operationList
     */
    public List<Operation> getOperationList() {
        return operationList;
    }

    /**
     * Does the operation needed in this case
     */
    @Override
    public void doOperation() {
        super.getDispenser().setMessageMode();
        super.getDispenser().setTitle("Welcome to Cinema Dispenser");
        super.getDispenser().setDescription("Select the Operation you want to make");
        super.getDispenser().setOption(0, "Buy Film Tickets");
        super.getDispenser().setOption(1, "Change Language");
        // wait until 30s, goes back to Multiplex & resets language
        char option = super.getDispenser().waitEvent(30);
        if (option == 'A') {
            for (Operation operation: operationList) {
                if (Objects.equals(operation.getTitle(), "MovieTicketSale")) {
                    operation.doOperation();
                }
            }
        } else if (option == 'B') {
            for (Operation operation: operationList) {
                if (Objects.equals(operation.getTitle(), "IdiomSelection")) {
                    operation.doOperation();
                }
            }
        }
    }

    /**
     * Gets the proper title in this case
     * @return String title
     */
    @Override
    public String getTitle() {
        return "MainMenu";
    }

}
