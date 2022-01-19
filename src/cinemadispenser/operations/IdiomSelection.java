package cinemadispenser.operations;

import cinemadispenser.Multiplex;
import cinemadispenser.Operation;
import sienens.CinemaTicketDispenser;

/**
 * IdiomSelection class extends Operation
 */
public class IdiomSelection extends Operation {

    /**
     * IdiomSelection builder
     * @param dispenser CinemaTicketDispenser dispenser
     * @param multiplex Multiplex multiplex
     */
    public IdiomSelection(CinemaTicketDispenser dispenser, Multiplex multiplex) {
        super(dispenser, multiplex);
    }

    /**
     * Does the operation needed in this case
     */
    @Override
    public void doOperation() {
        super.getDispenser().setMenuMode();
        super.getDispenser().setTitle(super.getMultiplex().getIdiomBundle().getString("IdiomSelection_Title"));
        super.getDispenser().setDescription(super.getMultiplex().getIdiomBundle().getString("IdiomSelection_Description"));
        int cont = 0;
        for (String idiom: super.getMultiplex().getIdiomArrayList()) {
            super.getDispenser().setOption(cont, idiom.toUpperCase());
            cont++;
        }
        // set auxiliary stuff to "empty" buttons
        if (super.getMultiplex().getIdiomArrayList().size() < 6) {
            for (int emptybuttons = super.getMultiplex().getIdiomArrayList().size(); emptybuttons < 6;  emptybuttons++) {
                super.getDispenser().setOption(emptybuttons, null);
            }
        }
        // language selector
        char option = super.getDispenser().waitEvent(30);
        if (option == 'A' && 0 < super.getMultiplex().getIdiomArrayList().size()) {
            super.getMultiplex().setIdiom(super.getMultiplex().getIdiomArrayList().get(0));
        }
        else if (option == 'B' && 1 < super.getMultiplex().getIdiomArrayList().size()) {
            super.getMultiplex().setIdiom(super.getMultiplex().getIdiomArrayList().get(1));
        }
        else if (option == 'C' && 2 < super.getMultiplex().getIdiomArrayList().size()) {
            super.getMultiplex().setIdiom(super.getMultiplex().getIdiomArrayList().get(2));
        }
        else if (option == 'D' && 3 < super.getMultiplex().getIdiomArrayList().size()) {
            super.getMultiplex().setIdiom(super.getMultiplex().getIdiomArrayList().get(3));
        }
        else if (option == 'E' && 4 < super.getMultiplex().getIdiomArrayList().size()) {
            super.getMultiplex().setIdiom(super.getMultiplex().getIdiomArrayList().get(4));
        }
        else if (option == 'F' && 5 < super.getMultiplex().getIdiomArrayList().size()) {
            super.getMultiplex().setIdiom(super.getMultiplex().getIdiomArrayList().get(5));
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
