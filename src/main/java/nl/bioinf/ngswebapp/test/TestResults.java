package nl.bioinf.ngswebapp.test;

import nl.bioinf.ngswebapp.dao.DatabaseException;
import nl.bioinf.ngswebapp.dao.VerySimpleDbConnector;
import nl.bioinf.ngswebapp.db_objects.Process;
import nl.bioinf.ngswebapp.service.FastQCResultsParser;
import nl.bioinf.ngswebapp.servlets.NewFileTabServlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestResults {
    private static VerySimpleDbConnector connector;
    public static void main(String[] args) {
        try {
            connector = new VerySimpleDbConnector();
        } catch (DatabaseException | IOException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Process> processes;
        try {
            processes = connector.getProcessFromUser(1, "fastqc");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (processes == null) {
            System.out.println(new ArrayList<>());
            return;
        }
        ResultParser resultParser = new ResultParser();
        List<List<String>> results = resultParser.isFinishedFastQC(processes);
        System.out.println(results);
    }
    public static VerySimpleDbConnector getConnector() {
        return connector;
    }
}
