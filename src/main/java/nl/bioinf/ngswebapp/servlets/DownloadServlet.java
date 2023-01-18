package nl.bioinf.ngswebapp.servlets;

import com.mysql.cj.util.StringUtils;
import nl.bioinf.ngswebapp.dao.DatabaseException;
import nl.bioinf.ngswebapp.dao.VerySimpleDbConnector;
import nl.bioinf.ngswebapp.service.JobRunner;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@WebServlet(name = "DownloadServlet", urlPatterns = "/download")
public class DownloadServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] allFiles = request.getParameterValues("allFiles");
        String[] projectFiles = request.getParameterValues("projectFiles[]");
        int projectId = StringUtils.isNullOrEmpty(request.getParameter("project")) ? -1 : Integer.parseInt(request.getParameter("project"));
        String resourcePath = request.getServletContext().getInitParameter("file.location");
        String outPath = request.getServletContext().getInitParameter("analyse.folder");

        if (allFiles != null) {
            FileInputStream fileInputStream = null;
            OutputStream responseOutputStream = null;
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
        } else {
            UUID randomID;
            while (true) {
                randomID = UUID.randomUUID();
                Path path = Path.of(outPath + randomID + ".tar.gz");
                if (Files.notExists(path)) {
                    break;
                }
            }
            String analyseType = "zip".toLowerCase();
            try {
                VerySimpleDbConnector connector = AllPersonalProjectsServlet.getConnector();
                connector.insertProcess(analyseType, projectId, randomID.toString());
            } catch (DatabaseException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            JobRunner zipper = new JobRunner(randomID, resourcePath, projectFiles, "zipper", outPath);
            zipper.startJob();
        }
    }
}