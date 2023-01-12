package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.dao.VerySimpleDbConnector;
import nl.bioinf.ngswebapp.db_objects.Process;
import nl.bioinf.ngswebapp.service.JobRunner;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@WebServlet(name = "DownloadServlet", urlPatterns = "/download")
public class DownloadServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] allFiles = request.getParameterValues("allFiles");
        System.out.println("B");
        int projectId = Integer.parseInt(request.getParameter("project"));
        System.out.println("AAA");
        String resourcePath = request.getServletContext().getInitParameter("resourcePath");
        System.out.println(allFiles.length);
        FileInputStream fileInputStream = null;
        OutputStream responseOutputStream = null;
        if (allFiles.length == 1) {
            try {
                String filePath = resourcePath + allFiles[0];
                File file = new File(filePath);

                String mimeType = request.getServletContext().getMimeType(filePath);
                if (mimeType == null) {
                    mimeType = "application/octet-stream";
                }
                response.setContentType(mimeType);
                response.addHeader("Content-Disposition", "attachment; filename=" + allFiles[0]);
                response.setContentLength((int) file.length());

                fileInputStream = new FileInputStream(file);
                responseOutputStream = response.getOutputStream();
                int bytes;
                while ((bytes = fileInputStream.read()) != -1) {
                    responseOutputStream.write(bytes);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                assert fileInputStream != null;
                fileInputStream.close();
                assert responseOutputStream != null;
                responseOutputStream.close();
            }
        } else if (allFiles.length > 1) {
            Process process;
            System.out.println("YE");
            try {
                System.out.println("A");
                VerySimpleDbConnector connector = NewFileTabServlet.getConnector();
                System.out.println("B");
                process = connector.insertProcess("download", projectId);
                System.out.println("C");
            } catch (Exception e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
            JobRunner zipper = new JobRunner(process.getId(), resourcePath, allFiles, "zip");
            zipper.startJob();
        }
    }
}
