package nl.bioinf.ngswebapp.service;

import java.io.File;
import java.io.IOException;

public class FastQCRunner implements JobRunner {
    private final FastQC job;

    public FastQCRunner(FastQC job) {
        this.job = job;
    }

    @Override
    public void startJob(String folderPath, String sessionID) throws IOException {
        String outFile = folderPath + sessionID + ".output.log";
        String errFile = folderPath + sessionID + ".error.log";

        ProcessBuilder pb = job.constructCommand();

        pb.redirectOutput(new File(outFile));
        pb.redirectError(new File(errFile));

        try {
            // Run non-blocking
            final Process p = pb.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
