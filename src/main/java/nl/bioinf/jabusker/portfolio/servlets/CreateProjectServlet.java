package nl.bioinf.jabusker.portfolio.servlets;

import nl.bioinf.jabusker.portfolio.config.WebConfig;
import nl.bioinf.jabusker.portfolio.dao.DatabaseException;
import nl.bioinf.jabusker.portfolio.db_objects.Project;
import nl.bioinf.jabusker.portfolio.model.Enums;
import nl.bioinf.jabusker.portfolio.model.FilesSorter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet(name = "CreateProjectServlet", urlPatterns = "/createproject", loadOnStartup = 1)
public class CreateProjectServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String projectName = request.getParameterValues("projectname[]")[0];

//        String name = request.getParameter("name");
//        String[] files = request.getParameter("files").split(",");
//        boolean con = true;
        try {
            MockupServlet.getConnector().insertProject(projectName, 1);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
//        int projectId;
//        if (con) {
//            try {
//                projectId = MockupServlet.getConnector().getProject(name, 1).getProjectId();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                for (String path : files) {
//                    MockupServlet.getConnector().insertLabeledFile(path, path, projectId);
//                }
//            } catch (DatabaseException e) {
//                throw new RuntimeException(e);
//            }
//        }

    }
}
