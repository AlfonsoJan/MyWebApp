package nl.bioinf.ngswebapp.servlets;

import com.google.gson.Gson;
import nl.bioinf.ngswebapp.db_objects.Process;
import nl.bioinf.ngswebapp.service.FastQCResults;
import nl.bioinf.ngswebapp.service.FastQCResultsParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "CheckStatusProcessServlet", urlPatterns = "/results")
public class CheckStatusProcessServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        String loc = getServletContext().getInitParameter("analyse.folder");
        ArrayList<Process> processList;
        try {
            String json;
            FastQCResults fastQCResults = new FastQCResultsParser();
            switch (type) {
                case "fastqc":
                    processList = NewFileTabServlet.getConnector().getProcess(1, "fastqc");
                    json = new Gson().toJson(fastQCResults.isFinishedFastQC(processList, loc));
                    break;
                case "downloads":
                    processList = NewFileTabServlet.getConnector().getProcess(1, "zip");
                    json = new Gson().toJson(fastQCResults.isFinishedDownload(processList, loc));
                    break;
                default:
                    json = new Gson().toJson(null);
                    break;
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
