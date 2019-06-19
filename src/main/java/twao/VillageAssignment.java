package twao;

import twao.villages.Village;

public final class VillageAssignment {
    private final Village  departure;
    private final Village  destination;
    private final int      squaredDistance;

    /**
     * Creates assigment.
     *
     * @param departure         Village from which the attack will be carried out
     * @param destination       Village which will be attacked
     * @param squaredDistance   Cartesian distance between villages raised to square
     *                          (no square root performed meanwhile because of efficiety issues)
     */
    public VillageAssignment(Village departure, Village destination, int squaredDistance) {
        this.departure = departure;
        this.destination = destination;
        this.squaredDistance = squaredDistance;
    }

    /**
     * @return "{@code departure} -> {@code destination}"
     */
    @Override
    public String toString() {
        return String.format("%s -> %s", departure, destination);
    }
    public Village getDeparture()   { return departure; }
    public Village getDestination() { return destination; }
    public int getSquaredDistance() { return squaredDistance; }
}
