package nl.bioinf.ngswebapp.test;

import nl.bioinf.ngswebapp.db_objects.Process;
import nl.bioinf.ngswebapp.service.FastQCResults;

import java.util.ArrayList;
import java.util.List;

public class ResultParser implements FastQCResults {

    @Override
    public List<List<String>> isFinished(ArrayList<Process> analyseInfos) {
        return null;
    }
}
