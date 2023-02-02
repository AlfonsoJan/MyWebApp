package nl.bioinf.ngswebapp.servlets;

import com.google.gson.Gson;
import nl.bioinf.ngswebapp.config.WebConfig;
import nl.bioinf.ngswebapp.dao.DatabaseException;
import nl.bioinf.ngswebapp.dao.DatabaseConnector;
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
import java.util.List;
import java.util.Map;

@WebServlet(name = "NewAnalyseServlet", urlPatterns = "/new-analyse")
public class NewAnalyseServlet extends HttpServlet {

    private Map<String, String> analyseOptions  = new HashMap<String, String>() {{
        put("FastQC", "Quality Control Tool");
    }};
    private TemplateEngine templateEngine;
    private static DatabaseConnector connector;

    @Serial
    private static final long serialVersionUID = 1L;
    @Override
    public void init(){
        this.templateEngine = WebConfig.getTemplateEngine();
        try {
            connector = new DatabaseConnector();
        } catch (DatabaseException | IOException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());
        ctx.setVariable("options", analyseOptions);
        templateEngine.process("new-analyse", ctx, response.getWriter());
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Project> projectListAnalyse;
        try {
            projectListAnalyse = connector.getAllProjects(1);
            String json = new Gson().toJson(projectListAnalyse);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseConnector getConnector() {
        return connector;
    }
}
