package nl.bioinf.jabusker.portfolio.servlets;

import nl.bioinf.jabusker.portfolio.config.WebConfig;
import org.thymeleaf.TemplateEngine;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@WebServlet(name = "DownloadServlet", urlPatterns = "/download", loadOnStartup = 1)
public class DownloadServlet extends HttpServlet {
    private TemplateEngine templateEngine;

    @Override
    public void init(){
        this.templateEngine = WebConfig.getTemplateEngine();
    }
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] myJsonData = request.getParameterValues("json[]");
        for (String i:
                myJsonData) {
            System.out.println(i);
        }
    }
}
