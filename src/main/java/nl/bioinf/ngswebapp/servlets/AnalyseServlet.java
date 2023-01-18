package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.config.WebConfig;
import nl.bioinf.ngswebapp.dao.DatabaseException;
import nl.bioinf.ngswebapp.dao.VerySimpleDbConnector;
import nl.bioinf.ngswebapp.db_objects.Process;
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
import java.util.stream.Collectors;

@WebServlet(name = "AnalyseServlet", urlPatterns = "/analyse")
public class AnalyseServlet extends HttpServlet {
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

        ArrayList<Process> processes;
        try {
            processes = connector.getProcessFromUser(1, "fastqc");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ctx.setVariable("processes", processes);
        templateEngine.process("analyse", ctx, response.getWriter());
    }

    public static VerySimpleDbConnector getConnector() {
        return connector;
    }
}
