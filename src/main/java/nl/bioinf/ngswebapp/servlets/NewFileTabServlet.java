package nl.bioinf.ngswebapp.servlets;

import com.google.gson.Gson;
import nl.bioinf.ngswebapp.config.WebConfig;
import nl.bioinf.ngswebapp.dao.DatabaseException;
import nl.bioinf.ngswebapp.dao.VerySimpleDbConnector;
import nl.bioinf.ngswebapp.model.FastqFiles;
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

@WebServlet(name = "NewFileTabServlet", urlPatterns = "/new-files", loadOnStartup = 1)
public class NewFileTabServlet extends HttpServlet {
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
        try {
            ctx.setVariable("projects", connector.getProjectsFromUser(1).keySet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        templateEngine.process("new-files", ctx, response.getWriter());
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String resourcePath = request.getServletContext().getInitParameter("file.location");
        ArrayList<ArrayList<String>> listOfFiles = FastqFiles.getFiles(resourcePath);
        String json = new Gson().toJson(listOfFiles);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    public static VerySimpleDbConnector getConnector() {
        return connector;
    }
}
