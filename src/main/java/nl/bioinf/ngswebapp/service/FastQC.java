package nl.bioinf.ngswebapp.service;

import java.util.Arrays;
import java.util.stream.Stream;

public class FastQC implements QualityControl {
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
        String[] commandPrefix = {fast, type, filename, options, loc};
        String[] command = Stream.of(commandPrefix, files).
                flatMap(Stream::of).
                toArray(String[]::new);
        ProcessBuilder fastqc = new ProcessBuilder(command);
        return fastqc;
    }
}
