package twao;

import java.util.List;
import java.util.LinkedList;


public class Player {
    private final String nick;
    private int numberOfVillages = 0;
    private int noblesAvailable;
    private final List<VillageAssignment> offAssignments = new LinkedList<>();
    private final List<VillageAssignment> fakeAssignments = new LinkedList<>();
    private final List<VillageAssignment> nobleAssignments = new LinkedList<>();
    private final List<VillageAssignment> fakeNobleAssignments = new LinkedList<>();

    public Player(String nick, int noblesAvailable) {
        this.nick = nick;
        this.noblesAvailable = noblesAvailable;
    }

    /**
     * -----------------------------------------------------------
     * getters and setters
     * -----------------------------------------------------------
     */

    public String getNick() { return nick; }

    public int getNumberOfVillages() { return numberOfVillages; }

    public void increaseNumberOfVillages() { numberOfVillages++; }

    public void decreaseNumberOfVillaes() { numberOfVillages--; }

    public synchronized boolean hasNoble() { return (noblesAvailable > 0) ? true : false; }

    public synchronized void decreaseNoblesAmount() { noblesAvailable--; }

    public int getNoblesAvailable() { return noblesAvailable; }

    public List<VillageAssignment> getOffAssignments() { return offAssignments; }

    public List<VillageAssignment> getFakeAssignments() { return fakeAssignments; }

    public List<VillageAssignment> getNobleAssignments() { return nobleAssignments; }

    public List<VillageAssignment> getFakeNobleAssignments() { return fakeNobleAssignments; }
}