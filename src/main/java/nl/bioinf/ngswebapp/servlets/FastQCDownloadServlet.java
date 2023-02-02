package nl.bioinf.ngswebapp.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@WebServlet(name = "FastQCDownloadServlet", urlPatterns = "/fastqc.results")
public class FastQCDownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("unique");
        String projectName = request.getParameter("projectname");
        String location = getServletContext().getInitParameter("analyse.folder");
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=" + projectName);
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
        zipDir(zos, location,code, projectName);
        zos.close();
    }
    public void zipDir(ZipOutputStream zos, String loc, String code, String projectName) throws IOException {
        File[] filesList = new File(loc + code).listFiles();
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        if (filesList != null) {
            for (File file: filesList) {
                if (file.isFile() && file.getName().endsWith(".html")) {
                    FileInputStream fis = new FileInputStream(file);
                    ZipEntry anEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(anEntry);
                    while((bytesIn = fis.read(readBuffer)) != -1)
                    {
                        zos.write(readBuffer, 0, bytesIn);
                    }
                    fis.close();
                }
            }
        }
    }
}