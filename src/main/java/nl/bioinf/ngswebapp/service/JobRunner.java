package nl.bioinf.ngswebapp.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

public class JobRunner implements CommandConstructor {
    private final UUID uniqueId;
    private final String resourcePath;
    private final String outPath;
    private final String[] files;
    private final String jobType;

    public JobRunner(UUID uniqueId, String resourcePath, String[] files, String jobType, String outPath) {
        this.uniqueId = uniqueId;
        this.resourcePath = resourcePath;
        this.files = files;
        this.jobType = jobType;
        this.outPath = outPath;
    }

    private String[] constructCommandPrefix() throws IOException {
        String uniquePath = outPath + uniqueId;
        switch (jobType) {
            case "fastqc":
                Files.createDirectories(Paths.get(uniquePath));
                return new String[]{"fastqc", "-o", uniquePath};
            case "zipper":
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
            outFile = outPath + uniqueId + "/output.log";
            errFile = outPath + uniqueId + "/error.log";
        } else {
            outFile = outPath + uniqueId + ".output.log";
            errFile = outPath + uniqueId + ".error.log";
        }

        // Get the job command
        ProcessBuilder job = constructCommand();
        // Redirect output (note: some tools 'log' to the error stream..)
        job.redirectOutput(new File(outFile));
        job.redirectError(new File(errFile));

        try {
            // Run non-blocking
            final Process p = job.start();
            if ("zipper".equals(jobType)) {
                p.waitFor();
                FileWriter myWriter = new FileWriter(outFile);
                myWriter.write("Done");
                myWriter.close();
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
