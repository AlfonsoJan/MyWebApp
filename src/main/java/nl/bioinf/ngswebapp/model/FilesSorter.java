package nl.bioinf.ngswebapp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FilesSorter implements Comparable<FilesSorter> {
    private String fileName;
    private Date date;

    private Enums.Used used;

    public FilesSorter(String fileName, String stringDate, Enums.Used used) throws ParseException {
        this.fileName = fileName;
        this.date = parseDate(stringDate);
        this.used = used;
    };

    @Override
    public int compareTo(FilesSorter o) {
        return getDate().compareTo(o.getDate());
    }

    public String getFileName() {
        return fileName;
    }

    private Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Date getDate() {
        return date;
    }

    public Enums.Used getUsed() {
        return used;
    }

    public void setUsed(Enums.Used used) {
        this.used = used;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
