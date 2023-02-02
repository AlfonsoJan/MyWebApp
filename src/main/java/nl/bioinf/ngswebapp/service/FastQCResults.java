package nl.bioinf.ngswebapp.service;

import nl.bioinf.ngswebapp.db_objects.Process;

import java.util.ArrayList;
import java.util.List;

public interface FastQCResults {
    List<List<String>> isFinishedFastQC(ArrayList<Process> analyseInfos, String loc);
    List<List<String>> isFinishedDownload(ArrayList<Process> analyseInfos, String loc);
}
