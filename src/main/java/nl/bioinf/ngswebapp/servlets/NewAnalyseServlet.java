package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.config.WebConfig;
import nl.bioinf.ngswebapp.dao.VerySimpleDbConnector;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "NewAnalyseServlet", urlPatterns = "/new-analyse")
public class NewAnalyseServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private static VerySimpleDbConnector connector;

    @Override
    public void init(){
        this.templateEngine = WebConfig.getTemplateEngine();
//        try {
//            connector = new VerySimpleDbConnector();
//        } catch (DatabaseException | IOException | NoSuchFieldException e) {
//            throw new RuntimeException(e);
//        }
    }
    @Serial
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());
        // Placeholder
        Map<String, Object> projectsMap = new HashMap<>();
        projectsMap.put("Project 1", new String[]{"testplaceholder0.fastq.gz", "testplaceholder1.fastq.gz", "testplaceholder2.fastq.gz"});
        projectsMap.put("Project 2", new String[]{"testplaceholder0.fastq.gz", "testplaceholder1.fastq.gz"});
        ctx.setVariable("projectInfo", projectsMap);
        templateEngine.process("new-analyse", ctx, response.getWriter());
    }

    public static VerySimpleDbConnector getConnector() {
        return connector;
    }
}
