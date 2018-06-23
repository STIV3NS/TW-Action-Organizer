package twao;

public final class VillageAssignment {
    private final Village departure;
    private final Village destination;
    private final double squaredDistance;

    public VillageAssignment(Village departure, Village destination, double squaredDistance) {
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

    public double getSquaredDistance() { return squaredDistance; }

    public Village getDeparture() { return departure; }

    public Village getDestination() { return destination; }
}
