package nl.bioinf.ngswebapp.db_objects;

public class Process {

    private int id;
    private int projectId;
    private String type;

    public Process(int id, String type, int projectId) {
        this.id = id;
        this.projectId = projectId;
        this.type = type;
    }

    public Process(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
