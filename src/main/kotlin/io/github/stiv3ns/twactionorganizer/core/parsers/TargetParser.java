package io.github.stiv3ns.twactionorganizer.core.parsers;

import io.github.stiv3ns.twactionorganizer.core.villages.TargetVillage;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;

public class TargetParser {
    /** Parses [filePath] and loads its content into List<TargetVillage>[outputList] 
      * with number of attacks equal [attacksPerVillage]
      */
    public static void parse(
            String filePath,
            int attacksPerVillage,
            List<TargetVillage> outputList
    ) throws FileNotFoundException {

        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            parseLine(line, attacksPerVillage, outputList);
        }

        scanner.close();
    }


    private static void parseLine(String line, int attacksPerVillage, List<TargetVillage> outputList) {
        Pattern coordinatesPattern = Pattern.compile("\\d{3}\\|\\d{3}");
        Matcher matcher = coordinatesPattern.matcher(line);

        while (matcher.find()) {
            String[] coordinates = matcher.group().split("\\|");
            addToOutputList(coordinates, attacksPerVillage, outputList);
        }
    }

    private static void addToOutputList(String[] coordinates, int attacksPerVillage, List<TargetVillage> outputList) {
        outputList.add(new TargetVillage(Integer.parseInt(coordinates[0]),
                                         Integer.parseInt(coordinates[1]),
                                         attacksPerVillage));
    }
}
