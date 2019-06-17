package twao.assigners;

import twao.Player;
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


            Player owner = attacker.getOwner();
            VillageAssignment assignment = new VillageAssignment(attacker, closestVil, distance);

            if (assigningFakes) {
                owner.putFakeAssignment( assignment );
            }
            else {
                owner.putOffAssignment( assignment );
            }

            usedVillages.add(attacker);

            closestVil.attack();
            if (closestVil.isAssignCompleted()) {
                targets.remove(closestVil);
            }
        }

        attackingVillages.removeAll(usedVillages);
    }
}
