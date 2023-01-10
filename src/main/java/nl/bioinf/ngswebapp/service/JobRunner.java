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

    public JobRunner(UUID uniqueId, String resourcePath, String[] files, String jobType) {
        this.uniqueId = uniqueId;
        this.resourcePath = resourcePath;
        this.files = files;
        this.jobType = jobType;
    }

    private String[] constructCommandPrefix() throws IOException {
        Files.createDirectory(Paths.get(resourcePath + "/temp"));
        switch (jobType) {
            case "fastqc":
                Files.createDirectory(Paths.get(resourcePath + "/temp/" + uniqueId));
                return new String[]{"fastqc", "-C", resourcePath, "-o", "/temp/" + uniqueId};
            case "zip":
                return new String[]{"tar", "-czvf", "/temp/" + uniqueId + ".tar.gz", "-C", resourcePath};
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
//        System.out.println(Arrays.toString(command));
        return new ProcessBuilder(command);
    }

    public void startJob() throws IOException {
        String outFile = resourcePath + uniqueId + ".output.log";
        String errFile = resourcePath + uniqueId + ".error.log";

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
