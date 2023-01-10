package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.config.WebConfig;
import nl.bioinf.ngswebapp.dao.DatabaseException;
import nl.bioinf.ngswebapp.dao.VerySimpleDbConnector;
import nl.bioinf.ngswebapp.db_objects.LabeledFile;
import nl.bioinf.ngswebapp.db_objects.Project;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "AllPersonalProjectsServlet", urlPatterns = "/all-projects")
public class AllPersonalProjectsServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private static VerySimpleDbConnector connector;

    @Override
    public void init(){
        this.templateEngine = WebConfig.getTemplateEngine();
        try {
            connector = new VerySimpleDbConnector();
        } catch (DatabaseException | IOException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    @Serial
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());
        Map<Project, ArrayList<LabeledFile>> projectLabeledFileHashMap;
        try {
            projectLabeledFileHashMap = connector.getProjectsFromUser(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Project> projectsInfo = new ArrayList<>();
        for (Project project : projectLabeledFileHashMap.keySet()) {
            for (LabeledFile labeledFile : projectLabeledFileHashMap.get(project)) {
                project.addLabeledFile(labeledFile);
            }
            projectsInfo.add(project);
        }
        ctx.setVariable("projects", projectsInfo);
        templateEngine.process("all-projects", ctx, response.getWriter());
    }

    public static VerySimpleDbConnector getConnector() {
        return connector;
    }
}
