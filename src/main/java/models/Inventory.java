package models;

public class Inventory {

    private long userID;
    private String username;
    private int quantity;
    private String itemName;

    public Inventory() {}

    public Inventory(long userID, String username, int quantity, String itemName) {
        this.userID = userID;
        this.username = username;
        this.quantity = quantity;
        this.itemName = itemName;
    }

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return "[**" + quantity + "**] " + itemName;
    }
}
