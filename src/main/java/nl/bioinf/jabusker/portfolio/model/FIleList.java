package nl.bioinf.jabusker.portfolio.model;

import java.text.ParseException;
import java.util.Date;

public class FIleList implements Comparable<FIleList> {

    private String fileName;
    private Date date;

    public FIleList(String fileName, Date stringDate) throws ParseException {
        this.fileName = fileName;
        this.date = stringDate;
    };


    public String getFileName() {
        return fileName;
    }
    public Date getDate() { return date; }

    @Override
    public int compareTo(FIleList fIleList) {
        return getDate().compareTo(fIleList.getDate());
    }
}
