package twao.assigners;

import twao.villages.AllyVillage;
import twao.villages.TargetVillage;
import twao.villages.Village;
import twao.VillageAssignment;

import java.util.*;

public class RamAssigner implements Runnable {
    private List<TargetVillage> targets;
    private List<AllyVillage> attackingVillages;
    private Village relativityPoint;
    private final boolean assigningFakes;

    public RamAssigner(List<TargetVillage> targets, List<AllyVillage> attackingVillages, Village relativityPoint, boolean assigningFakes) {
        this.targets = targets;
        this.attackingVillages = attackingVillages;
        this.relativityPoint = relativityPoint;
        this.assigningFakes = assigningFakes;
    }

    private void sortAttackers() {
        for (Village vil : attackingVillages) {
            vil.setRelativeDistance(relativityPoint);
        }

        attackingVillages.sort(Comparator.comparing(Village::getRelativeDistance));
        Collections.reverse(attackingVillages);
    }

    @Override
    public void run() {
        List<AllyVillage> usedVillages = new LinkedList<>();

        List<VillageAssignment> assignmentsList;
        TargetVillage closestVil;
        int distance;

        sortAttackers();
        for (AllyVillage attacker : attackingVillages) {
            if (targets.size() == 0) {
                break;
            }

            //init closestVil and distance
            closestVil = targets.get(0);
            distance = attacker.getDistanceTo(closestVil);

            //search for closest village
            for (TargetVillage target : targets) {
                if (attacker.getDistanceTo(target) < distance) {
                    distance = attacker.getDistanceTo(target);
                    closestVil = target;
                }
            }

            if (assigningFakes) {
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
