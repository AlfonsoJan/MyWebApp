package nl.bioinf.ngswebapp.model;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TarGzipExample1 {

    private String resourcePath;
    private String[] fileNames;


    public TarGzipExample1(String resourcePath, String[] fileNames) {
        this.resourcePath = resourcePath;
        this.fileNames = fileNames;
    }

    // tar.gz few files
    public void createTarGzipFiles(List<Path> paths, Path output) throws IOException {

        try (OutputStream fOut = Files.newOutputStream(output);
             BufferedOutputStream buffOut = new BufferedOutputStream(fOut);
             GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(buffOut);
             TarArchiveOutputStream tOut = new TarArchiveOutputStream(gzOut)) {

            for (Path path : paths) {

//                if (!Files.isRegularFile(path)) {
//                    throw new IOException("Support only file!");
//                }

                TarArchiveEntry tarEntry = new TarArchiveEntry(
                        path.toFile(),
                        path.getFileName().toString());

                tOut.putArchiveEntry(tarEntry);

                // copy file to TarArchiveOutputStream
                Files.copy(path, tOut);

                tOut.closeArchiveEntry();

            }

            tOut.finish();

        }

    }

}
