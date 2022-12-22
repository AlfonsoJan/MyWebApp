package nl.bioinf.jabusker.portfolio.model;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TarZipMultipleFiles {

    // tar.gz few files
    public static void createTarGzipFile(String[] fileNames, String resourcePath, String output) throws IOException {

        try (OutputStream outputStream = Files.newOutputStream(Paths.get(output));
             BufferedOutputStream bufferedOutput = new BufferedOutputStream(outputStream);
             GzipCompressorOutputStream gzipCompressorOutput = new GzipCompressorOutputStream(bufferedOutput);
             TarArchiveOutputStream tarArchiveOutput = new TarArchiveOutputStream(gzipCompressorOutput)) {

            for (String fileName : fileNames) {

                File file = new File(resourcePath + fileName);

                tarArchiveOutput.putArchiveEntry(new TarArchiveEntry(file, fileName));

                // copy file to TarArchiveOutputStream
                Files.copy(file.toPath(), tarArchiveOutput);

                tarArchiveOutput.closeArchiveEntry();
            }
            tarArchiveOutput.finish();
        }
    }
}
