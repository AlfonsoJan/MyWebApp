package nl.bioinf.ngswebapp.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ChangeFileNameFromProjectServlet", urlPatterns = "/changefilenamefromproject")
public class ChangeFileNameFromProjectServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String newName = request.getParameterValues("newName[]")[0];
        int id = Integer.parseInt(request.getParameterValues("id[]")[0]);
        try {
            AllPersonalProjectsServlet.getConnector().updateLabelName(newName, id);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }
}
