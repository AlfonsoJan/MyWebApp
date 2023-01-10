package nl.bioinf.ngswebapp.service;

import java.io.File;
import java.io.IOException;

public interface JobRunner {

    void startJob(String folderPath, String sessionID) throws IOException;

}
