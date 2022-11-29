package nl.bioinf.jabusker.portfolio.model;

import org.thymeleaf.util.ArrayUtils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;

public class FIleList implements Comparable<FIleList> {

    private String fullPath;
    private String fileName;
    private Date date;

    public FIleList(String fileName, Date stringDate) throws ParseException {
        String[] tmp = fileName.split("/");
        this.fullPath = fileName;
        this.fileName = Arrays.toString(Arrays.copyOfRange(tmp, 5, tmp.length));
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
