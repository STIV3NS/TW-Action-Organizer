package twao.villages;

import twao.Player;

public class AllyVillage extends Village {
    private final Player owner;

    public AllyVillage(int x, int y, Player owner) {
        super(x, y);
        this.owner = owner;
    }

    @Override
    public AllyVillage clone() {
        return (AllyVillage) super.clone();
    }

    /**
     * -----------------------------------------------------------
     * getters and setters
     * -----------------------------------------------------------
     */

    public Player getOwner() { return owner; }
}
