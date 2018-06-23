package twao;

import java.lang.Math;

public class Village implements Cloneable {
    private final String owner;
    private final int x;
    private final int y;
    private int id;

    private int attacksAmount;

    private int relativeDistance;

    public Village(int x, int y) {
        this.x = x;
        this.y = y;
        this.owner = null;
    }

    public Village(String owner, int x, int y) {
        this.owner = owner;
        this.x = x;
        this.y = y;
    }

    public Village(int x, int y, int attacksAmount) {
        this.x = x;
        this.y = y;
        this.attacksAmount = attacksAmount;
        this.owner = "enemy";
    }

    public int computeDistanceTo(Village vil) {
        return (int) (Math.pow(this.getX() - vil.getX(), 2) + Math.pow(this.getY() - vil.getY(), 2));
    }

    @Override
    public String toString() {
        return String.format("%s|%s", x, y);
    }

    @Override
    public Village clone() {
        Village clone = null;
        try {
            clone = (Village) super.clone();
        } catch (CloneNotSupportedException e) {
            System.out.println("Unable to clone Village object.");
        }
        return clone;
    }

    /**
     * -----------------------------------------------------------
     * getters and setters
     * -----------------------------------------------------------
     */

    public void setId(World world) throws Exception {
        id = world.getVillageId(this);
    }

    public int getId() { return id; }

    public void setRelativeDistance(Village vil) {
        relativeDistance = computeDistanceTo(vil);
    }

    public int getRelativeDistance() { return relativeDistance; }
    
    public String getOwner() { return owner; }

    public int getX() { return x; }

    public int getY() { return y; }

    public int getAttacksAmount() { return attacksAmount; }

    public void decreaseAttacks() {
        attacksAmount--;
    }

    public boolean isAssignCompleted() { return (attacksAmount > 0) ? false : true; }
}
