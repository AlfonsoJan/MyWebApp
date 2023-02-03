package nl.bioinf.ngswebapp.servlets;
/**
 * A servlet to change the name
 * @author John Busker
 * @version 1.0
 */

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ChangeNameServlet", urlPatterns = "/changename")
public class ChangeNameServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String newName = request.getParameter("newName");
        String type = request.getParameter("type");
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            if (type.equals("file")) {
                NewFileTabServlet.getConnector().updateLabelName(newName, id);
            } else if (type.equals("project")) {
                NewFileTabServlet.getConnector().updateProjectName(newName, id);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}