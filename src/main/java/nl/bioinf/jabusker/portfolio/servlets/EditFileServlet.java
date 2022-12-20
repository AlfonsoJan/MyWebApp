package nl.bioinf.jabusker.portfolio.servlets;

import org.w3c.dom.ls.LSOutput;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "EditFileServlet", urlPatterns = "/editfile", loadOnStartup = 1)
public class EditFileServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] projectName = request.getParameterValues("fileInfo[]");
        String filename = projectName[0];
        String read = projectName[1];
        String label = projectName[2];
        System.out.println(filename + read + label);
    }

}
