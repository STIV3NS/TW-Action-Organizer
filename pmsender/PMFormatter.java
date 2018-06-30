package pmsender;

import twao.Player;
import twao.VillageAssignment;
import twao.World;
import twao.villages.Village;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class PMFormatter {
    private final World world;

    private int previousDay = -1; //flag init
    private boolean nextDay = false; //flag init

    private static final int SECONDS_PER_MIN        = 60;
    private static final int GROUP_SIZE             = 5;
    private static final String OPEN_SPOILER        = "[spoiler]\n";
    private static final String CLOSE_SPOILER       = "[/spoiler]\n\n\n";

    ResourceBundle bundle = ResourceBundle.getBundle("resources.bundles.pmformatter");

    private final String REQUIREMENTS_HEADER = bundle.getString("REQUIREMENTS_HEADER");
    private final String NOBLE_HEADER        = bundle.getString("NOBLE_HEADER");
    private final String OFF_HEADER          = bundle.getString("OFF_HEADER");
    private final String FAKE_HEADER         = bundle.getString("FAKE_HEADER");
    private final String FAKENOBLE_HEADER    = bundle.getString("FAKENOBLE_HEADER");
    private final String EXECUTION_TEXT      = bundle.getString("EXECUTION_TEXT");

    private enum Unit {
        RAM,
        NOBLE
    }

    public PMFormatter(World world) {
        this.world = world;
    }

    public String get(Player player) {
        StringBuilder sbuilder = new StringBuilder();

        if (player.getNobleAssignments().size() > 0 || player.getFakeNobleAssignments().size() > 0) {
            sbuilder.append(REQUIREMENTS_HEADER);
            generateNobleRequirements(sbuilder, player);
        }

        if (player.getNobleAssignments().size() > 0) {
            sortAssignmentsList(player.getNobleAssignments());

            sbuilder.append(NOBLE_HEADER);
            generateCommandsList(sbuilder, player.getNobleAssignments(), getUnitSpeed(Unit.NOBLE));
        }

        if (player.getOffAssignments().size() > 0) {
            sortAssignmentsList(player.getOffAssignments());

            sbuilder.append(OFF_HEADER);
            generateCommandsList(sbuilder, player.getOffAssignments(), getUnitSpeed(Unit.RAM));
        }

        if (player.getFakeAssignments().size() > 0) {
            sortAssignmentsList(player.getFakeAssignments());

            sbuilder.append(FAKE_HEADER);
            generateCommandsList(sbuilder, player.getFakeAssignments(), getUnitSpeed(Unit.RAM));
        }

        if (player.getFakeNobleAssignments().size() > 0) {
            sortAssignmentsList(player.getFakeNobleAssignments());

            sbuilder.append(FAKENOBLE_HEADER);
            generateCommandsList(sbuilder, player.getFakeNobleAssignments(), getUnitSpeed(Unit.NOBLE));
        }

        return sbuilder.toString();
    }

    private void generateNobleRequirements(StringBuilder sbuilder, Player player) {
        HashMap<Village, Integer> requirements = new HashMap<>();

        List<VillageAssignment> nobleAssignments = new LinkedList<>();
        nobleAssignments.addAll(player.getNobleAssignments());
        nobleAssignments.addAll(player.getFakeNobleAssignments());

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

    //TODO implement custom dateOfArrival
    private String getDepartureTime(double squaredDistance, int standardTimePerUnit) {
        Calendar dateOfArrival = new GregorianCalendar(2018, Calendar.MAY, 21);
        dateOfArrival.add(Calendar.HOUR, 8);

        double distance = Math.sqrt(squaredDistance);
        double unitSpeed = standardTimePerUnit / world.getSpeed();
        int travelTime = (int) ((distance * unitSpeed) * SECONDS_PER_MIN );

        dateOfArrival.add(Calendar.SECOND, -travelTime);

        int dayOfMonth = dateOfArrival.get(Calendar.DAY_OF_MONTH);

        if (dayOfMonth != previousDay) {
            nextDay = true;
        } else {
            nextDay = false;
        }
        previousDay = dayOfMonth;

        return new SimpleDateFormat("dd.MM | HH:mm:ss").format(dateOfArrival.getTime());
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
