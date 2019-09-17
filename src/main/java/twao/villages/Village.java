package twao.villages;

import twao.World;
import twao.exceptions.VillageNotFoundException;

import java.lang.Math;

/**
 * Represents given Village.
 *
 * It is meant to be used as {@code List<Village>} member so it implements 'relativeDistance' methods to
 * cooperate with them.
 */
public class Village {
    private final int x;
    private final int y;
    private int       id;

    private int       relativeDistance;

    /**
     * Initializes village at ({@code x}|{@code y}).
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public Village(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param v1
     * @param v2
     * @return Cartesian distance between v1 and v2 (to power of square)
     */
    public static int distance(Village v1, Village v2) {
        return (int) (Math.pow(v1.getX() - v2.getX(), 2) + Math.pow(v1.getY() - v2.getY(), 2));
    }

    /**
     *
     * @return Village coordinates text representation
     */
    @Override
    public String toString() {
        return String.format("%s|%s", x, y);
    }

    public int getX()  { return x; }
    public int getY()  { return y; }
    public int getId() { return id; }

    /**
     * Uses {@code World} to initialize its id.
     *
     * @param world
     * @throws VillageNotFoundException
     */
    public void initId(World world) throws VillageNotFoundException {
        id = world.fetchVillageId(this);
    }

    public int getRelativeDistance()             { return relativeDistance; }
    public void setRelativeDistance(Village vil) { relativeDistance = distance(this, vil); }
}
