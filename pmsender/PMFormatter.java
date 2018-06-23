package pmsender;

import twao.Player;
import twao.VillageAssignment;
import twao.World;
import twao.Village;

import java.util.*;

import java.text.SimpleDateFormat;

public class PMFormatter {
    private final World world;
    private final int SECONDS_PER_MIN = 60;
    private int previousDay = 0;

    public PMFormatter(World world) {
        this.world = world;
    }

    private String getDepartureTime(double squaredDistance, int standardTimePerUnit) {
        Calendar dateOfArrival = new GregorianCalendar(2018, Calendar.MAY, 21);
        dateOfArrival.add(Calendar.HOUR, 8);

        double distance = Math.sqrt(squaredDistance);
        double unitSpeed = standardTimePerUnit / world.getSpeed();
        int travelTime = (int) (((distance * unitSpeed) * SECONDS_PER_MIN) * -1);

        dateOfArrival.add(Calendar.SECOND, travelTime);

        int dayOfMonth = dateOfArrival.get(Calendar.DAY_OF_MONTH);

        String output = (dayOfMonth != previousDay ? "\n\n\n\n\n" : "") + new SimpleDateFormat("dd.MM | HH:mm:ss").format(dateOfArrival.getTime());

        previousDay = dayOfMonth;
        return output;
    }

    public String generateCommandsList(Player player) {
        StringBuilder sbuilder = new StringBuilder();
        HashMap<Village, Integer> noblesToRecruit = new HashMap<>();
        int counter;

        if (player.getNobleAssignments().size() > 0) {
            player.getNobleAssignments().sort(Comparator.comparing(VillageAssignment::getSquaredDistance));
            Collections.reverse(player.getNobleAssignments());

            sbuilder.append("[unit]snob[/unit][unit]axe[/unit] [b]SZLACHTA[/b]\n");
            sbuilder.append("[spoiler]\n");

            counter = 0;
            for (VillageAssignment a : player.getNobleAssignments()) {
                noblesToRecruit.put(a.getDeparture(), 1);
                sbuilder.append( String.format("%s\n %s. %s\n", getDepartureTime(a.getSquaredDistance(), 35), ++counter,  a.toString()) );
                sbuilder.append( String.format("[url=%s/game.php?village=%s&screen=place&target=%s]Wykonaj[/url]\n", world.getDomain(), a.getDeparture().getId(), a.getDestination().getId()) );
            }
            sbuilder.append("[/spoiler]\n\n\n");
        }

        if (player.getOffAssignments().size() > 0) {
            player.getOffAssignments().sort(Comparator.comparing(VillageAssignment::getSquaredDistance));
            Collections.reverse(player.getOffAssignments());

            sbuilder.append("[unit]ram[/unit] [b]OFF[/b]\n");
            sbuilder.append("[spoiler]\n");

            counter = 0;
            for (VillageAssignment a : player.getOffAssignments()) {
                try {
                    a.getDeparture().setId(world);
                    a.getDestination().setId(world);
                } catch (Exception e) {
                    System.out.println("Cannot set village id");
                    System.out.println(a.getDeparture().toString());
                    System.out.println(a.getDestination().toString());
                }

                sbuilder.append( String.format("%s. %s %s\n", ++counter, getDepartureTime(a.getSquaredDistance(), 30),  a.toString()) );
                sbuilder.append( String.format("[url=%s/game.php?village=%s&screen=place&target=%s]Wykonaj[/url]\n", world.getDomain(), a.getDeparture().getId(), a.getDestination().getId()) );
            }
            sbuilder.append("[/spoiler]\n\n\n");
        }
        
        if (player.getFakeAssignments().size() > 0) {
            player.getFakeAssignments().sort(Comparator.comparing(VillageAssignment::getSquaredDistance));
            Collections.reverse(player.getFakeAssignments());

            sbuilder.append("[unit]spy[/unit] [b]FAKE[/b]\n");
            sbuilder.append("[spoiler]\n");

            counter = 0;
            for (VillageAssignment a : player.getFakeAssignments()) {
                try {
                    a.getDeparture().setId(world);
                    a.getDestination().setId(world);
                } catch (Exception e) {
                    System.out.println("Cannot set village id");
                    System.out.println(a.getDeparture().toString());
                    System.out.println(a.getDestination().toString());
                }

                sbuilder.append(String.format("%s. %s %s\n", ++counter, getDepartureTime(a.getSquaredDistance(), 30), a.toString()));
                sbuilder.append(String.format("[url=%s/game.php?village=%s&screen=place&target=%s]Wykonaj[/url]\n", world.getDomain(), a.getDeparture().getId(), a.getDestination().getId()));
            }
            sbuilder.append("[/spoiler]\n\n\n");
        }

        if (player.getFakeNobleAssignments().size() > 0) {
            //TODO
        }

        if (noblesToRecruit.size() > 0) {
            //todo
            //sbuilder.insert(0, str);
        }

        return sbuilder.toString();
    }
}
