package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.config.WebConfig;
import nl.bioinf.ngswebapp.dao.VerySimpleDbConnector;
import nl.bioinf.ngswebapp.model.AnalyseInfo;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serial;
import java.util.ArrayList;

@WebServlet(name = "AnalyseServlet", urlPatterns = "/analyse")
public class AnalyseServlet extends HttpServlet {
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
        ArrayList<AnalyseInfo> analyseInfos = new ArrayList<>();
        analyseInfos.add(new AnalyseInfo("Project 1",
                new String[]{"testplaceholder0.fastq.gz", "testplaceholder1.fastq.gz", "testplaceholder2.fastq.gz"},
                "8429187498412987189718917"));
        analyseInfos.add(new AnalyseInfo("Project 2",
                new String[]{"testplaceholder0.fastq.gz", "testplaceholder1.fastq.gz"},
                "9822897298284952596359"));
        ctx.setVariable("analyseInfo", analyseInfos);
        templateEngine.process("analyse", ctx, response.getWriter());
    }

    public static VerySimpleDbConnector getConnector() {
        return connector;
    }
}
