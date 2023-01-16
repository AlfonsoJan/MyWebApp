package nl.bioinf.ngswebapp.service;

import nl.bioinf.ngswebapp.db_objects.Process;
import nl.bioinf.ngswebapp.model.AnalyseInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FastQCResultsParser implements FastQCResults {

    @Override
    public List<List<String>> isFinished(ArrayList<Process> analyseInfos) {
        return analyseInfos.stream().map(ResultFilter::filterRunningBenchmarkResults).collect(Collectors.toList());
    }

    public static class ResultFilter {
        public static List<String> filterRunningBenchmarkResults(Process analyse) {
            List<String> tabledResults = new ArrayList<>();
            tabledResults.add(analyse.getProject().getName());
            //tabledResults.add(isDone(analyse.getFiles(), analyse.getSessionID()));
            return tabledResults;
        }
        public static String isDone(String[] file, String id) {
            // TODO: Method to check for errors
            Path path = Paths.get("/students/2022-2023/Thema10/ngs_dummyfiles/temp/" + id + "/output.log");
            List<String> doneFiles;
            try {
                BufferedReader reader = Files.newBufferedReader(path);
                doneFiles = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Analysis complete")) {
                        String fileDone = line.split(" ")[3];
                        doneFiles.add(fileDone);
                    }
                }
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (String fileName: file) {
                if (!doneFiles.contains(fileName)) {
                    return "false";
                }
            }
            return "true";
        }
    }

    public static void main(String[] args) {
        String loc = "/students/2022-2023/Thema10/ngs_dummyfiles/temp/";
        String[] id = new String[]{"78992c54-4bdd-4bbb-9854-1f8f1da6475d"};
    }
}
