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
        super.getDispenser().setTitle("Language Changer");
        super.getDispenser().setDescription("Select the language you want to change");
        int cont = 0;
        for (String idiom: super.getMultiplex().getIdiomHashSet()) {
            super.getDispenser().setOption(cont, idiom);
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
     * Gets the proper title in this case
     * @return String title
     */
    @Override
    public String getTitle() {
        return "IdiomSelection";
    }

}
