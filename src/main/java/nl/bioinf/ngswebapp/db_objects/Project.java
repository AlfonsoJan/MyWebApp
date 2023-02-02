package nl.bioinf.ngswebapp.db_objects;

import java.util.ArrayList;

public class Project {
    private int projectId;
    private String name;
    private ArrayList<LabeledFile> labeledFiles = new ArrayList<>();

    public Project(int projectId, String name, ArrayList<LabeledFile> labeledFiles) {
        this.projectId = projectId;
        this.name = name;
        this.labeledFiles = labeledFiles;
    }

    @Override
    public String toString() {
        return name + " " + projectId;
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

    public ArrayList<LabeledFile> getLabeledFiles() {
        return labeledFiles;
    }

    public void setLabeledFiles(ArrayList<LabeledFile> labeledFiles) {
        this.labeledFiles = labeledFiles;
    }

}
