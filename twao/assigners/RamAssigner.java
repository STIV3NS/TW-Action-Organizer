package twao.assigners;

import twao.Player;
import twao.Village;
import twao.VillageAssignment;

import java.util.*;

public class RamAssigner implements Runnable {
    private List<Village> targets;
    private List<Village> attackingVillages;

    private final boolean fake;

    private Village center;

    public RamAssigner(List<Village> targets, List<Village> attackingVillages, Village center, boolean fake) {
        this.targets = targets;
        this.attackingVillages = attackingVillages;
        this.center = center;
        this.fake = fake;
    }

    private void sortAttackers() {
        for (Village vil : attackingVillages) {
            vil.setRelativeDistance(center);
        }

        attackingVillages.sort(Comparator.comparing(Village::getRelativeDistance));
        Collections.reverse(attackingVillages);
    }

    @Override
    public void run() {
        List<Village> usedVillages = new LinkedList<>();

        Village closestVil;
        int distance;

        List<VillageAssignment> assignmentsList;

        sortAttackers();

        for (Village attacker : attackingVillages) {
            if (targets.size() < 1) {
                break;
            }

            //init closestVil and distance
            closestVil = targets.get(0);
            distance = attacker.computeDistanceTo(closestVil);

            //search for closest village
            for (Village target : targets) {
                if (attacker.computeDistanceTo(target) < distance) {
                    distance = attacker.computeDistanceTo(target);
                    closestVil = target;
                }
            }

            if (fake) {
                assignmentsList = attacker.getOwner().getFakeAssignments();
            }
            else {
                assignmentsList = attacker.getOwner().getOffAssignments();
            }

            assignmentsList.add( new VillageAssignment(attacker, closestVil, distance) );

            usedVillages.add(attacker);

            closestVil.attack();
            if (closestVil.isAssignCompleted()) {
                targets.remove(closestVil);
            }
        }
        
        attackingVillages.removeAll(usedVillages);
    }
}
