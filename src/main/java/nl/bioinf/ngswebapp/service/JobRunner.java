package nl.bioinf.ngswebapp.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class JobRunner implements CommandConstructor {
    private final UUID uniqueId;
    private final String resourcePath;
    private final String[] files;
    private final String jobType;
    private final List<String> paths;

    public JobRunner(UUID uniqueId, String resourcePath, String[] files, String jobType) {
        this.uniqueId = uniqueId;
        this.resourcePath = resourcePath;
        this.files = files;
        this.jobType = jobType;
        this.paths = fileNamesToPath();
    }

    private List<String> fileNamesToPath() {
        List<String> paths = new ArrayList<>();
        for (String fileName : files) {
            paths.add(resourcePath + fileName);
        }
        return paths;
    }

    private String[] constructCommandPrefix() throws IOException {
        String uniquePath = resourcePath + "temp/" + uniqueId;
        Files.createDirectories(Paths.get(resourcePath + "temp/"));
        switch (jobType) {
            case "fastqc":
                Files.createDirectories(Paths.get(uniquePath));
                return new String[]{"fastqc", "-o", uniquePath};
            case "zip":
                return new String[]{"tar", "-czvf", uniquePath + ".tar.gz", "-C", resourcePath};
            default:
                return null;
        }
    }

    @Override
    public ProcessBuilder constructCommand() throws IOException {
        String[] commandPrefix = constructCommandPrefix();
        String[] command = Stream.of(commandPrefix, files).
                flatMap(Stream::of).
                toArray(String[]::new);
        return new ProcessBuilder(command);
    }

    public void startJob() throws IOException {
        String outFile;
        String errFile;
        if ("fastqc".equals(jobType)) {
            outFile = resourcePath + "temp/" + uniqueId + "/output.log";
            errFile = resourcePath + "temp/" + uniqueId + "/error.log";
        } else {
            outFile = resourcePath + "temp/" + uniqueId + ".output.log";
            errFile = resourcePath + "temp/" + uniqueId + ".error.log";
        }

        // Get the job command
        ProcessBuilder job = constructCommand();

        // Redirect output (note: some tools 'log' to the error stream..)
        job.redirectOutput(new File(outFile));
        job.redirectError(new File(errFile));

        try {
            // Run non-blocking
            final Process p = job.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
