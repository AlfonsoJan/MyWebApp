package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.service.FastQCResultsParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet(name = "CheckAnalyseDoneServlet", urlPatterns = "/analysedone")
//public class CheckAnalyseDoneServlet extends HttpServlet {
//    String id = "78992c54-4bdd-4bbb-9854-1f8f1da6475d";
//
//    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        String[] uniqueIDs = request.getParameterValues("id[]");
//    }
//}

public class CheckAnalyseDoneServlet {
    public static void main(String[] args) {
        String loc = "/students/2022-2023/Thema10/ngs_dummyfiles/temp/";
        String[] id = new String[]{"78992c54-4bdd-4bbb-9854-1f8f1da6475d"};
        FastQCResultsParser fastQCResultsParser = new FastQCResultsParser();
        fastQCResultsParser.isFinished(id, loc);
    }
}
