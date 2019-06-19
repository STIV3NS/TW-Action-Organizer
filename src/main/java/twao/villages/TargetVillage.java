package twao.villages;

public class TargetVillage extends Village {
    private int numberOfAttacks;

    /**
     * @param x                 X coordinate
     * @param y                 Y coordinate
     * @param numberOfAttacks   The number of attacks for a given village
     */
    public TargetVillage(int x, int y, int numberOfAttacks) {
        super(x, y);
        this.numberOfAttacks = numberOfAttacks;
    }

    /**
     * Decreases {@code numberOfAttacks} by 1.
     */
    public void attack()                { numberOfAttacks--; }
    public int getNumberOfAttacks()     { return numberOfAttacks; }
    /**
     * @return {@code true} only if there is no more attacks needed
     */
    public boolean isAssignCompleted()  { return numberOfAttacks > 0 ? false : true; }
}
