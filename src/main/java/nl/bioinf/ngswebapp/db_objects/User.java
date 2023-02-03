package nl.bioinf.ngswebapp.db_objects;
/**
 * This class holds the data of the user
 * @author John Busker
 * @version 1.0
 */

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
