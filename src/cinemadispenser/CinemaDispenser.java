package cinemadispenser;

import java.io.IOException;

/**
 * CinemaDispenser class
 */
public class CinemaDispenser {

    /**
     * Main project entry
     * @param args String[]
     * @throws IOException IO exception
     * @throws ClassNotFoundException Class not found
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Multiplex multiplex = new Multiplex();
        multiplex.start();
    }
    
}
