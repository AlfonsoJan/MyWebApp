package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.service.FastQC;
import nl.bioinf.ngswebapp.service.FastQCRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RunFastQCServlet {
    public static void main(String[] args) throws IOException {
        //String algorithms[] = request.getParameterValues("algorithms[]");
//        String files[] = new String[]{"/homes/jabusker/Desktop/clock_10K_R1.fastq.gz",
//                "/homes/jabusker/Desktop/clock_10K_R2.fastq.gz", "-o",
//        "/homes/jabusker/Desktop/tmp/"};
        String files[] = new String[]{"clock_10K_R1.fastq.gz",
                "clock_10K_R2.fastq.gz"};
        FastQC fastQC = new FastQC(files);
        FastQCRunner runnerFastQC = new FastQCRunner(fastQC);
        Files.createDirectories(Paths.get("/homes/jabusker/Desktop/tmp/"));
        runnerFastQC.startJob("/homes/jabusker/Desktop/tmp/", "test");
    }
}
