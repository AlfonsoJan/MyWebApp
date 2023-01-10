package nl.bioinf.ngswebapp.model;

public class AnalyseInfo {
    private String projectName;
    private String[] files;
    private String sessionID;

    public AnalyseInfo(String projectName, String[] files, String sessionID) {
        this.projectName = projectName;
        this.files = files;
        this.sessionID = sessionID;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String[] getFiles() {
        return files;
    }

    public void setFiles(String[] files) {
        this.files = files;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }
}
