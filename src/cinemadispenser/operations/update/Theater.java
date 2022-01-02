package cinemadispenser.operations.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Theater {

    private int number;
    private int price;
    private int maxrows;
    private int maxcols;
    private final Set<Seat> seatSet = new HashSet<>();
    private final List<Film> filmList = new ArrayList<>();
    private final List<Session> sessionList = new ArrayList<>();

    public Theater(File theaterFile, File[] moviesFiles) throws FileNotFoundException {
        setNumber(theaterFile);
        setPrice();
        generateSeatSet(theaterFile);
        generateFilmList(moviesFiles);
    }

    /**
     * Sets theater number, searching the corresponding one in the txt file
     * @param file theaterFile
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
     * @param file theaterFile
     * @throws FileNotFoundException if theaterFile doesn't exist
     */
    private void generateSeatSet(File file) throws FileNotFoundException {
        Scanner scrow = new Scanner(new FileReader(file));
        Scanner sc = new Scanner(new FileReader(file));
        int row = 0;
        int col = 1;
        System.out.println("Going to start generating seats for theater number: " + number);
        // while for getting max rows
        while (scrow.hasNextLine()) {
            scrow.nextLine();
            row += 1;
        }
        maxrows = row;
        System.out.println("Max rows: " + maxrows);
        // while for generating seats with exact row & col
        maxcols = 1;
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            System.out.println(line);
            for (char ch: line.toCharArray()) {
                if (ch == '*') {
                    seatSet.add(new Seat(row, col));
                    System.out.println("Seat added in: " + row + " row, " + col + " col");
                    if (maxcols < col) {
                        maxcols = col;
                    }
                }
                col += 1;
            }
            col = 1;
            row -= 1;
        }
        System.out.println("Max col: " + maxcols);
    }

    /**
     * Creates corresponding films to Theater & adds them into filmList
     * @param files moviesFiles
     * @throws FileNotFoundException if moviesFiles doesn't exist
     */
    private void generateFilmList (File[] files) throws FileNotFoundException {
        for (File file: files) {
            Scanner sc = new Scanner(new FileReader(file));
            // while for searching theater films
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("Theatre: ")) {
                    for (char ch : line.toCharArray()) {
                        if (Character.isDigit(ch) && number == Character.getNumericValue(ch)) {
                            System.out.println("Found film for theater: " + number);
                            filmList.add(new Film(file));
                        }
                    }
                }
            }
        }
    }

    public int getNumber() {
        return number;
    }

    public int getPrice() {
        return price;
    }

    public int getMaxRows() {
        return maxrows;
    }

    public int getMaxCols() {
        return maxcols;
    }

    public Set<Seat> getSeatSeat() {
        return seatSet;
    }

    public List<Film> getFilmList() {
        return filmList;
    }

}
