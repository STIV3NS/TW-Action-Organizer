package io.github.stiv3ns.twactionorganizer.pmsender;

import io.github.stiv3ns.twactionorganizer.twao.Player;
import io.github.stiv3ns.twactionorganizer.twao.VillageAssignment;
import io.github.stiv3ns.twactionorganizer.twao.World;
import io.github.stiv3ns.twactionorganizer.twao.villages.Village;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class PMFormatter {
    private final World         world;
    private final Calendar      dateOfArrival;

    private static final int    SECONDS_PER_MIN        = 60;
    private static final int    GROUP_SIZE             = 5;
    private static final String OPEN_SPOILER        = "[spoiler]\n";
    private static final String CLOSE_SPOILER       = "[/spoiler]\n\n\n";

    private static ResourceBundle bundle = ResourceBundle.getBundle("localization/pmformatter");

    private static String REQUIREMENTS_HEADER = bundle.getString("REQUIREMENTS_HEADER");
    private static String NOBLE_HEADER        = bundle.getString("NOBLE_HEADER");
    private static String OFF_HEADER          = bundle.getString("OFF_HEADER");
    private static String FAKE_HEADER         = bundle.getString("FAKE_HEADER");
    private static String FAKENOBLE_HEADER    = bundle.getString("FAKENOBLE_HEADER");
    private static String EXECUTION_TEXT      = bundle.getString("EXECUTION_TEXT");

    private enum Unit {
        RAM,
        NOBLE
    }

    private int previousDay = -1; //flag init
    private boolean nextDay = false; //flag init

    /**
     * @param world             Specific world to get information from (e.g. speed)
     * @param dateOfArrival     Concrete date that is meant to be "D-Day"
     */
    public PMFormatter(World world, Calendar dateOfArrival) {
        this.world = world;
        this.dateOfArrival = dateOfArrival;
    }

    /**
     * Generates a message intended for a given player.
     *
     * @param player    Given player
     * @return          Message with all assignments
     */
    public String generatePlayerMsg(Player player) {
        StringBuilder sbuilder = new StringBuilder();

        if (player.getNobleAssignmentsCopy().size() > 0 || player.getFakeNobleAssignmentsCopy().size() > 0) {
            sbuilder.append(REQUIREMENTS_HEADER);
            generateNobleRequirements(sbuilder, player);
        }

        if (player.getNobleAssignmentsCopy().size() > 0) {
            sortAssignmentsList(player.getNobleAssignmentsCopy());

            sbuilder.append(NOBLE_HEADER);
            generateCommandsList(sbuilder, player.getNobleAssignmentsCopy(), getUnitSpeed(Unit.NOBLE));
        }

        if (player.getOffAssignmentsCopy().size() > 0) {
            sortAssignmentsList(player.getOffAssignmentsCopy());

            sbuilder.append(OFF_HEADER);
            generateCommandsList(sbuilder, player.getOffAssignmentsCopy(), getUnitSpeed(Unit.RAM));
        }

        if (player.getFakeAssignmentsCopy().size() > 0) {
            sortAssignmentsList(player.getFakeAssignmentsCopy());

            sbuilder.append(FAKE_HEADER);
            generateCommandsList(sbuilder, player.getFakeAssignmentsCopy(), getUnitSpeed(Unit.RAM));
        }

        if (player.getFakeNobleAssignmentsCopy().size() > 0) {
            sortAssignmentsList(player.getFakeNobleAssignmentsCopy());

            sbuilder.append(FAKENOBLE_HEADER);
            generateCommandsList(sbuilder, player.getFakeNobleAssignmentsCopy(), getUnitSpeed(Unit.NOBLE));
        }

        return sbuilder.toString();
    }

    private void generateNobleRequirements(StringBuilder sbuilder, Player player) {
        HashMap<Village, Integer> requirements = new HashMap<>();

        List<VillageAssignment> nobleAssignments = new LinkedList<>();
        nobleAssignments.addAll(player.getNobleAssignmentsCopy());
        nobleAssignments.addAll(player.getFakeNobleAssignmentsCopy());

        sbuilder.append(OPEN_SPOILER);

        int previousValue;
        for (VillageAssignment a : nobleAssignments) {
            if (requirements.get(a.getDeparture()) == null) {
                requirements.put(a.getDeparture(), 1);
            } else {
                previousValue = requirements.get(a.getDeparture());
                requirements.put(a.getDeparture(), ++previousValue);
            }
        }

        // {1: [Village, Village], 2: [], 3: [], ... }
        HashMap<Integer, List<Village>> sortedMap = sortRequirements(requirements);

        int iterator = 0;
        for (int value : sortedMap.keySet()) {
            for (Village vil : sortedMap.get(value)) {
                sbuilder.append(String.format("%d || %s\n", value, vil.toString()));

                if (++iterator % GROUP_SIZE == 0) {
                    sbuilder.append("\n\n");
                }
            }
        }

        sbuilder.append(CLOSE_SPOILER);
    }

    private void generateCommandsList(StringBuilder sbuilder, List<VillageAssignment> assignmentsList, int speed) {
        String departureTime, possibleNewLine, assignment, domain;
        int counter = 0, departureId, destinationId;

        sbuilder.append(OPEN_SPOILER);

        for (VillageAssignment a : assignmentsList) {
            departureTime = getDepartureTime(a.getSquaredDistance(), speed);

            possibleNewLine = nextDay ? "\n\n\n" : "";
            assignment = a.toString();
            domain = world.getDomain();
            departureId = a.getDeparture().getId();
            destinationId = a.getDestination().getId();

            sbuilder.append( String.format("%s%d. %s %s\n", possibleNewLine, ++counter, departureTime, assignment) );

            sbuilder.append( String.format("[url=%s/game.php?village=%d&screen=place&target=%d]%s[/url]\n", domain, departureId, destinationId, EXECUTION_TEXT) );
        }

        sbuilder.append(CLOSE_SPOILER);
    }
    
    private String getDepartureTime(double squaredDistance, int standardTimePerUnit) {

        Calendar departureTime = (Calendar) dateOfArrival.clone();

        double distance = Math.sqrt(squaredDistance);
        double unitSpeed = standardTimePerUnit / world.getSpeed();
        int travelTime = (int) ((distance * unitSpeed) * SECONDS_PER_MIN );

        departureTime.add(Calendar.SECOND, -travelTime);

        int dayOfMonth = departureTime.get(Calendar.DAY_OF_MONTH);

        if (dayOfMonth != previousDay) {
            nextDay = true;
        } else {
            nextDay = false;
        }
        previousDay = dayOfMonth;

        return new SimpleDateFormat("dd.MM | HH:mm:ss").format(departureTime.getTime());
    }

    private void sortAssignmentsList(List<VillageAssignment> list) {
        list.sort(Comparator.comparing(VillageAssignment::getSquaredDistance));
        Collections.reverse(list);
    }

    private HashMap<Integer, List<Village>> sortRequirements(HashMap<Village, Integer> map) {
        HashMap<Integer, List<Village>> sortedMap = new HashMap<>();

        int value;
        List<Village> vList;
        for (Village vil : map.keySet()) {
            value = map.get(vil);
            if (sortedMap.get(value) == null) {
                vList = new LinkedList<>();
                vList.add(vil);
                sortedMap.put(value, vList);
            } else {
                sortedMap.get(value).add(vil);
            }
        }

        return sortedMap;
    }

    private int getUnitSpeed(Unit type) {
        int speed;
        switch (type) {
            case RAM:
                speed = 30;
                break;
            case NOBLE:
                speed = 35;
                break;
            default:
                speed = 30;
                break;
        }
        return speed;
    }
}
