package nl.bioinf.ngswebapp.servlets;

import nl.bioinf.ngswebapp.dao.DatabaseException;
import nl.bioinf.ngswebapp.dao.DatabaseConnector;
import nl.bioinf.ngswebapp.db_objects.LabeledFile;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;

@WebServlet(name = "DeleteFileServlet", urlPatterns = "/delete")
public class DeleteServlet extends HttpServlet {
    private static DatabaseConnector connector;
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String type = request.getParameter("type");
        String location = getServletContext().getInitParameter("analyse.folder");
        connector = NewFileTabServlet.getConnector();
        try {
            ArrayList<String> unique;
            switch (type) {
                case "files":
                    boolean isLastFile = connector.deleteFileFromProject(1, id);
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");;
                    response.getWriter().write(String.valueOf(isLastFile));
                    break;
                case "files.all":
                    int projectID = connector.getProjectIdFromFile(id);
                    unique = connector.getUniqueCodeAll(projectID);
                    deleteDownloadFiles(location, unique);
                    deleteAnalyseFolder(location, unique);
                    connector.deleteWholeProjectUsingFileID(1, id);
                    break;
                case "project":
                    boolean existAnalyse = connector.checkIfProjectHasAnalyse(1, id);
                    if (!existAnalyse) {
                        unique = connector.getUniqueCodeAll(id);
                        deleteDownloadFiles(location, unique);
                        deleteAnalyseFolder(location, unique);
                        connector.deleteProject(1, id);
                    }
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");;
                    response.getWriter().write(String.valueOf(existAnalyse));
                    break;
                case "project.all":
                    unique = connector.getUniqueCodeAll(id);
                    deleteDownloadFiles(location, unique);
                    deleteAnalyseFolder(location, unique);
                    connector.deleteProject(1, id);
                    break;
                case "process":
                    String unique_string = request.getParameter("unique");
                    connector.deleteProcessUnique(unique_string);
                    deleteDownloadFiles(location, unique_string);
                    deleteAnalyseFolder(location, unique_string);
                    break;
                default:
                    break;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAnalyseFolder(String location, String uniqueCode) throws IOException {
        Path pathFolder = Paths.get(location + uniqueCode);
        if (Files.exists(pathFolder)) {
            Files.walk(pathFolder).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
        }
    }

    public void deleteAnalyseFolder(String location, ArrayList<String> uniqueCode) throws IOException {
        for (String unique: uniqueCode) {
            Path pathFolder = Paths.get(location + unique);
            if (Files.exists(pathFolder)) {
                Files.walk(pathFolder).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
            }
        }
    }

    public void deleteDownloadFiles(String location, String uniqueCode) throws IOException {
        Path pathFile = Paths.get(location + uniqueCode + ".tar.gz");
        Path pathLog = Paths.get(location + uniqueCode + ".output.log");
        Path pathError = Paths.get(location + uniqueCode + ".error.log");
        if (Files.exists(pathFile)) {
            Files.delete(pathFile);
        }
        if (Files.exists(pathLog)) {
            Files.delete(pathLog);
        }
        if (Files.exists(pathError)) {
            Files.delete(pathError);
        }
    }
    public void deleteDownloadFiles(String location, ArrayList<String> uniqueCode) throws IOException {
        for (String unique: uniqueCode) {
            Path pathFile = Paths.get(location + unique + ".tar.gz");
            Path pathLog = Paths.get(location + unique + ".output.log");
            Path pathError = Paths.get(location + unique + ".error.log");
            if (Files.exists(pathFile)) {
                Files.delete(pathFile);
            }
            if (Files.exists(pathLog)) {
                Files.delete(pathLog);
            }
            if (Files.exists(pathError)) {
                Files.delete(pathError);
            }
        }
    }
}
