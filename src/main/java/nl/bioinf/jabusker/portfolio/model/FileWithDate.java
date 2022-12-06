package nl.bioinf.jabusker.portfolio.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FileWithDate {

    public ArrayList<FileList> data = new ArrayList<>();
    public void listOfFiles(File dirPath){
        File[] filesList = dirPath.listFiles();
        for(File file : filesList) {
            if(file.isFile()) {
                try {
                    BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                    Date creationDate = new Date(attr.creationTime().to(TimeUnit.MILLISECONDS));
                    String pattern = "hh:mm:ss dd-MM-yyyy";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    String date = simpleDateFormat.format(creationDate);
                    data.add(new FileList(file.toPath().toString(), creationDate));
                } catch (IOException | ParseException e) {
                    throw new RuntimeException(e);
                }
            }
//            else {
//                listOfFiles(file);
//            }
        }
    }

    public ArrayList<FileList> getFiles() throws IOException {
        // make temporary dummy data - remove when no longer needed
        new File(System.getProperty("user.home")+ "/Desktop/Data/").mkdirs();
        for (int i = 0; i < 50; i++){
            String newFile = String.format("/Desktop/Data/dummydata_%d.txt",i);
            new File(System.getProperty("user.home")+ newFile).createNewFile();
        }

        File file = new File(System.getProperty("user.home")+ "/Desktop/Data/");
        listOfFiles(file);
        return this.data;
    }
}