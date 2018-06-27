package twao.villages;

import twao.Player;
import twao.World;

import java.lang.Math;

public class Village implements Cloneable {
    private final int x;
    private final int y;
    private int id;

    private int relativeDistance;

    public Village(int x, int y) {
        this.x = x;
        this.y = y;
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

    public int getX() { return x; }

    public int getY() { return y; }

    public int getId() { return id; }

    public void setId(World world) throws Exception {
        id = world.getVillageId(this);
    }

    public int getRelativeDistance() { return relativeDistance; }

    public void setRelativeDistance(Village vil) { relativeDistance = computeDistanceTo(vil); }
}
