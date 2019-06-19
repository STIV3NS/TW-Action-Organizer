package twao.villages;

import twao.Player;

public class AllyVillage extends Village {
    private final Player owner;

    /**
     * Initializes {@code owner}'s village at ({@code x}|{@code y}).
     * @param x
     * @param y
     * @param owner
     */
    public AllyVillage(int x, int y, Player owner) {
        super(x, y);
        this.owner = owner;
    }

    public Player getOwner() { return owner; }
}
