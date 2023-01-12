package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.dao.VerySimpleDbConnector;
import nl.bioinf.ngswebapp.db_objects.Process;
import nl.bioinf.ngswebapp.service.JobRunner;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@WebServlet(name = "RunFastQCServlet", urlPatterns = "/runfastqc")
public class RunFastQCServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resourcePath = request.getServletContext().getInitParameter("resourcePath");
        String files[] = request.getParameterValues("fileNames[]");
        String project = request.getParameterValues("project[]")[0];
        for (int i = 0; i < files.length; i++) {
            files[i] = resourcePath + files[i];
        }
        String analyseType = request.getParameter("analyseType").toLowerCase();

        Process process;
        try {
            VerySimpleDbConnector connector = NewAnalyseServlet.getConnector();
            int projectId = connector.getProject(project, 1).getProjectId();
            process = connector.insertProcess(analyseType, projectId);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        System.out.println(process.getId());
        JobRunner fastqc = new JobRunner(process.getId(), resourcePath, files, analyseType);
        fastqc.startJob();
        // TODO: Add fastqc to DB
    }
}
