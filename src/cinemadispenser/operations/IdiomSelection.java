package cinemadispenser.operations;

import cinemadispenser.Multiplex;
import cinemadispenser.Operation;
import sienens.CinemaTicketDispenser;

public class IdiomSelection extends Operation {

    public IdiomSelection(CinemaTicketDispenser dispenser, Multiplex multiplex) {
        super(dispenser, multiplex);
    }

    /**
     * Does the operation needed in this case
     */
    @Override
    public void doOperation() {
        int cont = 0;
        for (String idiom: super.getMultiplex().getIdiomHashSet()) {
            super.getDispenser().setOption(cont, idiom);
            cont++;
        }
    }

    /**
     * Gets the proper Title in this case
     * @return String title
     */
    @Override
    public String getTitle() {
        return "IdiomSelection";
    }

}
