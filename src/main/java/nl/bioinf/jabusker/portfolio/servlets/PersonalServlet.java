package nl.bioinf.jabusker.portfolio.servlets;

import nl.bioinf.jabusker.portfolio.config.WebConfig;
import nl.bioinf.jabusker.portfolio.dao.DatabaseException;
import nl.bioinf.jabusker.portfolio.dao.VerySimpleDbConnector;
import nl.bioinf.jabusker.portfolio.db_objects.LabeledFile;
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
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@WebServlet(name = "PersonalServlet", urlPatterns = "/personal_tab", loadOnStartup = 1)
public class PersonalServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private static VerySimpleDbConnector connector;

    @Override
    public void init(){
        this.templateEngine = WebConfig.getTemplateEngine();
        try {
            connector = new VerySimpleDbConnector();
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());

        Map<Project, ArrayList<LabeledFile>> projectLabeledFileHashMap;
        try {
            projectLabeledFileHashMap = connector.getProjectsFromUser(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ArrayList<Project> projects = new ArrayList<>();
        for (Project project : projectLabeledFileHashMap.keySet()) {
            for (LabeledFile labeledFile : projectLabeledFileHashMap.get(project)) {
                project.addLabeledFile(labeledFile);
            }
            projects.add(project);
        }
        ctx.setVariable("projects", projects);

        templateEngine.process("home/personal", ctx, response.getWriter());
    }

    public static VerySimpleDbConnector getConnector() {
        return connector;
    }
}
