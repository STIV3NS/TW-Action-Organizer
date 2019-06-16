package twao.assigners;

import twao.exceptions.UnspecifiedKeyException;
import twao.villages.AllyVillage;
import twao.villages.TargetVillage;
import twao.villages.Village;

import java.util.List;

public class AssignerBuilder {
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

    public AssignerBuilder setTargets(List<TargetVillage> targets) {
        this.targets = targets;
        return this;
    }

    public AssignerBuilder setAttackingVillages(List<AllyVillage> attackingVillages) {
        this.attackingVillages = attackingVillages;
        return this;
    }

    public AssignerBuilder setRelativityPoint(Village relativityPoint) {
        this.relativityPoint = relativityPoint;
        return this;
    }

    public AssignerBuilder setType(AssignerType type) {
        this.type = type;
        return this;
    }

    public AssignerBuilder setMaxNobleRange(int maxNobleRange) {
        this.maxNobleRange = maxNobleRange;
        return this;
    }
}
