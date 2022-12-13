package nl.bioinf.jabusker.portfolio.servlets;

import nl.bioinf.jabusker.portfolio.config.WebConfig;
import nl.bioinf.jabusker.portfolio.model.FilesSorter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.File;


@WebServlet(name = "DownloadServlet", urlPatterns = "/download", loadOnStartup = 1)
public class DownloadServlet extends HttpServlet {
    private TemplateEngine templateEngine;

    @Override
    public void init(){
        this.templateEngine = WebConfig.getTemplateEngine();
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
                fsList.add(new FilesSorter("placeholder_filename.fastq", date));
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Collections.sort(fsList, Collections.reverseOrder());
        ctx.setVariable("filesList", fsList);
        ctx.setVariable("testFiles", fsList);
        templateEngine.process("home/downloadtest", ctx, response.getWriter());
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] myJsonData = request.getParameterValues("json[]");
        System.out.println("Hi");
        for (String fileName: myJsonData) {

            FileInputStream fileInputStream = null;
            OutputStream responseOutputStream = null;

            try
            {
                String filePath = request.getServletContext().getRealPath("/WEB-INF/resources/")+ fileName;
                File file = new File(filePath);

                String mimeType = request.getServletContext().getMimeType(filePath);
                if (mimeType == null) {
                    mimeType = "application/octet-stream";
                }
                response.setContentType(mimeType);
                response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
                response.setContentLength((int) file.length());

                fileInputStream = new FileInputStream(file);
                responseOutputStream = response.getOutputStream();
                int bytes;
                while ((bytes = fileInputStream.read()) != -1) {
                    responseOutputStream.write(bytes);
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            finally
            {
                fileInputStream.close();
                responseOutputStream.close();
            }
        }
    }
}
