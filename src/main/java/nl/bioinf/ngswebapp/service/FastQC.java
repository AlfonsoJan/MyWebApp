package nl.bioinf.ngswebapp.service;

import java.util.stream.Stream;

public class FastQC implements QualityControl {
    private final String executable = "fastqc";

    private final String[] files;

    public FastQC(String[] files) {
        this.files = files;
    }



    @Override
    public ProcessBuilder constructCommand() {
        String[] commandPrefix = {executable};
        String[] command = Stream.of(commandPrefix, files).
                flatMap(Stream::of).
                toArray(String[]::new);
        System.out.println(command);
        return null;
    }
}
