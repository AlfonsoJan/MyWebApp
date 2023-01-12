package nl.bioinf.ngswebapp.service;

import nl.bioinf.ngswebapp.model.AnalyseInfo;

import java.util.ArrayList;
import java.util.List;

public interface FastQCResults {
    List<List<String>> isFinished(ArrayList<AnalyseInfo> analyseInfos);
}
