package cinemadispenser;

import cinemadispenser.operations.MainMenu;
import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;

import java.io.File;
import java.util.HashSet;
import java.util.Objects;

public class Multiplex {

    private final HashSet<String> idiomHashSet = new HashSet<>();
    private String idiom;

    /**
     * Starts all & have main loop of the application
     */
    public void start() {
        CinemaTicketDispenser dispenser = new CinemaTicketDispenser();
        UrjcBankServer bank = new UrjcBankServer();
        MainMenu menu = new MainMenu(dispenser, this);
        File idiomsDir = new File("./src/resources/idioms/");
        // searches for idioms and collect them into HashSet<String> idiomHashSet
        for (File idiomFile: Objects.requireNonNull(idiomsDir.listFiles())) {
            // cinemadispenser_idiom.properties -> idiom.properties
            final String headclear = idiomFile.toString().substring(idiomFile.toString().indexOf("_") + 1).trim();
            // idiom.properties -> idiom
            idiomHashSet.add(headclear.substring(0, headclear.length() - 11));
        }
        for (Operation operation: menu.operationList) {
            if (Objects.equals(operation.getTitle(), "MainMenu")) {
                menu.operationList.get(0).doOperation();
            }
        }
    }

    /**
     * Sets idiom if exists in HashSet<String> idiomHashSet
     * @param idiom String idiom
     * @return boolean, true if idiom exists in HashSet<String> idiomHashSet, false if it doesn't
     */
    public boolean setIdiom(String idiom) {
        if (idiomHashSet.contains(idiom)) {
            this.idiom = idiom;
            return true;
        }
        return false;
    }

    /**
     * Gets HashSet<String> idiomHashSet
     * @return HashSet<String> idiomHashSet
     */
    public HashSet<String> getIdiomHashSet() {
        return idiomHashSet;
    }

    /**
     * Gets String idiom
     * @return String idiom
     */
    public String getIdiom() {
        return idiom;
    }

}
