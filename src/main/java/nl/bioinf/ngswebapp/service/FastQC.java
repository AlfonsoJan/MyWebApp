package nl.bioinf.ngswebapp.service;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

public class FastQC implements CommandConstructor {
    private final String executable = "fastqc";
    private final String fast = "tar";
    private final String type = "-czvf";
    private final String options = "-C";
    private final String loc = "/homes/jabusker/Desktop/";
    private final String filename = "/homes/jabusker/Desktop/tmp/projects.tar.gz";

    private final String[] files;

    public FastQC(String[] files) {
        this.files = files;
    }



    @Override
    public ProcessBuilder constructCommand() {
        String[] commandPrefixZip = {fast, type, filename, options, loc};
        String[] commadnPrefixFastQC = {executable};
        String[] command = Stream.of(commandPrefixZip, files).
                flatMap(Stream::of).
                toArray(String[]::new);
        ProcessBuilder fastqc = new ProcessBuilder(command);
        return fastqc;
    }

    public void startJob(String folderPath, String sessionID){
        String outFile = folderPath + sessionID + ".output.log";
        String errFile = folderPath + sessionID + ".error.log";

        ProcessBuilder pb = constructCommand();

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
