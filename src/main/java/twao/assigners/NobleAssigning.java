package twao.assigners;

import twao.Player;
import twao.VillageAssignment;
import twao.villages.AllyVillage;
import twao.villages.TargetVillage;

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

                Player owner = closestVil.getOwner();
                VillageAssignment assignment = new VillageAssignment(closestVil, target, distance);

                if (assigningFakes) {
                    owner.putFakeNobleAssignment( assignment );
                }
                else {
                    owner.putNobleAssignment( assignment );
                }

                owner.delegateNoble();
                target.attack();

                attackingVillages.remove(closestVil);

            }
        }
    }
}
