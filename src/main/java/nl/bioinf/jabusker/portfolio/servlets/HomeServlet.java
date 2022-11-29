package nl.bioinf.jabusker.portfolio.servlets;

import nl.bioinf.jabusker.portfolio.config.WebConfig;
import nl.bioinf.jabusker.portfolio.model.FileWithDate;
import nl.bioinf.jabusker.portfolio.model.FilesSorter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


@WebServlet(name = "WelcomeServlet", urlPatterns = "/welcome", loadOnStartup = 1)
public class HomeServlet extends HttpServlet {

    private TemplateEngine templateEngine;

    @Override
    public void init(){
        this.templateEngine = WebConfig.getTemplateEngine();
    }
    private static final long serialVersionUID = 1L;
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException{
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());
        ArrayList<FilesSorter> fsList = new ArrayList<>();
        FileWithDate f = new FileWithDate();
        // System.out.println(f.getFiles());
//        try {
//            for (int i = 0; i < 50; i++) {
//                int year = ThreadLocalRandom.current().nextInt(2014, 2022 + 1);
//                int month = ThreadLocalRandom.current().nextInt(1, 12 + 1);
//                int day = ThreadLocalRandom.current().nextInt(0, 28 + 1);
//                String date = year + "-" + month + "-" + day;
//                fsList.add(new FilesSorter("placeholder_filename.fastq", date));
//            }
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
        Collections.sort(f.getFiles(), Collections.reverseOrder());
        ctx.setVariable("checkMap", f.getFiles());
        System.out.println(f.getFiles());
        templateEngine.process("home/index", ctx, response.getWriter());
    }
}
