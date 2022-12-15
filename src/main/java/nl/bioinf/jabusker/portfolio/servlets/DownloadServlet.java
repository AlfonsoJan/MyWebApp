package nl.bioinf.jabusker.portfolio.servlets;

import nl.bioinf.jabusker.portfolio.model.TarGzipExample1;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@WebServlet(name = "DownloadServlet", urlPatterns = "/download", loadOnStartup = 1)
public class DownloadServlet extends HttpServlet {
    private String fileName;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] allFiles = request.getParameterValues("allFiles");
        String resourcePath = request.getServletContext().getRealPath("/WEB-INF/resources/");

        if (allFiles.length == 1) {
            fileName = allFiles[0];
        }
        else if (allFiles.length > 1) {
            fileName = "placeholder.tar.gz";

            TarGzipExample1 zipper = new TarGzipExample1(resourcePath, allFiles);

            resourcePath = resourcePath + "/temp";

            List<Path> paths = zipper.fileNamesToPaths();
            Path outPath = Paths.get(resourcePath + fileName);

            zipper.createTarGzipFiles(paths, outPath);
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
