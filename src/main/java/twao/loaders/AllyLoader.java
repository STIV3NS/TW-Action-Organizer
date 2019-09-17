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

/**
 * Used to parse file with player questionnaires.
 *
 * It must have {@link #nicknameKey}, {@link #nobleKey} and {@link #villagesKey}
 * before {@link #load()} method usage. To set it use {@link #setNicknameKey(String)}, {@link #setNobleKey(String)}
 * and {@link #setVillagesKey(String)} methods.
 */
public class AllyLoader {
    private final List<Player>      players = new LinkedList<>();
    private final List<AllyVillage> villages = new LinkedList<>();

    private String nicknameKey;
    private String nobleKey;
    private String villagesKey;

    private Iterable<CSVRecord> records;

    /**
     * Initializes AllyLoader to parse {@code filePath}.
     *
     * @param filePath      Path to CSV file containing questionnaires
     * @throws IOException
     */
    public AllyLoader(String filePath) throws IOException {
        Reader in = new FileReader(filePath);
        records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
    }

    /**
     * Loads player questionnaires into object context.
     *
     * @throws UnspecifiedKeyException Thrown if {@link #nicknameKey}, {@link #nobleKey} or {@link #villagesKey} is not set.
     */
    public void load() throws UnspecifiedKeyException {
        if (nicknameKey == null || nobleKey == null || villagesKey == null) {
            throw new UnspecifiedKeyException();
        }

        final Pattern coordinatesPattern = Pattern.compile("\\(\\d{3}\\|\\d{3}\\)"); // (XXX|YYY)
        final Pattern xyPattern = Pattern.compile("\\d{3}");

        Player player;
        String nickname, rawVillages, tmp;
        Matcher matcher, innerMatcher;
        int x, y, numberOfNobles;

        final Map<String, Player> knownPlayers = new HashMap<>();

        for (CSVRecord record : records) {
            nickname = record.get(nicknameKey);
            numberOfNobles = Integer.parseInt(record.get(nobleKey));

            if (knownPlayers.get(nickname) == null) {
               player = new Player(nickname, numberOfNobles);
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
     * @param nicknameKey   Column with header content equal key will be used to load nicknames from CSV
     */
    public void setNicknameKey(String nicknameKey) { this.nicknameKey = nicknameKey; }
    /**
     * @param nobleKey      Column with header content equal key will be used to load number of nobles from CSV
     */
    public void setNobleKey(String nobleKey)       { this.nobleKey = nobleKey; }

    /**
     * @param villagesKey   Column with header content equal key will be used to load village list from CSV
     */
    public void setVillagesKey(String villagesKey) { this.villagesKey = villagesKey; }

    /**
     * Returns List of all villages that belong to tribe.
     *
     * @return {@code List<AllyVillage>}
     */
    public List<AllyVillage> getVillages() { return villages; }

    /**
     * Returns list of all players who completed the questionnaire.
     *
     * @return {@code List<Player>}
     */
    public List<Player> getPlayers()       { return players; }
}
