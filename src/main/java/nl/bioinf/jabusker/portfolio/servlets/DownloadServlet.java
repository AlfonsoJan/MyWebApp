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

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] allFiles = request.getParameterValues("allFiles");

        if (allFiles.length > 1) {
            // remove this when we get the actual file location and add that to tarzip model
            String resourcePath = getServletContext().getRealPath("") + "/WEB-INF/resources/";

            TarGzipExample1 zipper = new TarGzipExample1(resourcePath, allFiles);

            List<Path> paths = zipper.fileNamesToPaths();
            Path outPath = Paths.get(resourcePath + "temp/out.tar.gz");

            zipper.createTarGzipFiles(paths, outPath);

            FileInputStream fileInputStream = null;
            OutputStream responseOutputStream = null;

            try
            {
                String filePath = String.valueOf(outPath);
                File file = new File(filePath);

                String mimeType = request.getServletContext().getMimeType(filePath);
                if (mimeType == null) {
                    mimeType = "application/octet-stream";
                }
                response.setContentType(mimeType);
                response.addHeader("Content-Disposition", "attachment; filename=" + "out.tar.gz");
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
                fileInputStream.close();
                responseOutputStream.close();
            }
        }
        else {
            FileInputStream fileInputStream = null;
            OutputStream responseOutputStream = null;

            try
            {
                String filePath = request.getServletContext().getRealPath("/WEB-INF/resources/") + allFiles[0];
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
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            finally
            {
                fileInputStream.close();
                responseOutputStream.close();
            }
        }


    }
}
