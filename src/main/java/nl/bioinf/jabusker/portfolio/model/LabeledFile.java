package nl.bioinf.jabusker.portfolio.model;

public class LabeledFile {
    private final int fileId;
    private String label;
    private String fullPath;
    private int projectId;

    public LabeledFile(int id, String fullPath) {
        this.fileId = id;
        this.fullPath = fullPath;
    }

    public int getId() {
        return fileId;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
