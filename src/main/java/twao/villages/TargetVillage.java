package twao.villages;

public class TargetVillage extends Village {
    private int numberOfAttacks;

    public TargetVillage(int x, int y, int numberOfAttacks) {
        super(x, y);
        this.numberOfAttacks = numberOfAttacks;
    }

    public void attack() {
        numberOfAttacks--;
    }

    /**
     * -----------------------------------------------------------
     * getters and setters
     * -----------------------------------------------------------
     */

    public int getNumberOfAttacks() { return numberOfAttacks; }

    public boolean isAssignCompleted() { return numberOfAttacks > 0 ? false : true; }
}
