package nl.bioinf.ngswebapp.db_objects;

import java.util.ArrayList;

public class Project {
    private int projectId;
    private String name;
    private int userId;
    private ArrayList<LabeledFile> labeledFiles = new ArrayList<>();

    private int processes;
    private int fileSize;

    public Project(int projectId, String name, int userId) {
        this.projectId = projectId;
        this.name = name;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return name;
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

    public void addLabeledFile(LabeledFile labeledFile) {
        labeledFiles.add(labeledFile);
    }

    public void setLabeledFiles(ArrayList<LabeledFile> labeledFiles) {
        this.labeledFiles = labeledFiles;
    }

    public int getFileSize() {
        return labeledFiles.size();
    }

    public int getProcesses() {
        return processes;
    }

    public void setProcesses(int processes) {
        this.processes = processes;
    }

}
