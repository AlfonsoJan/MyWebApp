package nl.bioinf.ngswebapp.servlets;
/**
 * A servlet to add files to your account
 * @author John Busker
 * @version 1.0
 */

import nl.bioinf.ngswebapp.dao.DatabaseConnector;
import nl.bioinf.ngswebapp.dao.DatabaseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "CreateProjectServlet", urlPatterns = "/add-files-account")
public class CreateProjectServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String projectName = request.getParameter("projectname").strip();
        String[] files = request.getParameterValues("files[]");
        String type = request.getParameter("type");
        DatabaseConnector connector = NewFileTabServlet.getConnector();
        try {
            if (type.equals("create")) {
                boolean existProject = connector.checkIfProjectExist(1, projectName);
                if (!existProject) {
                    connector.createProject(projectName);
                    connector.insertProject(1, projectName, files);
                }
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(String.valueOf(existProject));
            } else if (type.equals("add")) {
                connector.insertProject(1, projectName, files);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
