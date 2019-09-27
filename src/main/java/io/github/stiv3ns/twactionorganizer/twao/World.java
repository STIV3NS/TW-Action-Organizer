package io.github.stiv3ns.twactionorganizer.twao;

import org.w3c.dom.Document;

import io.github.stiv3ns.twactionorganizer.twao.utils.exceptions.BadDomainException;
import io.github.stiv3ns.twactionorganizer.twao.utils.exceptions.VillageNotFoundException;
import io.github.stiv3ns.twactionorganizer.twao.villages.Village;

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
    private final int    maxNobleRange;
    private final int    nightBonusEndHour;
    private String villagesRawText;

    public World(String domain) throws BadDomainException {
        if ( !domain.startsWith("https://") ) {
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
        } catch (Exception e) {
            throw new BadDomainException();
        }
    }

    public int fetchVillageID(Village vil) throws VillageNotFoundException {
        Pattern pattern = Pattern.compile(String.format("^.*,%d,%d,.*$", vil.getX(), vil.getY()), Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(villagesRawText);

        if (matcher.find()) {
            /* village.txt::village repr: id,x,y,(not important data) */
            return Integer.parseInt(matcher.group().split(",")[0]);
        } else {
            throw new VillageNotFoundException();
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


    @Override
    public String toString() {
        return String.format("domain: %s | speed: %.2f (%.2f * %.2f) | max noble distance: %d",
                domain, getSpeed(), worldSPeed, unitSpeed, maxNobleRange);
    }
    
    public String getDomain()         { return domain; }
    public int getMaxNobleRange()     { return maxNobleRange; }
    public int getNightBonusEndHour() { return nightBonusEndHour; }
    public double getSpeed()          { return worldSPeed*unitSpeed; }
}
