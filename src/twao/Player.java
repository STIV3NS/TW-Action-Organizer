package twao;

import java.util.List;
import java.util.LinkedList;


public class Player {
    private final String nickname;
    private int numberOfVillages = 0;
    private int numberOfNobles;
    private final List<VillageAssignment> offAssignments = new LinkedList<>();
    private final List<VillageAssignment> fakeAssignments = new LinkedList<>();
    private final List<VillageAssignment> nobleAssignments = new LinkedList<>();
    private final List<VillageAssignment> fakeNobleAssignments = new LinkedList<>();

    public Player(String nickname, int numberOfNobles) {
        this.nickname = nickname;
        this.numberOfNobles = numberOfNobles;
    }

    /**
     * -----------------------------------------------------------
     * getters and setters
     * -----------------------------------------------------------
     */

    public String getNickname() { return nickname; }

    public int getNumberOfVillages() { return numberOfVillages; }

    public void increaseNumberOfVillages() { numberOfVillages++; }

    public void decreaseNumberOfVillaes() { numberOfVillages--; }

    public boolean hasNoble() { return (numberOfNobles > 0) ? true : false; }

    public void delegateNoble() { numberOfNobles--; }

    public int getNumberOfNobles() { return numberOfNobles; }

    public synchronized List<VillageAssignment> getOffAssignments() { return offAssignments; }

    public synchronized List<VillageAssignment> getFakeAssignments() { return fakeAssignments; }

    public synchronized List<VillageAssignment> getNobleAssignments() { return nobleAssignments; }

    public synchronized List<VillageAssignment> getFakeNobleAssignments() { return fakeNobleAssignments; }
}
