package twao.assigners;

import twao.exceptions.UnspecifiedKeyException;
import twao.villages.AllyVillage;
import twao.villages.TargetVillage;
import twao.villages.Village;

import java.util.List;

public class AssignerBuilder {
    /**
     * Determines assigner behavior
     *
     * -. RAM: (concrete attack without noble) Pick ALLY village that is the farthest from [#relativityPoint] and link it with target village that
     * distance between it and given village is the shortest. Repeat.
     *
     * -. REVERSED_RAM: (concrete attack without noble) Pick TARGET village that is the closest to [#relativityPoint and link it with ally village
     * that distance between it and given village is the shortest. Repeat.
     *
     * -. FAKE_RAM: (fake attack without noble) Act like [type.RAM].
     *
     * -. NOBLE: (concrete attack with noble) Pick TARGET village that is the closest to [#relativityPoint] and link it with ally village
     * that distance between it and given village is the shortest. Repeat.
     *
     * -. FAKE_NOBKE: (fake attack with noble) Act like [type.NOBLE].
     */
    public enum AssignerType {
        RAM,
        REVERSED_RAM,
        FAKE_RAM,
        NOBLE,
        FAKE_NOBLE
    }

    private List<TargetVillage> targets;
    private List<AllyVillage> attackingVillages;
    private Village relativityPoint;
    private AssignerType type;
    private int maxNobleRange = -1;

    /**
     * Requires [#targets], [#attackingVillages], [#relativityPoint], [#type] to be set.
     * If assigning (fake)nobles [#maxNobleRange] is also obligatory.
     *
     * @return Concrete Assigner instance
     */
    public Assigner build() throws UnspecifiedKeyException {
        if (targets == null || attackingVillages == null || relativityPoint == null || type == null) {
            throw new UnspecifiedKeyException();
        }
        else if ( (type == AssignerType.NOBLE || type == AssignerType.FAKE_NOBLE) && maxNobleRange == -1 ) {
            throw new UnspecifiedKeyException("Max noble range is not specified.");
        }


        Assigner assigner = null;
        switch (type) {
            case RAM:
                assigner = new Assigner(targets, attackingVillages, relativityPoint, false, new RamAssigning());
                break;
            case REVERSED_RAM:
                assigner = new Assigner(targets, attackingVillages, relativityPoint, false, new ReversedRamAssigning());
                break;
            case FAKE_RAM:
                assigner = new Assigner(targets, attackingVillages, relativityPoint, true, new RamAssigning());
                break;
            case NOBLE:
                assigner = new Assigner(targets, attackingVillages, relativityPoint, false, new NobleAssigning(maxNobleRange));
                break;
        }

        return assigner;
    }

    /** Obligatory */
    public AssignerBuilder setTargets(List<TargetVillage> targets) {
        this.targets = targets;
        return this;
    }

    /** Obligatory */
    public AssignerBuilder setAttackingVillages(List<AllyVillage> attackingVillages) {
        this.attackingVillages = attackingVillages;
        return this;
    }

    /** Obligatory */
    public AssignerBuilder setRelativityPoint(Village relativityPoint) {
        this.relativityPoint = relativityPoint;
        return this;
    }

    /** Obligatory */
    public AssignerBuilder setType(AssignerType type) {
        this.type = type;
        return this;
    }

    /** Used only when assigning (fake)nobles */
    public AssignerBuilder setMaxNobleRange(int maxNobleRange) {
        this.maxNobleRange = maxNobleRange;
        return this;
    }
}
