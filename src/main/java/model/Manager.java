package model;

public class Manager extends Staff implements Promotable {

    private int teamSize;

    public Manager(int id, String name, double salary, int teamSize) {
        super(id, name, salary, "Manager");
        setTeamSize(teamSize);
    }

    public int getTeamSize() { return teamSize; }

    public void setTeamSize(int teamSize) {
        if (teamSize < 0) {
            throw new IllegalArgumentException("Team size cannot be negative");
        }
        this.teamSize = teamSize;
    }

    @Override
    public void work() {
        System.out.println("Manager is managing a team of " + teamSize + " employees.");
    }

    @Override
    public String toString() {
        return super.toString() + " [Manager TeamSize=" + teamSize + "]";
    }

    @Override
    public void promote() {
        System.out.println(name + " has been promoted!");
    }
}