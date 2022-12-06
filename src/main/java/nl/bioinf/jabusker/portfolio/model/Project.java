package nl.bioinf.jabusker.portfolio.model;

public class Project {
    private int projectId;
    private String name;
    private int userId;

    public Project(int projectId, String name, int userId) {
        this.projectId = projectId;
        this.name = name;
        this.userId = userId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
