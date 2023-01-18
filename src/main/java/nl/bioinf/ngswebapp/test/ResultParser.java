package nl.bioinf.ngswebapp.test;

import nl.bioinf.ngswebapp.db_objects.LabeledFile;
import nl.bioinf.ngswebapp.db_objects.Process;
import nl.bioinf.ngswebapp.service.FastQCResults;
import nl.bioinf.ngswebapp.service.FastQCResultsParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResultParser implements FastQCResults {

    @Override
    public List<List<String>> isFinished(ArrayList<Process> analyseInfos) {
        return analyseInfos.stream().map(ResultParser.ResultFilter::filterRunningBenchmarkResults).collect(Collectors.toList());
    }

    public static class ResultFilter {
        public static List<String> filterRunningBenchmarkResults(Process analyse) {
            List<String> tabledResults = new ArrayList<>();
            tabledResults.add(analyse.getProject().getName());
            tabledResults.add(isDone(analyse.getProject().getLabeledFiles(), analyse.getUniqueID(), "/students/2022-2023/Thema10/ngs_dummyfiles/temp/"));
            return tabledResults;
        }

        public static String isDone(ArrayList<LabeledFile> file, String id, String loc) {
            Path path = Paths.get(loc + id + "/output.log");
            Path pathError = Paths.get(loc + id + "/error.log");
            if (Files.notExists(path)) {
                return "missing";
            }
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
                BufferedReader readerError = Files.newBufferedReader(pathError);
                while ((line = readerError.readLine()) != null) {
                    if (line.startsWith("Failed")) {
                        return "failed";
                    }
                }
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (LabeledFile fileName: file) {
                if (!doneFiles.contains(fileName.getFullPath())) {
                    return "false";
                }
            }
            return "true";
        }

    }
}
