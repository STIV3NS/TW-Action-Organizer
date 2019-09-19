package twao.parsers;

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
import java.util.stream.Collectors;

/**
 * Used to parse file with player questionnaires.
 *
 * It must have [#nicknameHeader], [#noblesHeader] and [#offsHeader]
 * before [#parse()] method usage. To set it use [#setNicknameHeader(String)], [#setNoblesHeader(String)]
 * and {[#setOffsHeader(String)] methods.
 */
public class AllyParser {
    private final List<Player>      players = new ArrayList<>();
    private List<AllyVillage>       villages = new ArrayList<>();

    private String nicknameHeader;
    private String noblesHeader;
    private String offsHeader;

    private Iterable<CSVRecord> records;
    private Map<String, Player> knownPlayers = new HashMap<>();

    public AllyParser(String filePath) throws IOException {
        Reader in = new FileReader(filePath);
        records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);
    }

    /**
     * @param nicknameHeader    Column with that header will be marked as nicknames
     */
    public void setNicknameHeader(String nicknameHeader) { this.nicknameHeader = nicknameHeader; }
    /**
     * @param noblesHeader      Column with that header will be marked as number of nobles
     */
    public void setNoblesHeader(String noblesHeader)       { this.noblesHeader = noblesHeader; }

    /**
     * @param offsHeader        Column with that header will be marked as list of offs
     */
    public void setOffsHeader(String offsHeader) { this.offsHeader = offsHeader; }

    /**
     * Parses player questionnaires into object context.
     *
     * @throws UnspecifiedKeyException Thrown if [#nicknameHeader], [#noblesHeader] or [#offsHeader] is not set.
     */
    public void parse() throws UnspecifiedKeyException {
        if (nicknameHeader == null || noblesHeader == null || offsHeader == null) {
            throw new UnspecifiedKeyException();
        } else {
            for (CSVRecord record : records) {
                Player player = parsePlayer(record);
                parseVillages(player, record);
            }

            removeDuplicates();
        }
    }

    /**
     * Returns list of all players who completed the questionnaire.
     *
     * @return [List<Player>]
     */
    public List<Player> getPlayers()       { return players; }

    /**
     * Returns List of all villages that belong to tribe.
     *
     * @return [List<AllyVillage>]
     */
    public List<AllyVillage> getVillages() { return villages; }

    private Player parsePlayer(CSVRecord record) {
        String nickname = record.get(nicknameHeader);
        int numberOfNobles = Integer.parseInt(record.get(noblesHeader));

        Player player;
        if (knownPlayers.get(nickname) == null) {
            player = new Player(nickname, numberOfNobles);
            knownPlayers.put(nickname, player);
            players.add(player);
        } else {
            player = knownPlayers.get(nickname);
        }

        return player;
    }

    private void parseVillages(Player player, CSVRecord record) {
        Pattern coordinatesPattern = Pattern.compile("\\(\\d{3}\\|\\d{3}\\) [a-zA-Z]\\d{2}\\s+(\\d|\\()");
        Pattern xyPattern = Pattern.compile("\\d{3}");

        String rawVillages = record.get(offsHeader);

        Matcher matcher = coordinatesPattern.matcher(rawVillages);

        while (matcher.find()) {
            String tmp = matcher.group();
            Matcher innerMatcher = xyPattern.matcher(tmp);

            innerMatcher.find();
            int x = Integer.parseInt(innerMatcher.group());
            innerMatcher.find();
            int y = Integer.parseInt(innerMatcher.group());

            villages.add(new AllyVillage(x, y, player));
            player.increaseNumberOfVillages();
        }
    }

    private void removeDuplicates() {
        HashSet<String> knownVillages = new HashSet<>();

        villages.forEach( v ->{
            if (knownVillages.contains(v.toString())) {
                v.getOwner().decreaseNumberOfVillaes();
            } else {
                knownVillages.add(v.toString());
            }
        });

        villages = villages
                .stream()
                .distinct()
                .collect(Collectors.toList());
    }
}
