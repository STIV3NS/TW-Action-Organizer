package twao;

import org.w3c.dom.Document;

import twao.exceptions.BadDomainException;
import twao.exceptions.VillageNotFoundException;
import twao.villages.Village;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class World {
    private final String    domain;
    private final double    worldSPeed;
    private final double    unitSpeed;
    private final int       maxNobleRange;
    private final int       nightBonusEndHour;
    private String          villagesList;

    public World(String domain) throws BadDomainException {
        this.domain = "https://" + domain;

        try {
            URL url = new URL(this.getDomain() + "/interface.php?func=get_config");

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(url.openStream());

            doc.getDocumentElement().normalize();

            worldSPeed = Double.parseDouble(doc.getElementsByTagName("speed").item(0).getTextContent());
            unitSpeed = Double.parseDouble(doc.getElementsByTagName("unit_speed").item(0).getTextContent());
            maxNobleRange = Integer.parseInt(doc.getElementsByTagName("max_dist").item(0).getTextContent());
            nightBonusEndHour = Integer.parseInt(doc.getElementsByTagName("end_hour").item(0).getTextContent());

            loadVillageList();
        } catch (Exception e) {
            throw new BadDomainException();
        }
    }

    private void loadVillageList() throws IOException {
        URL url = new URL(this.getDomain() + "/map/village.txt");
        Scanner sc = new Scanner(url.openStream());
        this.villagesList = sc.useDelimiter("\\Z").next();
        sc.close();
    }

    public int fetchVillageID(Village vil) throws VillageNotFoundException {
        Pattern pattern = Pattern.compile(String.format("^.*,%d,%d,.*$", vil.getX(), vil.getY()), Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(villagesList);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group().split(",")[0]);
        } else {
            throw new VillageNotFoundException();
        }
    }

    @Override
    public String toString() {
        return String.format("domain: %s | speed: %.2f (%.2f * %.2f) | max snob distance: %d",
                domain, getSpeed(), worldSPeed, unitSpeed, maxNobleRange);
    }
    public String getDomain()           { return domain; }
    public int getMaxNobleRange()       { return maxNobleRange; }
    public int getNightBonusEndHour()   { return nightBonusEndHour; }
    public double getSpeed()            { return worldSPeed*unitSpeed; }
}
