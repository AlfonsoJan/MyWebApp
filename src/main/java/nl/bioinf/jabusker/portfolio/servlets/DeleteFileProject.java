package nl.bioinf.jabusker.portfolio.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteFileServlet", urlPatterns = "/deletefile", loadOnStartup = 1)
public class DeleteFileProject extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fileName = request.getParameter("fileName");
        System.out.println(fileName);
    }

}
