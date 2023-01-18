package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.dao.DatabaseException;
import nl.bioinf.ngswebapp.dao.VerySimpleDbConnector;
import nl.bioinf.ngswebapp.service.JobRunner;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.UUID;

@WebServlet(name = "RunFastQCServlet", urlPatterns = "/runfastqc")
public class RunFastQCServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resourcePath = request.getServletContext().getInitParameter("file.location");
        String outPath = request.getServletContext().getInitParameter("analyse.folder");
        String[] files = request.getParameterValues("fileNames[]");
        String project = request.getParameter("project");
        String analyseType = request.getParameter("analyseType").toLowerCase();

        for (int i = 0; i < files.length; i++) {
            files[i] = resourcePath + files[i];
        }

        UUID randomID;
        while (true) {
            randomID = UUID.randomUUID();
            Path path = Path.of(outPath + randomID);
            if (Files.notExists(path)) {
                break;
            }
        }
        try {
            VerySimpleDbConnector connector = NewAnalyseServlet.getConnector();
            int projectId = connector.getProject(project, 1).getProjectId();
            connector.insertProcess(analyseType, projectId, randomID.toString());
        } catch (SQLException | DatabaseException e) {
            throw new RuntimeException(e);
        }
        JobRunner fastqc = new JobRunner(randomID, resourcePath, files, analyseType, outPath);
        fastqc.startJob();
    }
}
