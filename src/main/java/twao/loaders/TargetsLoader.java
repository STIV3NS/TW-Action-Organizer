package twao.loaders;

import twao.villages.TargetVillage;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;

public class TargetsLoader {
    /**
     * Parses [filePath] and loads its coordinates into [outputList]
     *
     * @param filePath                  Inputfile path
     * @param attacksPerVillage         How many attacks should be assigned to each village from the list
     * @param outputList                Destination List<TargetVillage>
     */
    public static void load(String filePath, int attacksPerVillage, List<TargetVillage> outputList) throws FileNotFoundException {
        Pattern coordinatesPattern = Pattern.compile("\\d{3}\\|\\d{3}"); // XXX|YYY
        Matcher matcher;

        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        String line;
        String[] coordinates;

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();

            matcher = coordinatesPattern.matcher(line);

            while (matcher.find()) {
                coordinates = matcher.group().split("\\|");
                outputList.add(new TargetVillage(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]), attacksPerVillage));
            }
        }

        scanner.close();
    }
}
