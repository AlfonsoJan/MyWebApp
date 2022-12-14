package nl.bioinf.jabusker.portfolio.servlets;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet(name = "DownloadServlet", urlPatterns = "/download", loadOnStartup = 1)
public class DownloadServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int queryLength = request.getParameterValues("allFiles").length;

        for (String fileName: request.getParameterValues("allFiles")) {
            System.out.println(fileName);

            FileInputStream fileInputStream = null;
            OutputStream responseOutputStream = null;

            try
            {
                String filePath = request.getServletContext().getRealPath("/WEB-INF/resources/") + fileName;
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
                fileInputStream.close();
                responseOutputStream.close();
            }
        }
    }
}
