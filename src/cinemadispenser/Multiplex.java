package cinemadispenser;

import cinemadispenser.operations.MainMenu;
import sienens.CinemaTicketDispenser;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Multiplex class
 */
public class Multiplex {

    /**
     * MainMenu menu
     */
    private MainMenu menu;
    /**
     * Idioms String ArrayList
     */
    private final ArrayList<String> idiomArrayList = new ArrayList<>();
    /**
     * Current Idiom working
     */
    private String idiom;
    /**
     * int purchasePrice MovieTicketSale
     */
    private int purchasePrice;
    /**
     * boolean purchaseStatus PerformPayment
     */
    private boolean purchaseStatus = false;

    /**
     * Starts all, and have main loop of the application
     */
    public void start() {
        CinemaTicketDispenser dispenser = new CinemaTicketDispenser();
        this.menu = new MainMenu(dispenser, this);
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
            for (Operation operation: this.menu.getOperationList()) {
                if (Objects.equals(operation.getTitle(), "MainMenu")) { operation.doOperation(); }
            }
            // checks if purchase finished, so changed language
            if (this.purchaseStatus) {
                this.setIdiom(this.getIdiomArrayList().get(0));
                this.purchaseStatus = false;
            }
        }
    }

    /**
     * Gets MainMenu menu
     * @return MainMenu menu
     */
    public MainMenu getMenu() {
        return this.menu;
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

    /**
     * Sets int purchasePrice MovieTicketSale
     * @param purchasePrice int purchasePrice MovieTicketSale
     */
    public void setPurchasePrice(int purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    /**
     * Gets int purchasePrice MovieTicketSale
     * @return int purchasePrice MovieTicketSale
     */
    public int getPurchasePrice() {
        return this.purchasePrice;
    }

    /**
     * Sets boolean purchaseStatus PerformPayment
     * @param purchaseStatus boolean purchaseStatus PerformPayment
     */
    public void setPurchaseStatus(boolean purchaseStatus) {
        this.purchaseStatus = purchaseStatus;
    }

    /**
     * Gets boolean purchaseStatus PerformPayment
     * @return boolean purchaseStatus PerformPayment
     */
    public boolean getPurchaseStatus() {
        return this.purchaseStatus;
    }

}
