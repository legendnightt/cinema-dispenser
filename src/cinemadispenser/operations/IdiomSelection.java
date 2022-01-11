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
        super.getDispenser().setTitle("Language Selector");
        super.getDispenser().setDescription("Select the language you want to change");
        int cont = 0;
        for (String idiom: super.getMultiplex().getIdiomHashSet()) {
            super.getDispenser().setOption(cont, idiom.toUpperCase());
            cont++;
        }
        char option = super.getDispenser().waitEvent(30);
        if (option == 'A') {
            super.getMultiplex().setIdiom("en");
        } else if (option == 'B') {
            super.getMultiplex().setIdiom("es");
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
