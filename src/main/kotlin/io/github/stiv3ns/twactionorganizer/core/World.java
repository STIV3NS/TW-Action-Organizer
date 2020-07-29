package io.github.stiv3ns.twactionorganizer.core;

import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.BadDomainException;
import io.github.stiv3ns.twactionorganizer.core.utils.exceptions.VillageNotFoundException;
import io.github.stiv3ns.twactionorganizer.core.villages.Village;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class World {
    private final String domain;
    private final double worldSPeed;
    private final double unitSpeed;
    private final int maxNobleRange;
    private final int nightBonusEndHour;
    private String villagesRawText;
    private String playersRawText;

    public World(String domain) throws BadDomainException {
        if (!domain.startsWith("https://")) {
            domain = "https://" + domain;
        }
        this.domain = domain;

        try {
            Document doc = getXMLDocumentWithConfig(this.domain);

            worldSPeed = Double.parseDouble(doc.getElementsByTagName("speed").item(0).getTextContent());
            unitSpeed = Double.parseDouble(doc.getElementsByTagName("unit_speed").item(0).getTextContent());
            maxNobleRange = Integer.parseInt(doc.getElementsByTagName("max_dist").item(0).getTextContent());
            nightBonusEndHour = Integer.parseInt(doc.getElementsByTagName("end_hour").item(0).getTextContent());

            fetchVillagesRawText();
            fetchPlayersRawText();
        } catch (Exception e) {
            throw new BadDomainException();
        }
    }

    public int fetchVillageID(Village vil) throws VillageNotFoundException {
        return Integer.parseInt(fetchVillageEntity(vil).split(",")[0]);
    }

    public String fetchVillageOwner(Village vil) throws VillageNotFoundException {
        int ownerId = Integer.parseInt(fetchVillageEntity(vil).split(",")[4]);
        return fetchPlayerEntity(ownerId).split(",")[1];
    }

    private String fetchVillageEntity(Village vil) throws VillageNotFoundException {
        Pattern pattern = Pattern.compile(String.format("^.*,%d,%d,.*$", vil.getX(), vil.getY()), Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(villagesRawText);

        if (matcher.find()) {
            return matcher.group();
        } else {
            throw new VillageNotFoundException();
        }
    }

    private String fetchPlayerEntity(int playerId) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile(String.format("^%d,.*,.*,.*,.*,.*", playerId), Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(playersRawText);

        if (matcher.find()) {
            return matcher.group();
        } else {
            /* this shouldn't happen if function used properly. */
            throw new IllegalArgumentException();
        }
    }

    private Document getXMLDocumentWithConfig(String domain) throws Exception {
        URL url = new URL(domain + "/interface.php?func=get_config");

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(url.openStream());

        doc.getDocumentElement().normalize();

        return doc;
    }

    private void fetchVillagesRawText() throws IOException {
        URL url = new URL(domain + "/map/village.txt");
        Scanner sc = new Scanner(url.openStream());

        villagesRawText = sc.useDelimiter("\\Z").next();

        sc.close();
    }

    private void fetchPlayersRawText() throws IOException {
        URL url = new URL(domain + "/map/player.txt");
        Scanner sc = new Scanner(url.openStream());

        playersRawText = sc.useDelimiter("\\Z").next();

        sc.close();
    }


    @Override
    public String toString() {
        return String.format("domain: %s | speed: %.2f (%.2f * %.2f) | max noble distance: %d",
                domain, getSpeed(), worldSPeed, unitSpeed, maxNobleRange);
    }

    public String getDomain() {
        return domain;
    }

    public int getMaxNobleRange() {
        return maxNobleRange;
    }

    public int getNightBonusEndHour() {
        return nightBonusEndHour;
    }

    public double getSpeed() {
        return worldSPeed * unitSpeed;
    }
}
