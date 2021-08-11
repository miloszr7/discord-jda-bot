package models;

public class Players {

    String id ;
    String name ;

    public Players() {}

    public Players(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "**" + id + "**" + "\u2009\u2009\u2009\u2009\u2009\u2009\u2009" + name;
    }
}
