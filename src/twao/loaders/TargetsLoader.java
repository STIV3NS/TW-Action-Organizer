package twao.loaders;

import twao.villages.TargetVillage;

import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;

public class TargetsLoader {
    public TargetsLoader(String filePath, int attacksPerVillage, List<TargetVillage> outputList) throws FileNotFoundException {
        Pattern coordinatesPattern = Pattern.compile("\\d{3}\\|\\d{3}");
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
