package twao.assigners;

import twao.VillageAssignment;
import twao.villages.AllyVillage;
import twao.villages.TargetVillage;
import twao.villages.Village;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

class RamAssigning implements AssigningStrategy {
    public void run(Assigner assigner) {
        List<AllyVillage> attackingVillages = assigner.attackingVillages;
        List<TargetVillage> targets = assigner.targets;
        boolean assigningFakes = assigner.assigningFakes;


        List<AllyVillage> usedVillages = new LinkedList<>();

        Assigner.sort(attackingVillages, assigner.relativityPoint);
        Collections.reverse(attackingVillages);
        for (AllyVillage attacker : attackingVillages) {
            if (targets.size() == 0) {
                break;
            }

            Assigner.sort(targets, attacker);

            TargetVillage closestVil = targets.get(0);
            int distance =  Village.distance(attacker, closestVil);


            List<VillageAssignment> assignmentsList;
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
