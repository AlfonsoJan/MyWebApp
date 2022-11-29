package nl.bioinf.jabusker.portfolio.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FilesSorter implements Comparable<FilesSorter> {
    private String fileName;
    private Date date;

    public FilesSorter(String fileName, String stringDate) throws ParseException {
        this.fileName = fileName;
        this.date = parseDate(stringDate);;
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

    public void setDate(Date date) {
        this.date = date;
    }
}
