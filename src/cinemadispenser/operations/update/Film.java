package cinemadispenser.operations.update;

import static java.time.temporal.ChronoUnit.HOURS;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Film {

    private String name;
    private String description;
    private final List<LocalTime> sessionList = new ArrayList<>();
    private int duration;
    private File poster;

    public Film(File file) throws FileNotFoundException {
        setInfo(file);
    }

    private String cleanLine(String line) {
        return line.substring(line.indexOf(":") + 1).trim();
    }

    private void setInfo(File file) throws FileNotFoundException {
        Scanner sc = new Scanner(new FileReader(file));
        System.out.println("Going to start generating a film:");
        // while for getting movie config stuff
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("Title: ")) {
                name = cleanLine(line);
                System.out.println("Title: " + name);
            } else if (line.startsWith("Description: ")) {
                description = cleanLine(line);
                System.out.println("Description: " + description);
            } else if (line.startsWith("Sessions: ")) {
                String sessions = cleanLine(line);
                System.out.println("Sessions: " + sessions);
                // while for getting sessions from sessions string & converts them to LocalTime format inside sessionList
                while (!sessions.isEmpty()) {
                    sessionList.add(LocalTime.parse(sessions.substring(0, 5), DateTimeFormatter.ofPattern("HH:mm")));
                    sessions = sessions.substring(sessions.indexOf(":") + 3).trim();
                }
                System.out.println(sessionList);
                duration = (int) HOURS.between(sessionList.get(0), sessionList.get(1));
                System.out.println("Duration: " + duration + "h");
            } else if (line.startsWith("Poster: ")) {
                poster = new File("./src/resources/movies/images/" + cleanLine(line));
                System.out.println("Poster: " + poster);
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public File getPoster() {
        return poster;
    }

    public List<LocalTime> getSessionList() {
        return sessionList;
    }

    public int getDuration() {
        return duration;
    }

}
