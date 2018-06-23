package twao.loaders;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import twao.Player;
import twao.Village;

import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AllyLoader {
    private final HashMap<String, Player> players = new HashMap<>();
    private final List<Village> villages = new LinkedList<>();

    private final Pattern coordinatesPattern = Pattern.compile("\\(\\d{3}\\|\\d{3}\\)\\sK\\d{1,3}"); // (XXX|YYY) K..
    private final Pattern xyPattern = Pattern.compile("\\d{3}");


    public AllyLoader(String filePath) throws Exception {
        Reader in = new FileReader(filePath);
        Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);

        String name, rawVillages, tmp;
        Matcher matcher, innerMatcher;
        int noblesAvailable, x, y;

        for (CSVRecord record : records) {
            name = record.get("nick");
            noblesAvailable = Integer.parseInt(record.get("nobleAvailable"));

            if (players.get(name) == null) {
                players.put(name, new Player(name, noblesAvailable));
            }

            rawVillages = record.get("villages");
            matcher = coordinatesPattern.matcher(rawVillages);

            while (matcher.find()) {
                tmp = matcher.group();
                innerMatcher = xyPattern.matcher(tmp);

                innerMatcher.find();
                x = Integer.parseInt(innerMatcher.group());
                innerMatcher.find();
                y = Integer.parseInt(innerMatcher.group());

                villages.add(new Village(name, x, y));
                players.get(name).increaseVillagesAmount();
            }
        }
    }

    private void removeDuplicates() {
        HashSet<String> knownVillages = new HashSet<>();
        List<Village> duplicates = new LinkedList<>();

        for (Village vil : villages) {
            if ( knownVillages.contains(vil.toString()) ) {
                duplicates.add(vil);
            }
            else {
                knownVillages.add(vil.toString());
            }
        }

        villages.removeAll(duplicates);
    }

    /**
     * -----------------------------------------------------------
     * getters and setters
     * -----------------------------------------------------------
     */

    public HashMap<String, Player> getPlayers() { return players; }

    public List<Village> getVillages() {
        removeDuplicates();
        return villages;
    }
}
