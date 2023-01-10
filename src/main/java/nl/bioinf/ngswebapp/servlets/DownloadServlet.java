package nl.bioinf.ngswebapp.servlets;

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
        String resourcePath = request.getServletContext().getInitParameter("resourcePath");

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
            JobRunner zipper = new JobRunner(UUID.randomUUID(), resourcePath, allFiles, "zip");
            zipper.startJob();
        }
    }
}
