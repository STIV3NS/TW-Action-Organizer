package twao.assigners;

import twao.villages.AllyVillage;
import twao.villages.TargetVillage;
import twao.villages.Village;

import java.util.Comparator;
import java.util.List;

class Assigner implements Runnable {

    final List<TargetVillage> targets;
    final List<AllyVillage> attackingVillages;

    final Village relativityPoint;
    final boolean assigningFakes;
    private final AssigningStrategy assigningStrategy;

    Assigner(List<TargetVillage> targets, List<AllyVillage> attackingVillages, Village relativityPoint, boolean assigningFakes,
                    AssigningStrategy assigningStrategy) {
        this.targets = targets;
        this.attackingVillages = attackingVillages;

        this.relativityPoint = relativityPoint;
        this.assigningFakes = assigningFakes;
        this.assigningStrategy = assigningStrategy;
    }

    public void run() {
        assigningStrategy.run(this);
    }

    static void sort(List<? extends Village> villages, Village relativityPoint) {
        for (Village vil : villages) {
            vil.setRelativeDistance(relativityPoint);
        }

        villages.sort(Comparator.comparing(Village::getRelativeDistance));
    }
}
