package nl.bioinf.jabusker.portfolio.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteProjectServlet", urlPatterns = "/projectdelete", loadOnStartup = 1)
public class DeleteProjectServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String projectName = request.getParameter("projectName");
        System.out.println(projectName);
    }

}
