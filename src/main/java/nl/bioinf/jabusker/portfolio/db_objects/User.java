package nl.bioinf.jabusker.portfolio.db_objects;

public class User {
    private final int userId;
    private String name;


    public User(int userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}