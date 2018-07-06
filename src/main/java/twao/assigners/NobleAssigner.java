package twao.assigners;

import twao.villages.AllyVillage;
import twao.villages.TargetVillage;
import twao.villages.Village;
import twao.VillageAssignment;

import java.util.*;

public class NobleAssigner implements Runnable {
    private List<TargetVillage> targets;
    private List<AllyVillage> attackingVillages;
    private Village relativityPoint;
    private final int maxNobleRange;
    private final boolean assigningFakes;

    public NobleAssigner(List<TargetVillage> targets, List<AllyVillage> attackingVillages, Village relativityPoint, boolean assigningFakes, int maxNobleRange) {
        this.targets = targets;
        this.attackingVillages = attackingVillages;
        this.relativityPoint = relativityPoint;
        this.maxNobleRange = (int) Math.pow(maxNobleRange, 2) -1;
        this.assigningFakes = assigningFakes;
    }

    private void sortTargets() {
        for (Village vil : targets) {
            vil.setRelativeDistance(relativityPoint);
        }

        targets.sort(Comparator.comparing(Village::getRelativeDistance));
    }

    @Override
    public void run() {
        List<AllyVillage> processingList = new LinkedList<>();

        List<AllyVillage> uselessVillages;
        List<VillageAssignment> assignmentsList;
        AllyVillage closestVil;
        int distance;

        processingList.addAll(attackingVillages);

        sortTargets();
        for (TargetVillage target : targets) {
            while (target.isAssignCompleted() == false) {
                if (processingList.size() == 0) {
                    break;
                }

                //init closestVil and distance
                closestVil = processingList.get(0);
                distance = target.getDistanceTo(closestVil);

                //search for closest village
                uselessVillages = new LinkedList<>();
                for (AllyVillage attacker : processingList) {
                    if (attacker.getOwner().hasNoble() == false) {
                        uselessVillages.add(attacker);
                    }

                    else if (attacker.getDistanceTo(target) < distance) {
                        closestVil = attacker;
                        distance = closestVil.getDistanceTo(target);
                    }
                }
                processingList.removeAll(uselessVillages);

                if (maxNobleRange < closestVil.getRelativeDistance()) {
                    break;
                }

                if ( closestVil.getOwner().hasNoble() ) { //make sure that closestVil can send noble
                    if (assigningFakes) {
                        assignmentsList = closestVil.getOwner().getFakeNobleAssignments();
                    }
                    else {
                        assignmentsList = closestVil.getOwner().getNobleAssignments();
                    }

                    assignmentsList.add( new VillageAssignment(closestVil, target, distance) );

                    closestVil.getOwner().delegateNoble();

                    attackingVillages.remove(closestVil);
                    processingList.remove(closestVil);

                    target.attack();
                } 
                else {
                    processingList.remove(closestVil);
                }
            }
        }
    }
}
