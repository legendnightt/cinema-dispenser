package cinemadispenser.operations.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Theater implements Serializable {

    private int number;
    private int price;
    private int maxrows;
    private int maxcols;
    private final Set<Seat> seatSet = new HashSet<>();
    private final List<Film> filmList = new ArrayList<>();
    private final List<Session> sessionList = new ArrayList<>();

    /**
     * Theater builder
     * @param theaterFile File theaterFile
     * @param moviesFiles File moviesFiles
     * @throws FileNotFoundException if theaterFile or moviesFiles doesn't exist
     */
    public Theater(File theaterFile, File[] moviesFiles) throws FileNotFoundException {
        setNumber(theaterFile);
        setPrice();
        generateSeatSet(theaterFile);
        generateFilmSessionList(moviesFiles);
    }

    /**
     * Sets theater number, searching the corresponding one in the txt file
     * @param file File theaterFile
     */
    private void setNumber(File file) {
        for (char ch: file.toString().toCharArray()) {
            if (Character.isDigit(ch)) {
                number = Character.getNumericValue(ch);
            }
        }
    }

    /**
     * Sets random price between minprice & maxprice to price
     */
    private void setPrice() {
        int minprice = 10;
        int maxprice = 15;
        price = (int) (Math.random() * (maxprice - minprice)) + minprice;
    }

    /**
     * Creates Seat Set with theaterFile directory, also maxrows & maxcols
     * @param file File theaterFile
     * @throws FileNotFoundException if theaterFile doesn't exist
     */
    private void generateSeatSet(File file) throws FileNotFoundException {
        Scanner scrow = new Scanner(new FileReader(file));
        Scanner sc = new Scanner(new FileReader(file));
        int row = 0;
        int col = 1;
        // while for getting max rows
        while (scrow.hasNextLine()) {
            scrow.nextLine();
            row += 1;
        }
        maxrows = row;
        // while for generating seats with exact row & col
        maxcols = 1;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            for (char ch: line.toCharArray()) {
                if (ch == '*') {
                    seatSet.add(new Seat(row, col));
                    if (maxcols < col) {
                        maxcols = col;
                    }
                }
                col += 1;
            }
            col = 1;
            row -= 1;
        }
    }

    /**
     * Creates corresponding films to Theater & adds them into filmList,
     * same with sessions with sessionList
     * @param files File moviesFiles
     * @throws FileNotFoundException if moviesFiles doesn't exist
     */
    private void generateFilmSessionList (File[] files) throws FileNotFoundException {
        for (File file: files) {
            Scanner sc = new Scanner(new FileReader(file));
            // while for searching theater films
            boolean found = false;
            // while for getting Theatre Films & Sessions
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("Theatre: ")) {
                    for (char ch : line.toCharArray()) {
                        if (Character.isDigit(ch) && number == Character.getNumericValue(ch)) {
                            filmList.add(new Film(file));
                            found = true;
                        } else {
                            found = false;
                        }
                    }
                } else if (line.startsWith("Sessions: ") && found) {
                    String sessions = line.substring(line.indexOf(":") + 1).trim();
                    // while for getting sessions from sessions string & converts them to LocalTime format to create new Session
                    while (!sessions.isEmpty()) {
                        sessionList.add(new Session(LocalTime.parse(sessions.substring(0, 5), DateTimeFormatter.ofPattern("HH:mm")), seatSet));
                        sessions = sessions.substring(sessions.indexOf(":") + 3).trim();
                    }
                }
            }
        }
    }

    /**
     * Gets int number
     * @return int number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gets int price
     * @return int price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Gets int maxrows
     * @return int maxrows
     */
    public int getMaxRows() {
        return maxrows;
    }

    /**
     * Gets max columns
     * @return int maxcols
     */
    public int getMaxCols() {
        return maxcols;
    }

    /**
     * Gets Set<Seat> seatSet
     * @return Set<Seat> seatSet
     */
    public Set<Seat> getSeatSeat() {
        return seatSet;
    }

    /**
     * Gets List<Film> filmList
     * @return List<Film> filmList
     */
    public List<Film> getFilmList() {
        return filmList;
    }

    /**
     * Gets List<Session> sessionList
     * @return List<Session> sessionList
     */
    public List<Session> getSessionList() {
        return sessionList;
    }

}
