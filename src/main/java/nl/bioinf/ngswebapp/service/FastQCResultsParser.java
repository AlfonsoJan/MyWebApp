package nl.bioinf.ngswebapp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FastQCResultsParser implements FastQCResults {

    @Override
    public List<List<Object>> isFinished(String[] uniqueID, String folderPath) {
        //uniqueID..map(ResultFilter::filterRunningBenchmarkResults);
        System.out.println(Arrays.stream(uniqueID).map(ResultFilter::filterRunningBenchmarkResults).collect(Collectors.toList()));
        return null;
    }

    public static class ResultFilter {
        public static List<String> filterRunningBenchmarkResults(String id) {
            List<String> tabledResults = new ArrayList<>();
            tabledResults.add(id);
            return tabledResults;
        }
    }

    public static void main(String[] args) {
        String loc = "/students/2022-2023/Thema10/ngs_dummyfiles/temp/";
        String[] id = new String[]{"78992c54-4bdd-4bbb-9854-1f8f1da6475d"};
    }
}
