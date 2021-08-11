package models;

public class Members {

    private long ID;
    private String name;

    public Members(String name, long ID) {
        this.name = name;
        this.ID = ID;
    }

    public long getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (**" + ID + "**)";
    }
}
