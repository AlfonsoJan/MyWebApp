package nl.bioinf.jabusker.portfolio.servlets;

import nl.bioinf.jabusker.portfolio.dao.DatabaseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "AddFileToProjectServlet", urlPatterns = "/addfilestoproject", loadOnStartup = 1)
public class AddFileToProjectServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String projectName = request.getParameterValues("projectname[]")[0];
        String[] files = request.getParameterValues("files[]");
        int projectId;
        try {
            projectId = MockupServlet.getConnector().getProject(projectName, 1).getProjectId();
        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        try {
            for (String path : files) {
                MockupServlet.getConnector().insertLabeledFile(path, path, projectId);
            }
        } catch (DatabaseException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }

    }
}