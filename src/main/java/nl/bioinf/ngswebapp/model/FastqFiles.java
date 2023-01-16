package nl.bioinf.ngswebapp.model;

import nl.bioinf.ngswebapp.dao.DatabaseException;
import nl.bioinf.ngswebapp.dao.VerySimpleDbConnector;
import nl.bioinf.ngswebapp.servlets.NewFileTabServlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class FastqFiles {

    public static ArrayList<ArrayList<String>> getFiles(String filePath) throws IOException {
        ArrayList<ArrayList<String>> listOfFiles = new ArrayList<>();
        File[] filesList = new File(filePath).listFiles();
        if (filesList != null) {
            Arrays.sort(filesList, Comparator.comparingLong(File::lastModified).reversed());
            for (File file: filesList) {
                if (file.isFile() && (file.getName().endsWith(".fastq") || file.getName().endsWith(".fastq.gz") )) {
                    ArrayList<String> tmp = new ArrayList<>();
                    tmp.add(file.getName());
                    BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                    Date creationDate = new Date(attr.creationTime().to(TimeUnit.MILLISECONDS));
                    String pattern = "HH:mm:ss dd-MM-yyyy";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    String date = simpleDateFormat.format(creationDate);
                    tmp.add(date);
                    tmp.add(String.valueOf(getEnum(file.getName())));
                    listOfFiles.add(tmp);
                }
            }
        }
        return listOfFiles;
    }

    public static Enums.Used getEnum(String file){
        try {
            return NewFileTabServlet.getConnector().getTimesHardFileIsUsed(file);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
