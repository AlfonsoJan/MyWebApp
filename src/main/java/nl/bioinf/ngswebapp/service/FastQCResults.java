package nl.bioinf.ngswebapp.service;

import java.util.List;

public interface FastQCResults {
    List<List<Object>> isFinished(String[] uniqueID, String folderPath);
}
