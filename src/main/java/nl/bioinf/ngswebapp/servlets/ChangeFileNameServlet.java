package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.dao.DatabaseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ChangeFileNameServlet", urlPatterns = "/changefilename")
public class ChangeFileNameServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String newName = request.getParameterValues("newName[]")[0];
        int id = Integer.parseInt(request.getParameterValues("id[]")[0]);
        try {
            AllPersonalFilesServlet.getConnector().updateLabelName(newName, id);
        } catch (DatabaseException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}