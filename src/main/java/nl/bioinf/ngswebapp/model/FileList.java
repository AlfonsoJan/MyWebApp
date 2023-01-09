package nl.bioinf.ngswebapp.model;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

public class FileList implements Comparable<FileList> {

    private String fullPath;
    private String fileName;
    private Date date;

    public FileList(String fileName, Date stringDate) throws ParseException {
        String[] tmp = fileName.split("/");
        this.fullPath = fileName;
        this.fileName = String.join("/", Arrays.copyOfRange(tmp, 5, tmp.length));
        this.date = stringDate;
    };


    public String getFileName() {
        return fileName;
    }
    public Date getDate() {
        return date;
    }
    public String getFullPath() {
        return fullPath;
    }

    @Override
    public int compareTo(FileList fIleList) {
        return getDate().compareTo(fIleList.getDate());
    }
}
