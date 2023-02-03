package nl.bioinf.ngswebapp.service;
/**
 * Interface tp construct the command
 * @author John Busker
 * @version 1.0
 */


import java.io.IOException;

public interface CommandConstructor {
    ProcessBuilder constructCommand() throws IOException;
}
