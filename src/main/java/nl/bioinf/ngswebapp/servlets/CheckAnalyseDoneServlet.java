package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.service.FastQCResultsParser;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

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
        String analyseInfo = request.getParameter("json");
        System.out.println(analyseInfo);
        //Gson gson = new Gson();
        //System.out.println(request.getParameterMap().keySet());
    }
}
