package nl.bioinf.jabusker.portfolio.servlets;

import nl.bioinf.jabusker.portfolio.config.WebConfig;
import nl.bioinf.jabusker.portfolio.model.FilesSorter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.File;

import java.io.OutputStream;
import java.util.Arrays;

@WebServlet(name = "DownloadServlet", urlPatterns = "/download", loadOnStartup = 1)
public class DownloadServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        for (String filename: request.getParameterValues("allfiles")) {
            System.out.println(filename);
        }

        // String[] myJsonData = request.getParameterValues("json[]");
//        for (String fileName: myJsonData) {
//
//            System.out.println(fileName);
//
//            FileInputStream fileInputStream = null;
//            OutputStream responseOutputStream = null;
//
//            try
//            {
//                String filePath = request.getServletContext().getRealPath("/WEB-INF/resources/")+ fileName;
//                File file = new File(filePath);
//
//                String mimeType = request.getServletContext().getMimeType(filePath);
//                if (mimeType == null) {
//                    mimeType = "application/octet-stream";
//                }
//                response.setContentType(mimeType);
//                response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
//                response.setContentLength((int) file.length());
//
//                fileInputStream = new FileInputStream(file);
//                responseOutputStream = response.getOutputStream();
//                int bytes;
//                while ((bytes = fileInputStream.read()) != -1) {
//                    responseOutputStream.write(bytes);
//                }
//            }
//            catch(Exception ex)
//            {
//                ex.printStackTrace();
//            }
//            finally
//            {
//                fileInputStream.close();
//                responseOutputStream.close();
//            }
//        }
    }
}
