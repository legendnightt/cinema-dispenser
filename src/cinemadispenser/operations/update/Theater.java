package cinemadispenser.operations.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Theater {

    private final int maxprice = 15;
    private final int minprice = 10;

    private int number;
    private int price;
    private Set<Seat> seatSet = new HashSet<>();
    private List<Film> filmList = new ArrayList<>();

    public Theater(File file) throws FileNotFoundException {
        setNumber(file);
        setPrice();
        generateSeatSet(file);
    }

    private void setNumber(File file) {
        for (char ch: file.toString().toCharArray()) {
            if (Character.isDigit(ch)) {
                number = Character.getNumericValue(ch);
            }
        }
    }

    private void setPrice() {
        price = (int) (Math.random() * (maxprice - minprice)) + minprice;
    }

    private void generateSeatSet(File file) throws FileNotFoundException {
        Scanner scrow = new Scanner(new FileReader(file));
        Scanner sc = new Scanner(new FileReader(file));
        int row = 0;
        int col = 1;
        System.out.println("Going to start generating seats for theater number: " + number);
        // while for getting total rows
        while (scrow.hasNextLine()) {
            scrow.nextLine();
            row += 1;
        }
        System.out.println("Number of rows: " + row);
        // while for generate seats with exact row & col
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            System.out.println(line);
            for (char ch: line.toCharArray()) {
                if (ch == '*') {
                    seatSet.add(new Seat(row, col));
                    System.out.println("Seat added in: " + row + " row, " + col + " col");
                }
                col += 1;
            }
            col = 1;
            row -= 1;
        }
    }

}
