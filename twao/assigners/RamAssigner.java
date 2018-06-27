package twao.assigners;

import twao.villages.AllyVillage;
import twao.villages.TargetVillage;
import twao.villages.Village;
import twao.VillageAssignment;

import java.util.*;

public class RamAssigner implements Runnable {
    private List<TargetVillage> targets;
    private List<AllyVillage> attackingVillages;

    private final boolean fake;

    private Village center;

    public RamAssigner(List<TargetVillage> targets, List<AllyVillage> attackingVillages, Village center, boolean fake) {
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
        List<AllyVillage> usedVillages = new LinkedList<>();

        TargetVillage closestVil;
        int distance;

        List<VillageAssignment> assignmentsList;

        sortAttackers();

        for (AllyVillage attacker : attackingVillages) {
            if (targets.size() == 0) {
                break;
            }

            //init closestVil and distance
            closestVil = targets.get(0);
            distance = attacker.computeDistanceTo(closestVil);

            //search for closest village
            for (TargetVillage target : targets) {
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
