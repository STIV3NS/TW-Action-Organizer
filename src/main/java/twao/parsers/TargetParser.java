package twao.parsers;

import twao.villages.TargetVillage;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;

public class TargetParser {
    /** Parses [filePath] and loads its content into List<TargetVillage>[outputList] 
      * number of attacks equal [attacksPerVillage]
      */
    public static void parse(
            String filePath,
            int attacksPerVillage,
            List<TargetVillage> outputList) throws FileNotFoundException {

        File file = new File(filePath);
        Scanner scanner = new Scanner(file);

        Pattern coordinatesPattern = Pattern.compile("\\d{3}\\|\\d{3}");
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            Matcher matcher = coordinatesPattern.matcher(line);

            while (matcher.find()) {
                String[] coordinates = matcher.group().split("\\|");
                outputList.add(new TargetVillage(Integer.parseInt(coordinates[0]),
                                                 Integer.parseInt(coordinates[1]),
                                                 attacksPerVillage));
            }
        }

        scanner.close();
    }
}
