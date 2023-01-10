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
import java.util.ArrayList;

@WebServlet(name = "AllPersonalFilesServlet", urlPatterns = "/all-files")
public class AllPersonalFilesServlet extends HttpServlet {
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
//        try {
//            ctx.setVariable("projects", connector.getProjectsFromUser(1).keySet());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
        ArrayList<String[]> listOfFiles = new ArrayList<>();
        listOfFiles.add(new String[] {"SRR94389.fastq.gz", "Mouse", "1", "testplaceholder0.fastq.gz"});
        listOfFiles.add(new String[] {"SRR97834698346.fastq.gz", "Jabusker", "0", "testplaceholder1.fastq.gz"});
        ctx.setVariable("filesList", listOfFiles);
        templateEngine.process("all-files", ctx, response.getWriter());
    }

    public static VerySimpleDbConnector getConnector() {
        return connector;
    }
}
