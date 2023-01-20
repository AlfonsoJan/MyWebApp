package nl.bioinf.ngswebapp.service;

import nl.bioinf.ngswebapp.db_objects.LabeledFile;
import nl.bioinf.ngswebapp.db_objects.Process;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class FastQCResultsParser implements FastQCResults {

    @Override
    public List<List<String>> isFinishedFastQC(ArrayList<Process> analyseInfos, String loc) {
        return listCombiner(analyseInfos, loc, FastQCResultsParser.ResultFilter::filterRunningFastqc);
    }

    //    @Override
//    public static List<List<String>> isFinishedFastQC(ArrayList<Process> analyseInfos, String loc) {
//        return listCombiner(analyseInfos, loc, FastQCResultsParser.ResultFilter::filterRunningFastqc);
//    }
    private static <T, U, R> List<R> listCombiner(
            List<T> list1, String list2, BiFunction<T, U, R> combiner) {
        List<R> result = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            result.add(combiner.apply(list1.get(i), (U) list2));
        }
        return result;
    }

    @Override
    public List<List<String>> isFinishedDownload(ArrayList<Process> analyseInfos, String loc) {
        return analyseInfos.stream().map(FastQCResultsParser.ResultFilter::filterRunningDownload).collect(Collectors.toList());
    }

    public static class ResultFilter {

        public static List<String> filterRunningDownload(Process analyse) {
            List<String> tabledResults = new ArrayList<>();
            tabledResults.add(analyse.getProject().getName());
            tabledResults.add(isDownloadDone(analyse.getProject().getLabeledFiles(), analyse.getUniqueID(), "/students/2022-2023/Thema10/ngs_dummyfiles/temp/"));
            tabledResults.add(String.valueOf(analyse.getUniqueID()));
            return tabledResults;
        }
        public static List<String> filterRunningFastqc(Process analyse, String location) {
            List<String> tabledResults = new ArrayList<>();
            tabledResults.add(analyse.getProject().getName());
            tabledResults.add(isFastqcDone(analyse.getProject().getLabeledFiles(), analyse.getUniqueID(), location));
            tabledResults.add(String.valueOf(analyse.getUniqueID()));
            return tabledResults;
        }

        public static String isDownloadDone(ArrayList<LabeledFile> file, String id, String loc) {
            Path pathFile = Paths.get(loc + id + ".tar.gz");
            Path pathLog = Paths.get(loc + id + "/output.log");
            Path pathError = Paths.get(loc + id + "/error.log");
            if (Files.notExists(pathFile)) {
                return "missing";
            }

            Reader reader = null;
            BufferedReader readlog;
            try {
                reader = new FileReader(String.valueOf(pathError));
                int readSize = reader.read();
                if (readSize != -1) {
                    return "Error";
                }
                reader.close();

                String line;
                BufferedReader readLog = Files.newBufferedReader(pathLog);

                while ((line = readLog.readLine()) != null) {
                    if (line.toLowerCase().startsWith("done")) {
                        return "done";
                    }
                }
                readLog.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return "running";
        }

        public static String isFastqcDone(ArrayList<LabeledFile> file, String id, String loc) {
            Path path = Paths.get(loc + id + "/output.log");
            Path pathError = Paths.get(loc + id + "/error.log");
            if (Files.notExists(path)) {
                return "missing";
            }
            List<String> doneFiles;
            try {
                String line;
                BufferedReader readerError = Files.newBufferedReader(pathError);
                while ((line = readerError.readLine()) != null) {
                    if (line.toLowerCase().startsWith("failed")) {
                        return "failed";
                    }
                }
                readerError.close();
                BufferedReader reader = Files.newBufferedReader(path);
                doneFiles = new ArrayList<>();

                while ((line = reader.readLine()) != null) {
                    if (line.toLowerCase().startsWith("analysis complete")) {
                        String fileDone = line.split(" ")[3];
                        doneFiles.add(fileDone);
                    }
                }
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            for (LabeledFile fileName: file) {
                if (!doneFiles.contains(fileName.getFullPath())) {
                    return "running";
                }
            }
            return "done";
        }
    }
}
