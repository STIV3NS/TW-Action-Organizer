package twao.loaders;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import twao.Player;
import twao.exceptions.UnspecifiedKeyException;
import twao.villages.AllyVillage;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AllyLoader {
    private final List<Player> players = new LinkedList<>();
    private final List<AllyVillage> villages = new LinkedList<>();

    private String nicknameKey;
    private String nobleKey;
    private String villagesKey;

    private Iterable<CSVRecord> records;

    public AllyLoader(String filePath) throws IOException {
        Reader in = new FileReader(filePath);
        records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
    }

    public void load() throws UnspecifiedKeyException {
        if (nicknameKey == null || nobleKey == null || villagesKey == null) {
            throw new UnspecifiedKeyException();
        }

        final Pattern coordinatesPattern = Pattern.compile("\\(\\d{3}\\|\\d{3}\\)\\sK\\d{1,3}"); // (XXX|YYY) K..
        final Pattern xyPattern = Pattern.compile("\\d{3}");

        Player player;
        String nickname, rawVillages, tmp;
        Matcher matcher, innerMatcher;
        int x, y, noblesAvailable;

        final Map<String, Player> knownPlayers = new HashMap<>();

        for (CSVRecord record : records) {
            nickname = record.get(nicknameKey);
            noblesAvailable = Integer.parseInt(record.get(nobleKey));

            if (knownPlayers.get(nickname) == null) {
                player = new Player(nickname, noblesAvailable);
                knownPlayers.put(nickname, player);
                players.add(player);
            } else {
                player = knownPlayers.get(nickname);
            }

            rawVillages = record.get(villagesKey);
            matcher = coordinatesPattern.matcher(rawVillages);

            while (matcher.find()) {
                tmp = matcher.group();
                innerMatcher = xyPattern.matcher(tmp);

                innerMatcher.find();
                x = Integer.parseInt(innerMatcher.group());
                innerMatcher.find();
                y = Integer.parseInt(innerMatcher.group());

                villages.add(new AllyVillage(x, y, player));
                player.increaseNumberOfVillages();
            }
        }

        removeDuplicates();
    }

    private void removeDuplicates() {
        HashSet<String> knownVillages = new HashSet<>();
        List<AllyVillage> duplicates = new LinkedList<>();

        for (AllyVillage vil : villages) {
            if ( knownVillages.contains(vil.toString()) ) {
                duplicates.add(vil);
                vil.getOwner().decreaseNumberOfVillaes();
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

    public void setNicknameKey(String nicknameKey) {
        this.nicknameKey = nicknameKey;
    }

    public void setNobleKey(String nobleKey) {
        this.nobleKey = nobleKey;
    }

    public void setVillagesKey(String villagesKey) {
        this.villagesKey = villagesKey;
    }

    public List<AllyVillage> getVillages() { return villages; }

    public List<Player> getPlayers() { return players; }
}
