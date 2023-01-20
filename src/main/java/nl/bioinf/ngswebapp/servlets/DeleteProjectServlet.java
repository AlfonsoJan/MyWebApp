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

@WebServlet(name = "DeleteProjectServlet", urlPatterns = "/deleteproject")
public class DeleteProjectServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameterValues("id[]")[0]);
        try {
            VerySimpleDbConnector connector = AllPersonalProjectsServlet.getConnector();
            ArrayList<LabeledFile> files = connector.getLabelFilesFromProject(id);
            for (LabeledFile file : files) {
                connector.deleteLabeledFile(file.getFileId());
            }
            connector.deleteProject(id);
        } catch (DatabaseException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
