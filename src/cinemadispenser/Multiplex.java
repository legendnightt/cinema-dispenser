package cinemadispenser;

import cinemadispenser.operations.MainMenu;
import sienens.CinemaTicketDispenser;
import urjc.UrjcBankServer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Multiplex class
 */
public class Multiplex {

    /**
     * Idioms String ArrayList
     */
    private final ArrayList<String> idiomArrayList = new ArrayList<>();
    /**
     * Current Idiom working
     */
    private String idiom;

    /**
     * Starts all, and have main loop of the application
     * @throws IOException IO exception
     * @throws ClassNotFoundException Class not found
     */
    public void start() throws IOException, ClassNotFoundException {
        CinemaTicketDispenser dispenser = new CinemaTicketDispenser();
        UrjcBankServer bank = new UrjcBankServer();
        MainMenu menu = new MainMenu(dispenser, this);
        File idiomsDir = new File("./src/resources/idioms/");
        // searches for idioms and collect them into HashSet<String> idiomHashSet
        for (File idiomFile: Objects.requireNonNull(idiomsDir.listFiles())) {
            // cinemadispenser_idiom.properties -> idiom.properties
            final String headclear = idiomFile.toString().substring(idiomFile.toString().indexOf("_") + 1).trim();
            // idiom.properties -> idiom
            this.idiomArrayList.add(headclear.substring(0, headclear.length() - 11));
        }
        // sets default language
        this.setIdiom(this.getIdiomArrayList().get(0));
        // main loop
        while (true) {
            for (Operation operation: menu.getOperationList()) {
                if (Objects.equals(operation.getTitle(), "MainMenu")) {
                    operation.doOperation();
                }
            }
        }
    }

    /**
     * Sets idiom if exists in ArrayList idiomArrayList
     * @param idiom String idiom
     */
    public void setIdiom(String idiom) {
        this.idiom = idiom;
    }

    /**
     * Gets ArrayList idiomArrayList
     * @return ArrayList idiomArrayList
     */
    public ArrayList<String> getIdiomArrayList() {
        return this.idiomArrayList;
    }

    /**
     * Gets ResourceBundle idiomBundle
     * @return ResourceBundle idiomBundle
     */
    public ResourceBundle getIdiomBundle() {
        return java.util.ResourceBundle.getBundle("resources/idioms/cinemadispenser_" + this.idiom);
    }

}
