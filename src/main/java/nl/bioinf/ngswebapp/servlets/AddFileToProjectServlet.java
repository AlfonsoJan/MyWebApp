package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.dao.DatabaseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "AddFileToProjectServlet", urlPatterns = "/addfilestoproject")
public class AddFileToProjectServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String projectName = request.getParameterValues("projectname[]")[0];
        String[] files = request.getParameterValues("files[]");
        int projectId;
        try {
            projectId = NewFileTabServlet.getConnector().getProject(projectName, 1).getProjectId();
        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        try {
            for (String path : files) {
                NewFileTabServlet.getConnector().insertLabeledFile(path, path, projectId);
            }
        } catch (DatabaseException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }

    }
}