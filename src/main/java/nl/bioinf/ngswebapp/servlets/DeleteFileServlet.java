package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.dao.DatabaseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteFileServlet", urlPatterns = "/deletefile")
public class DeleteFileServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameterValues("id[]")[0]);
        try {
            AllPersonalFilesServlet.getConnector().deleteLabeledFile(id);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
    }
}
