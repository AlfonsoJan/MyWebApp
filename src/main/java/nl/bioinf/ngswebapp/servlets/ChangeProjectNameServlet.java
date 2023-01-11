package nl.bioinf.ngswebapp.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ChangeProjectNameServlet", urlPatterns = "/changeproject")
public class ChangeProjectNameServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String newName = request.getParameterValues("newName[]")[0];
        int id = Integer.parseInt(request.getParameterValues("id[]")[0]);
        try {
            AllPersonalProjectsServlet.getConnector().updateProject(newName, id);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
