package nl.bioinf.ngswebapp.servlets;
/**
 * A servlet is for the site all download
 * @author John Busker
 * @version 1.0
 */


import com.google.gson.Gson;
import nl.bioinf.ngswebapp.config.WebConfig;
import nl.bioinf.ngswebapp.dao.DatabaseException;
import nl.bioinf.ngswebapp.dao.DatabaseConnector;
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

@WebServlet(name = "AllDownloadServlet", urlPatterns = "/all-downloads")
public class AllDownloadServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private static DatabaseConnector connector;
    @Override
    public void init(){
        this.templateEngine = WebConfig.getTemplateEngine();
        try {
            connector = new DatabaseConnector();
        } catch (DatabaseException | IOException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    @Serial
    private static final long serialVersionUID = 1L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WebContext ctx = new WebContext(request, response, request.getServletContext(), request.getLocale());
        templateEngine.process("all-download", ctx, response.getWriter());
    }

    /**
     * This return all the process dynamically
     * @param request   an {@link HttpServletRequest} object that
     *                  contains the request the client has made
     *                  of the servlet
     *
     * @param response  an {@link HttpServletResponse} object that
     *                  contains the response the servlet sends
     *                  to the client
     *
     * @throws IOException
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ArrayList<Process> processList;
        try {
            processList = connector.getProcess(1, "zip");
            String json = new Gson().toJson(processList);
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
