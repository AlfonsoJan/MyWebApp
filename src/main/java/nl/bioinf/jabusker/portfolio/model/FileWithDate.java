package nl.bioinf.jabusker.portfolio.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FileWithDate {
    public static void listOfFiles(File dirPath){
        List<List<String>> data = new ArrayList<>();
        File filesList[] = dirPath.listFiles();
        for(File file : filesList) {
            if(file.isFile()) {
                try {
                    BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                    Date creationDate = new Date(attr.creationTime().to(TimeUnit.MILLISECONDS));
                    // System.out.println(attr.creationTime() + " " + file);
                    String pattern = "hh:mm:ss dd-MM-yyyy";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    String date = simpleDateFormat.format(creationDate);
                    System.out.println(file + " " + date);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                listOfFiles(file);
            }
        }
    }
    public static void main(String args[]) throws IOException {
        //Creating a File object for directory
        File file = new File(System.getProperty("user.home")+ "/Desktop/Data/");
        //List of all files and directories
        listOfFiles(file);
    }
}