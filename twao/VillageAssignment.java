package twao;

import twao.villages.Village;

public final class VillageAssignment {
    private final Village departure;
    private final Village destination;
    private final int squaredDistance;

    public VillageAssignment(Village departure, Village destination, int squaredDistance) {
        this.departure = departure;
        this.destination = destination;
        this.squaredDistance = squaredDistance;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", departure, destination);
    }

    /**
     * -----------------------------------------------------------
     * getters and setters
     * -----------------------------------------------------------
     */

    public int getSquaredDistance() { return squaredDistance; }

    public Village getDeparture() { return departure; }

    public Village getDestination() { return destination; }
}
