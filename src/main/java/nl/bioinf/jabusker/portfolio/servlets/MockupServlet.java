package nl.bioinf.jabusker.portfolio.servlets;

import nl.bioinf.jabusker.portfolio.config.WebConfig;
import nl.bioinf.jabusker.portfolio.dao.DatabaseException;
import nl.bioinf.jabusker.portfolio.dao.VerySimpleDbConnector;
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

@WebServlet(name = "MockupServlet", urlPatterns = "/welcomeshow", loadOnStartup = 1)
public class MockupServlet extends HttpServlet {
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
        ArrayList<FilesSorter> fsList = new ArrayList<>();
        try {
            for (int i = 0; i < 50; i++) {
                int year = ThreadLocalRandom.current().nextInt(2014, 2022 + 1);
                int month = ThreadLocalRandom.current().nextInt(1, 12 + 1);
                int day = ThreadLocalRandom.current().nextInt(0, 28 + 1);
                String date = year + "-" + month + "-" + day;
                Enums.Used used = Enums.Used.values()[new Random().nextInt(Enums.Used.values().length)];
                fsList.add(new FilesSorter("placeholder_filename.fastq", date, used));
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Collections.sort(fsList, Collections.reverseOrder());
        ctx.setVariable("filesList", fsList);
        ctx.setVariable("testFiles", fsList);
        try {
            ctx.setVariable("projects", connector.getProjectsFromUser(5).keySet());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        templateEngine.process("home/simple", ctx, response.getWriter());
    }

    public static VerySimpleDbConnector getConnector() {
        return connector;
    }
}
