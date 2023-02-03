package nl.bioinf.ngswebapp.service;
/**
 * Parse the results to check if the fastq or download is done
 * @author John Busker
 * @version 1.0
 */


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
    /**
     * This class is needed to map with 2 params
     * @param list1
     * @param location
     * @param combiner
     * @return
     * @param <T>
     * @param <U>
     * @param <R>
     */
    private static <T, U, R> List<R> listCombiner(
            List<T> list1, String location, BiFunction<T, U, R> combiner) {
        List<R> result = new ArrayList<>();
        for (int i = 0; i < list1.size(); i++) {
            result.add(combiner.apply(list1.get(i), (U) location));
        }
        return result;
    }

    @Override
    public List<List<String>> isFinishedFastQC(ArrayList<Process> analyseInfos, String loc) {
        return listCombiner(analyseInfos, loc, FastQCResultsParser.ResultFilter::filterRunningFastqc);
    }

    @Override
    public List<List<String>> isFinishedDownload(ArrayList<Process> downloadInfo, String loc) {
        return listCombiner(downloadInfo, loc, FastQCResultsParser.ResultFilter::filterRunningDownload);
    }

    public static class ResultFilter {

        public static List<String> filterRunningDownload(Process download, String loc) {
            List<String> tabledResults = new ArrayList<>();
            tabledResults.add(download.getProject().getName());
            tabledResults.add(isDownloadDone(download.getUniqueID(), loc));
            tabledResults.add(String.valueOf(download.getUniqueID()));
            return tabledResults;
        }
        public static List<String> filterRunningFastqc(Process analyse, String location) {
            List<String> tabledResults = new ArrayList<>();
            tabledResults.add(analyse.getProject().getName());
            tabledResults.add(isFastqcDone(analyse.getProject().getLabeledFiles(), analyse.getUniqueID(), location));
            tabledResults.add(String.valueOf(analyse.getUniqueID()));
            return tabledResults;
        }

        public static String isDownloadDone(String id, String loc) {
            Path pathFile = Paths.get(loc + id + ".tar.gz");
            Path pathLog = Paths.get(loc + id + ".output.log");
            Path pathError = Paths.get(loc + id + ".error.log");
            if (Files.notExists(pathFile)) {
                return "missing";
            }
            Reader reader = null;
            BufferedReader readlog;
            try {
                reader = new FileReader(String.valueOf(pathError));
                int readSize = reader.read();
                if (readSize != -1) {
                    return "error";
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