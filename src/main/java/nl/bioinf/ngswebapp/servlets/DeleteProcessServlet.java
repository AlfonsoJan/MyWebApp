package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.dao.DatabaseException;
import nl.bioinf.ngswebapp.dao.VerySimpleDbConnector;
import nl.bioinf.ngswebapp.db_objects.LabeledFile;
import nl.bioinf.ngswebapp.db_objects.Process;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet(name = "DeleteProcessServlet", urlPatterns = "/deleteprocess")
public class DeleteProcessServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameterValues("processid[]")[0]);
        System.out.println(id);
        try {
            VerySimpleDbConnector connector = AnalyseServlet.getConnector();
            connector.deleteProcess(id);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}
