package nl.bioinf.jabusker.portfolio.servlets;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static nl.bioinf.jabusker.portfolio.model.TarZipMultipleFiles.createTarGzipFile;

@WebServlet(name = "DownloadServlet", urlPatterns = "/download", loadOnStartup = 1)
public class DownloadServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] allFiles = request.getParameterValues("allFiles");
        String resourcePath = request.getServletContext().getRealPath("/WEB-INF/resources/");
        String sessionId = request.getSession(true).getId();

        String fileName;
        if (allFiles.length == 1) {
            fileName = allFiles[0];
        }
        else {
            fileName = sessionId + ".tar.gz";

            createTarGzipFile(allFiles, resourcePath, resourcePath + "/temp" + fileName);
        }

        FileInputStream fileInputStream = null;
        OutputStream responseOutputStream = null;

        try
        {
            String filePath = resourcePath + fileName;
            File file = new File(filePath);

            String mimeType = request.getServletContext().getMimeType(filePath);
            if (mimeType == null) {
                mimeType = "application/octet-stream";
            }
            response.setContentType(mimeType);
            response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setContentLength((int) file.length());

            fileInputStream = new FileInputStream(file);
            responseOutputStream = response.getOutputStream();
            int bytes;
            while ((bytes = fileInputStream.read()) != -1) {
                responseOutputStream.write(bytes);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            assert fileInputStream != null;
            fileInputStream.close();
            assert responseOutputStream != null;
            responseOutputStream.close();
        }
    }
}
