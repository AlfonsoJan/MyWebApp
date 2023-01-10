package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.dao.DatabaseException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteFileServlet", urlPatterns = "/deletefile")
public class DeleteFileServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(request.getParameterValues("a[]")[0]);
        //int id = Integer.parseInt(request.getParameter("id"));
//        try {
//            System.out.println(id);
//            AllPersonalFilesServlet.getConnector().deleteLabeledFile(1);
//        } catch (DatabaseException e) {
//            System.out.println(e);
//            throw new RuntimeException(e);
//        }
    }
}
