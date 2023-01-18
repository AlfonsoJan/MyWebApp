package nl.bioinf.ngswebapp.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import nl.bioinf.ngswebapp.db_objects.Process;
import nl.bioinf.ngswebapp.model.AnalyseInfo;
import nl.bioinf.ngswebapp.service.FastQCResultsParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//@WebServlet(name = "CheckAnalyseDoneServlet", urlPatterns = "/analysedone")
//public class CheckAnalyseDoneServlet extends HttpServlet {
//    String id = "78992c54-4bdd-4bbb-9854-1f8f1da6475d";
//
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String[] uniqueIDs = request.getParameterValues("id[]");
//    }
//}

@WebServlet(name = "CheckAnalyseDoneServlet", urlPatterns = "/analysedone")
public class CheckAnalyseDoneServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        ArrayList<AnalyseInfo> analyseInfos = new ArrayList<>();
//        analyseInfos.add(new AnalyseInfo("Project 2",
//                new String[]{"clock_10K_R1.fastq.gz", "clock_10K_R2.fastq.gz"},
//                "78992c54-4bdd-4bbb-9854-1f8f1da6475d"));
//        analyseInfos.add(new AnalyseInfo("Project 1",
//                new String[]{"clock_10K_R1.fastq.gz", "clock_10K_R2.fastq.gz", "Solid_R1.fastq.gz", "Solid_R2.fastq.gz", "SRR494099.fastq.gz"},
//                "8d2900dc-5347-417b-8862-edb490804511"));
//        FastQCResultsParser fastQCResultsParser = new FastQCResultsParser();
//        List<List<String>> results = fastQCResultsParser.isFinished(analyseInfos);
//
//        String json = new Gson().toJson(results);
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(json);
    }
}
