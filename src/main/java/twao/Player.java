package twao;

import java.util.List;
import java.util.LinkedList;

public class Player {
    private final String                  nickname;
    private int                           numberOfVillages = 0;
    private int                           numberOfNobles;
    private final List<VillageAssignment> offAssignments = new LinkedList<>();
    private final List<VillageAssignment> fakeAssignments = new LinkedList<>();
    private final List<VillageAssignment> nobleAssignments = new LinkedList<>();
    private final List<VillageAssignment> fakeNobleAssignments = new LinkedList<>();

    /**
     * Initializes player.
     *
     * @param nickname          Player's nickname
     * @param numberOfNobles    Number of nobles that given player may produce
     */
    public Player(String nickname, int numberOfNobles) {
        this.nickname = nickname;
        this.numberOfNobles = numberOfNobles;
    }

    public String getNickname()             { return nickname; }

    public int getNumberOfVillages()        { return numberOfVillages; }
    public void increaseNumberOfVillages()  { numberOfVillages++; }
    public void decreaseNumberOfVillaes()   { numberOfVillages--; }

    public int getNumberOfNobles()          { return numberOfNobles; }
    public boolean hasNoble()               { return (numberOfNobles > 0) ? true : false; }

    /**
     * Decreases number of available nobles by 1.
     */
    public void delegateNoble()             { numberOfNobles--; }

    public List<VillageAssignment> getOffAssignmentsCopy()          { return new LinkedList<>(offAssignments); }
    public List<VillageAssignment> getFakeAssignmentsCopy()         { return new LinkedList<>(fakeAssignments); }
    public List<VillageAssignment> getNobleAssignmentsCopy()        { return new LinkedList<>(nobleAssignments); }
    public List<VillageAssignment> getFakeNobleAssignmentsCopy()    { return new LinkedList<>(fakeNobleAssignments); }

    public synchronized void putOffAssignment       (VillageAssignment assignment) { offAssignments.add(assignment); }
    public synchronized void putFakeAssignment      (VillageAssignment assignment) { fakeAssignments.add(assignment); }
    public synchronized void putNobleAssignment     (VillageAssignment assignment) { nobleAssignments.add(assignment); }
    public synchronized void putFakeNobleAssignment (VillageAssignment assignment) { fakeNobleAssignments.add(assignment); }
}
