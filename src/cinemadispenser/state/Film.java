package cinemadispenser.state;

import static java.time.temporal.ChronoUnit.HOURS;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Film class implements Serializable
 */
public class Film implements Serializable {

    /**
     * Film name
     */
    private String name;
    /**
     * Film description
     */
    private String description;
    /**
     * Film duration
     */
    private int duration;
    /**
     * Film price
     */
    private int price;
    /**
     * Film poster path
     */
    private String poster;
    /**
     * Film Session List
     */
    private final List<Session> sessionList = new ArrayList<>();

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
                this.name = cleanLine(line);
            } else if (line.startsWith("Description: ")) {
                this.description = cleanLine(line);
            } else if (line.startsWith("Sessions: ")) {
                String sessions = cleanLine(line);
                this.duration = (int) HOURS.between(
                        // first session
                        LocalTime.parse(sessions.substring(0, 5), DateTimeFormatter.ofPattern("HH:mm")),
                        // second session
                        LocalTime.parse(sessions.substring(6, 11), DateTimeFormatter.ofPattern("HH:mm"))
                );
                // while for getting sessions from sessions string & converts them to LocalTime format to create new Session
                while (!sessions.isEmpty()) {
                    this.sessionList.add(new Session(LocalTime.parse(sessions.substring(0, 5), DateTimeFormatter.ofPattern("HH:mm"))));
                    sessions = sessions.substring(sessions.indexOf(":") + 3).trim();
                }
            } else if (line.startsWith("Poster: ")) {
                this.poster = new File("./src/resources/movies/images/" + cleanLine(line)).toString();
            } else if (line.startsWith("Price: ")) {
                for (char ch : line.toCharArray()) {
                    if (Character.isDigit(ch)) {
                        this.price = Character.getNumericValue(ch);
                    }
                }
            }
        }
    }

    /**
     * Cleans line until : and clears space after :
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
        return this.name;
    }

    /**
     * Gets String description
     * @return String description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Gets int duration
     * @return int duration
     */
    public int getDuration() {
        return this.duration;
    }

    /**
     * Gets int price
     * @return int price
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Gets File poster
     * @return File poster
     */
    public String getPoster() {
        return this.poster;
    }

    /**
     * Gets List SessionList
     * @return List SessionList
     */
    public List<Session> getSessionList() {
        return this.sessionList;
    }

}
