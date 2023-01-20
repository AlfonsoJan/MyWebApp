package nl.bioinf.ngswebapp.servlets;

import com.google.gson.Gson;
import nl.bioinf.ngswebapp.db_objects.Process;
import nl.bioinf.ngswebapp.service.FastQCResults;
import nl.bioinf.ngswebapp.service.FastQCResultsParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CheckAnalyseDoneServlet", urlPatterns = "/analysedone")
public class CheckAnalyseDoneServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resourcePath = request.getServletContext().getInitParameter("analyse.folder");
        ArrayList<Process> processes;
        try {
            processes = AnalyseServlet.getConnector().getProcessFromUser(1, "fastqc");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        List<List<String>> results = null;
        if (processes != null) {
            FastQCResultsParser fastQCResultsParser = new FastQCResultsParser();
            results = fastQCResultsParser.isFinishedFastQC(processes, resourcePath);
        }

        String json = new Gson().toJson(results);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }
}
