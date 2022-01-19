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

    /**
     * Operation List
     */
    private final List<Operation> operationList = new ArrayList<>();

    /**
     * MainMenu builder
     * @param dispenser CinemaTicketDispenser dispenser
     * @param multiplex Multiplex multiplex
     */
    public MainMenu(CinemaTicketDispenser dispenser, Multiplex multiplex) {
        super(dispenser, multiplex);
        // adding operations
        this.operationList.add(this);
        this.operationList.add(new IdiomSelection(dispenser, multiplex));
        this.operationList.add(new MovieTicketSale(dispenser, multiplex));
    }

    /**
     * Gets List operationList
     * @return List operationList
     */
    public List<Operation> getOperationList() { return this.operationList; }

    /**
     * Does the operation needed in this case
     */
    @Override
    public void doOperation() {
        super.getDispenser().setMessageMode();
        super.getDispenser().setTitle(super.getMultiplex().getIdiomBundle().getString("MainMenu_Title"));
        super.getDispenser().setDescription(super.getMultiplex().getIdiomBundle().getString("MainMenu_Description"));
        super.getDispenser().setOption(0, super.getMultiplex().getIdiomBundle().getString("MainMenu_Option1"));
        super.getDispenser().setOption(1, super.getMultiplex().getIdiomBundle().getString("MainMenu_Option2"));
        // wait until 30s, goes back to Multiplex & resets language
        char option = super.getDispenser().waitEvent(30);
        if (option == 'A') {
            for (Operation operation: this.operationList) {
                if (Objects.equals(operation.getTitle(), "MovieTicketSale")) {
                    operation.doOperation();
                }
            }
        } else if (option == 'B') {
            for (Operation operation: this.operationList) {
                if (Objects.equals(operation.getTitle(), "IdiomSelection")) {
                    operation.doOperation();
                }
            }
        }
    }

    /**
     * Gets this className
     * @return String title
     */
    @Override
    public String getTitle() {
        return this.getClass().getSimpleName();
    }

}
