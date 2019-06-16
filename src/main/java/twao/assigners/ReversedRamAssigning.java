package twao.assigners;

import twao.VillageAssignment;
import twao.villages.AllyVillage;
import twao.villages.TargetVillage;
import twao.villages.Village;

import java.util.List;

class ReversedRamAssigning implements AssigningStrategy {
    public void run(Assigner assigner) {
        List<TargetVillage> targets = assigner.targets;
        List<AllyVillage> attackingVillages = assigner.attackingVillages;
        boolean assigningFakes = assigner.assigningFakes;


        Assigner.sort(targets, assigner.relativityPoint);
        for (TargetVillage target : targets) {
            while (target.isAssignCompleted() == false) {
                if (attackingVillages.size() == 0) {
                    break;
                }

                Assigner.sort(attackingVillages, target);

                AllyVillage closestVil = attackingVillages.get(0);
                int distance = Village.distance(target, closestVil);

                List<VillageAssignment> assignmentsList;
                if (assigningFakes) {
                    assignmentsList = closestVil.getOwner().getFakeAssignments();
                }
                else {
                    assignmentsList = closestVil.getOwner().getOffAssignments();
                }

                assignmentsList.add( new VillageAssignment(closestVil, target, distance) );

                attackingVillages.remove(closestVil);
                target.attack();
            }
        }
    }
}
