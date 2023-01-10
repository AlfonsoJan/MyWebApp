package nl.bioinf.ngswebapp.service;

import java.io.IOException;

public interface CommandConstructor {
    ProcessBuilder constructCommand() throws IOException;
}
