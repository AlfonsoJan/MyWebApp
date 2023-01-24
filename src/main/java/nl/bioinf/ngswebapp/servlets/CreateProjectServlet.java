package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.dao.DatabaseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CreateProjectServlet", urlPatterns = "/createproject")
public class CreateProjectServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String projectName = request.getParameter("projectname");
        String[] files = request.getParameterValues("files[]");
        int projectId;
        try {
            NewFileTabServlet.getConnector().insertProject(projectName, 1);
            if (files == null || files.length < 1) return;
            projectId = NewFileTabServlet.getConnector().getProject(projectName, 1).getProjectId();
            for (String path : files) {
                NewFileTabServlet.getConnector().insertLabeledFile(path, path, projectId);
            }
        } catch (DatabaseException | SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
