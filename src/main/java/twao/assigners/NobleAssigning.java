package twao.assigners;

import twao.VillageAssignment;
import twao.villages.AllyVillage;
import twao.villages.TargetVillage;
import twao.villages.Village;

import java.util.LinkedList;
import java.util.List;

class NobleAssigning implements AssigningStrategy {
    private final int maxNobleRange;

    NobleAssigning(int maxNobleRange) {
        this.maxNobleRange = maxNobleRange;
    }

    public void run(Assigner assigner) {
        List<AllyVillage> attackingVillages = assigner.attackingVillages;
        List<TargetVillage> targets = assigner.targets;
        boolean assigningFakes = assigner.assigningFakes;

        LinkedList<AllyVillage> processingList = new LinkedList<>();
        processingList.addAll(attackingVillages);

        Assigner.sort(targets, assigner.relativityPoint);
        for (TargetVillage target : targets) {
            while (target.isAssignCompleted() == false) {
                if (processingList.size() == 0) {
                    break;
                }

                Assigner.sort(attackingVillages ,target);

                AllyVillage closestVil = processingList.pollFirst();
                while (closestVil != null && closestVil.getOwner().hasNoble() == false) {
                    closestVil = processingList.pollFirst();
                }

                if (closestVil == null) {
                    break;
                }

                int distance = closestVil.getRelativeDistance();

                if (maxNobleRange < distance) {
                    break;
                }

                List<VillageAssignment> assignmentsList;
                if (assigningFakes) {
                    assignmentsList = closestVil.getOwner().getFakeNobleAssignments();
                }
                else {
                    assignmentsList = closestVil.getOwner().getNobleAssignments();
                }

                assignmentsList.add( new VillageAssignment(closestVil, target, distance) );

                closestVil.getOwner().delegateNoble();

                attackingVillages.remove(closestVil);

                target.attack();
            }
        }
    }
}
