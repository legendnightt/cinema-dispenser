package cinemadispenser.operations.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Film {

    private int theater;
    private String name;
    private String description;
    private String poster;
    private int duration;

    public Film(File file) throws FileNotFoundException {
        this.setInfo(file);
    }

    private String cleanLine(String line) {
        return line.substring(line.indexOf(":") + 1).trim();
    }

    private void setInfo(File file) throws FileNotFoundException {

        FileReader filmFile = new FileReader(file);
        Scanner sc = new Scanner(filmFile);

        System.out.println("Going to start generating a film:");

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("Theatre: ")) {
                for (char ch: line.toCharArray()) {
                    if (Character.isDigit(ch)) {
                        theater = Character.getNumericValue(ch);
                        System.out.println("Theater: " + theater);
                    }
                }
            } else if (line.startsWith("Title: ")) {
                name = cleanLine(line);
                System.out.println("Title: " + name);
            } else if (line.startsWith("Description: ")) {
                description = cleanLine(line);
                System.out.println("Description: " + description);
            } else if (line.startsWith("Sessions: ")) {
                String sessions = cleanLine(line);
                System.out.println("Sessions: " + sessions);
                SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                String firstSession = sc.findInLine("(..:..)");
                System.out.println(firstSession);
            } else if (line.startsWith("Poster: ")) {
                poster = cleanLine(line);
                System.out.println("Poster: " + poster);
            }
        }

    }

}
