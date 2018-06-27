package pmsender;

import twao.Player;
import twao.VillageAssignment;
import twao.World;

import java.text.SimpleDateFormat;
import java.util.*;

public class PMFormatter {
    private final World world;

    private int previousDay = -1; //flag init
    private boolean nextDay = false; //flag init

    private static final int SECONDS_PER_MIN        = 60;
    private static final String OPEN_SPOILER        = "[spoiler]\n";
    private static final String CLOSE_SPOILER       = "[/spoiler]\n\n\n";
    private static final String NOBLE_HEADER        = "[unit]snob[/unit][unit]axe[/unit] [b]SZLACHTA[/b]\n";
    private static final String RAM_HEADER          = "[unit]ram[/unit] [b]OFF[/b]\n";
    private static final String FAKE_HEADER         = "[unit]spy[/unit] [b]FAKE[/b]\n";
    private static final String FAKENOBLE_HEADER    = "[unit]snob[/unit][unit]spy[/unit] [b]FAKE SZLACHTA[/b]\n";
    private static final String EXECUTION_TEXT      = "wykonaj";

    private enum Unit {
        RAM,
        NOBLE
    }

    public PMFormatter(World world) {
        this.world = world;
    }

    public String get(Player player) {
        StringBuilder sbuilder = new StringBuilder();

        if (player.getNobleAssignments().size() > 0) {
            sortAssignmentsList(player.getNobleAssignments());

            sbuilder.append(NOBLE_HEADER);
            generateCommandsList(sbuilder, player.getNobleAssignments(), getUnitSpeed(Unit.NOBLE));
        }

        if (player.getOffAssignments().size() > 0) {
            sortAssignmentsList(player.getOffAssignments());

            sbuilder.append(RAM_HEADER);
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

    private void generateCommandsList(StringBuilder sbuilder, List<VillageAssignment> assignmentsList, int speed) {
        String departureTime;
        int counter = 0;

        sbuilder.append(OPEN_SPOILER);

        for (VillageAssignment a : assignmentsList) {
            departureTime = getDepartureTime(a.getSquaredDistance(), speed);

            sbuilder.append(String.format("%s%d. %s %s\n", nextDay ? "\n\n\n" : "",
                                                        ++counter,
                                                        departureTime,
                                                        a.toString()));

            sbuilder.append(String.format("[url=%s/game.php?village=%d&screen=place&target=%d]%s[/url]\n", world.getDomain(),
                                                                                                            a.getDeparture().getId(),
                                                                                                            a.getDestination().getId(),
                                                                                                            EXECUTION_TEXT));
        }

        sbuilder.append(CLOSE_SPOILER);
    }

    private void sortAssignmentsList(List<VillageAssignment> list) {
        list.sort(Comparator.comparing(VillageAssignment::getSquaredDistance));
        Collections.reverse(list);
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
}
