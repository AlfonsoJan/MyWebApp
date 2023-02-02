package nl.bioinf.ngswebapp.db_objects;

public class LabeledFile {
    private final int fileId;
    private String label;
    private String fullPath;

    public LabeledFile(int id, String label, String fullPath) {
        this.fileId = id;
        this.label = label;
        this.fullPath = fullPath;
    }

    public int getFileId() {
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
}
