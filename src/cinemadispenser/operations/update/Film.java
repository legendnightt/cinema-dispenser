package cinemadispenser.operations.update;

import static java.time.temporal.ChronoUnit.HOURS;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Film {

    private String name;
    private String description;
    private int duration;
    private int price;
    private File poster;

    /**
     * Film builder
     * @param file movieFile
     * @throws FileNotFoundException if movieFile doesn't exist
     */
    public Film(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(new FileReader(file));
        // while for getting movie config stuff
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("Title: ")) {
                name = cleanLine(line);
            } else if (line.startsWith("Description: ")) {
                description = cleanLine(line);
            } else if (line.startsWith("Sessions: ")) {
                String sessions = cleanLine(line);
                duration = (int) HOURS.between(
                        // first session
                        LocalTime.parse(sessions.substring(0, 5), DateTimeFormatter.ofPattern("HH:mm")),
                        // second session
                        LocalTime.parse(sessions.substring(6, 11), DateTimeFormatter.ofPattern("HH:mm"))
                );
            } else if (line.startsWith("Poster: ")) {
                poster = new File("./src/resources/movies/images/" + cleanLine(line));
            } else if (line.startsWith("Price: ")) {
                for (char ch : line.toCharArray()) {
                    if (Character.isDigit(ch)) {
                        price = Character.getNumericValue(ch);
                    }
                }
            }
        }
    }

    /**
     * Cleans line until : & clears space after :
     * @param line String
     * @return String cleaned line
     */
    private String cleanLine(String line) {
        return line.substring(line.indexOf(":") + 1).trim();
    }

    /**
     * Gets String name
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets String description
     * @return String description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets int duration
     * @return int duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Gets int price
     * @return int price
     */
    public int getPrice() {
        return price;
    }

    /**
     * Gets File poster
     * @return File poster
     */
    public File getPoster() {
        return poster;
    }

}
