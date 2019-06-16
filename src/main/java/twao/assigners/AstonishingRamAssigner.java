package twao.assigners;

import twao.VillageAssignment;
import twao.villages.AllyVillage;
import twao.villages.TargetVillage;
import twao.villages.Village;

import java.util.*;

public class AstonishingRamAssigner implements Runnable {
    private List<TargetVillage> targets;
    private List<AllyVillage> attackingVillages;
    private Village relativityPoint;

    public AstonishingRamAssigner(List<TargetVillage> targets, List<AllyVillage> attackingVillages, Village relativityPoint) {
        this.targets = targets;
        this.attackingVillages = attackingVillages;
        this.relativityPoint = relativityPoint;
    }

    private void sortTargets() {
        for (Village vil : targets) {
            vil.setRelativeDistance(relativityPoint);
        }

        targets.sort(Comparator.comparing(Village::getRelativeDistance));
    }

    @Override
    public void run() {
        AllyVillage closestVil;
        int distance;

        sortTargets();
        for (TargetVillage target : targets) {
            while (target.isAssignCompleted() == false) {
                if (attackingVillages.size() == 0) {
                    break;
                }

                //init closestVil and distance
                closestVil = attackingVillages.get(0);
                distance = Village.distance(target, closestVil);

                //search for closest village
                for (AllyVillage attacker : attackingVillages) {
                    if (Village.distance(attacker, target) < distance) {
                        closestVil = attacker;
                        distance = Village.distance(closestVil, target);
                    }
                }

                closestVil.getOwner().getOffAssignments().add( new VillageAssignment(closestVil, target, distance) );

                attackingVillages.remove(closestVil);
                target.attack();
            }
        }
    }
}
