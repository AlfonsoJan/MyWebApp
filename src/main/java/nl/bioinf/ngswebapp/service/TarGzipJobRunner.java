package nl.bioinf.ngswebapp.service;

import nl.bioinf.ngswebapp.model.TarGzipExample1;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TarGzipJobRunner {

    public void callZipper() {
        fileName = "placeholder.tar.gz";

        TarGzipExample1 zipper = new TarGzipExample1(resourcePath, allFiles);

        resourcePath = resourcePath + "/temp";

        List<Path> paths = zipper.fileNamesToPaths();
        Path outPath = Paths.get(resourcePath + fileName);

        zipper.createTarGzipFiles(paths, outPath);
    }
}
