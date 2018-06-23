package twao;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class World {
    private final String domain;
    private final double worldSPeed;
    private final double unitSpeed;
    private final int maxNobleDistance;
    private final int nightEndHour;
    private String villagesList;

    public World(String domain) throws Exception {
        this.domain = "https://" + domain;

        URL url = new URL(this.getDomain() +"/interface.php?func=get_config");

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(url.openStream());

        doc.getDocumentElement().normalize();

        worldSPeed = Double.parseDouble(doc.getElementsByTagName("speed").item(0).getTextContent());
        unitSpeed = Double.parseDouble(doc.getElementsByTagName("unit_speed").item(0).getTextContent());
        maxNobleDistance = Integer.parseInt(doc.getElementsByTagName("max_dist").item(0).getTextContent());
        nightEndHour = Integer.parseInt(doc.getElementsByTagName("end_hour").item(0).getTextContent());
    }

    public void loadVillageList() {
        try {
            URL url = new URL(this.getDomain() + "/map/village.txt");
            Scanner sc = new Scanner(url.openStream());
            this.villagesList = sc.useDelimiter("\\Z").next();
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getVillageId(Village vil) throws Exception {
        Pattern pattern = Pattern.compile(String.format("^.*,%d,%d,.*$", vil.getX(), vil.getY()), Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(villagesList);

        if (matcher.find()) {
            int id = Integer.parseInt(matcher.group().split(",")[0]);
            return id;
        } else {
            throw new Exception();
        }
    }

    /**
     * -----------------------------------------------------------
     * getters and setters
     * -----------------------------------------------------------
     */

    public String getDomain() { return domain; }

    public int getMaxNobleDistance() { return maxNobleDistance; }

    public int getNightEndHour() { return nightEndHour; }

    public double getSpeed() { return worldSPeed*unitSpeed; }

    public String getWorldInfo() {
        return String.format("domain: %s | speed: %sx(%s*%s) | max snob distance: %s", domain, getSpeed(), worldSPeed, unitSpeed, maxNobleDistance);
    }
}
