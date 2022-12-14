package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.dao.DatabaseException;
import nl.bioinf.ngswebapp.dao.VerySimpleDbConnector;
import nl.bioinf.ngswebapp.db_objects.LabeledFile;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "DeleteFileServlet", urlPatterns = "/deletefile")
public class DeleteFileServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameterValues("id[]")[0]);
        try {
            VerySimpleDbConnector connector = AllPersonalFilesServlet.getConnector();
            LabeledFile file = connector.getLabeledFile(id);
            connector.deleteLabeledFile(id);
            if (connector.getLabelFilesFromProject(file.getProjectId()).size() < 1) {
                connector.deleteProject(file.getProjectId());
            }
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
