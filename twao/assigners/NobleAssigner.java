package twao.assigners;

import twao.Player;
import twao.Village;
import twao.VillageAssignment;

import java.util.*;

public class NobleAssigner implements Runnable {
    private List<Village> targets;
    private List<Village> attackingVillages;
    private HashMap<String, Player> players;
    private Village center;
    private final boolean fake;
    private final int maxNobleDistance;

    public NobleAssigner(List<Village> targets, List<Village> attackingVillages, HashMap<String, Player> players, Village center, boolean fake, int maxNobleDistance) {
        this.targets = targets;
        this.attackingVillages = attackingVillages;
        this.players = players;
        this.center = center;
        this.fake = fake;
        this.maxNobleDistance = (int) Math.pow(maxNobleDistance, 2) -1;
    }

    private void sortTargets() {
        for (Village vil : targets) {
            vil.setRelativeDistance(center);
        }

        targets.sort(Comparator.comparing(Village::getRelativeDistance));
    }

    @Override
    public void run() {
        List<Village> usedVillages = new LinkedList<>();
        List<Village> processingList = new LinkedList<>();

        Village closestVil;
        double distance;

        List<VillageAssignment> assignmentsList;

        sortTargets();

        processingList.addAll(attackingVillages);

        for (Village target : targets) {
            while (target.isAssignCompleted() == false) {
                if (processingList.size() < 1) {
                    break;
                }

                //init closestVil and distance
                closestVil = processingList.get(0);
                distance = target.computeDistanceTo(closestVil);

                //search for closest village
                List<Village> uselessVillages = new LinkedList<>();
                for (Village attacker : processingList) {
                    if (players.get(attacker.getOwner()).hasNoble() == false) {
                        uselessVillages.add(attacker);
                    }

                    else if (attacker.computeDistanceTo(target) < distance) {
                        closestVil = attacker;
                        distance = closestVil.computeDistanceTo(target);
                    }
                }
                processingList.removeAll(uselessVillages);

                if (maxNobleDistance < closestVil.getRelativeDistance()) {
                    break;
                }

                if ( players.get(closestVil.getOwner()).hasNoble() ) { //make sure that closestVil can send noble
                    if (fake) {
                        assignmentsList = players.get( closestVil.getOwner() ).getFakeNobleAssignments();
                    }
                    else {
                        assignmentsList = players.get( closestVil.getOwner() ).getNobleAssignments();
                    }

                    assignmentsList.add( new VillageAssignment(closestVil, target, distance) );

                    players.get( closestVil.getOwner() ).decreaseNoblesAmount();

                    usedVillages.add(closestVil);
                    processingList.remove(closestVil);

                    target.decreaseAttacks();
                } 
                else {
                    processingList.remove(closestVil);
                }
            }
        }
        
        attackingVillages.removeAll(usedVillages);
    }
}
