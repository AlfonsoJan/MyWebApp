package nl.bioinf.ngswebapp.dao;
/**
 * Creates the database connector
 * @author John Busker
 * @version 1.0
 */

import nl.bioinf.ngswebapp.db_objects.Process;
import nl.bioinf.ngswebapp.db_utils.DbCredentials;
import nl.bioinf.ngswebapp.db_utils.DbUser;
import nl.bioinf.ngswebapp.db_objects.LabeledFile;
import nl.bioinf.ngswebapp.db_objects.Project;
import nl.bioinf.ngswebapp.db_objects.User;
import nl.bioinf.ngswebapp.model.Enums;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DatabaseConnector {
    private final String url;
    private final String dbUser;
    private final String dbPassword;
    private Connection connection;

    private final Map<String, PreparedStatement> preparedStatements = new HashMap();

    private static final String GET_USER_USING_ID = "get_user_using_id";
    private static final String GET_USER_USING_NAME = "get_userid";
    private static final String GET_ALL_LABEL_FILES = "get_all_label_files";
    private static final String GET_PROJECT_NAMES_USING_ID = "get_project_names";
    private static final String CHECK_IF_PROJECT_EXIST = "check_if_project_exist";
    private static final String CREATE_PROJECT = "create_project";
    private static final String GET_PROJECT_ID_USING_NAME = "get_project_id_using_filename";
    private static final String CREATE_LABEL_FILE = "create_label_file";
    private static final String GET_LABEL_FILE_ID = "get_label_file_id";
    private static final String INSERT_INTO_USER_PROJECT_FILE = "insert_into_user_project_file";
    private static final String GET_ALL_LABELED_FILES_USER = "get_all_labeled_files_user";
    private static final String UPDATE_FILE = "update_file";
    private static final String UPDATE_PROJECT = "update_project";
    private static final String DELETE_FILE_FROM_USER = "delete_file_from_user";
    private static final String GET_PROJECT_USING_ID = "get_project";
    private static final String GET_PROJECT_USING_PROJECT_ID = "get_project_project_id";
    private static final String INSERT_PROJECT_USING_ID = "insert_project_using_id";
    private static final String CHECK_IF_PROJECT_EMPTY = "check_if_project_empty";
    private static final String DROP_EMPTY_PROJECT = "drop_empty_project";
    private static final String GET_PROJECT_NAME_USING_FILE_ID = "get_project_name_using_file_id";
    private static final String DELETE_FROM_LABELED = "delete_from_labeled";
    private static final String CHECK_IF_PROJECT_HAS_ANALYSE = "check_if_project_has_analyse";
    private static final String INSERT_INTO_PROCESS = "insert_into_process";
    private static final String DELETE_DOWNLOAD_PROCESS = "delete_download_process";
    private static final String GET_PROCESS_USER = "get_process_user";
    private static final String DROP_PROCESS_UNIQUE = "drop_user_unique";
    private static final String GET_PROJECT_FILE_LENGTH = "get_project_file_length";
    private static final String INSERT_USER = "insert_user";
    private static final String GET_PROJECT_ID_USING_FILE_ID = "get_project_id_using_file_id";
    private static final String GET_PROCESS_UNIQUE_ID_USING_PROJECT_ID = "get_process_unique_id_using_project_id";
    private static final String GET_PROCESS_DOWNLOAD_UNIQUE_ID_USING_PROJECT_ID = "get_process_download_unique_id_using_project_id";

    /**
     * a main for demonstration purposes
     *
     * @param args
     */
    public static void main(String[] args) {
        try {

            //connect
            DatabaseConnector connector = new DatabaseConnector();

            //make an user object
            System.out.println(connector.getUser(1));

            //a catch-all for database interaction exceptions
        } catch (DatabaseException | IOException | NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The constructor, this will create a databaseconnection for the information that is within
     * your .my.cnf file!
     *
     * @throws DatabaseException
     * @throws IOException
     * @throws NoSuchFieldException
     */
    public DatabaseConnector() throws DatabaseException, IOException, NoSuchFieldException {
        DbUser credentialsFinder = DbCredentials.getMySQLuser();

        this.url = credentialsFinder.getUrl();;
        this.dbUser = credentialsFinder.getUserName();
        this.dbPassword = credentialsFinder.getDatabasePassword();

        //make the connection
        connect();
    }

    /**
     * A function that does the connecting itself.
     *
     * @throws DatabaseException
     */
    private void connect() throws DatabaseException {
        try {
            //load driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
            //create connection
            connection = DriverManager.getConnection(url, dbUser, dbPassword);
            //prepare statements
            prepareStatements();
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Something is wrong with the database, see cause Exception",
                    e.getCause());
        }
    }

    /**
     * This function creates all preparestatements. When adding new statements, put them in here.
     *
     * @throws SQLException
     */
    private void prepareStatements() throws SQLException {
        String userNameFetchQuery = "SELECT * FROM users WHERE id = ?";
        this.preparedStatements.put(GET_USER_USING_ID, connection.prepareStatement(userNameFetchQuery));
        String userIdFetchQuery = "SELECT * FROM users WHERE username = ?";
        this.preparedStatements.put(GET_USER_USING_NAME, connection.prepareStatement(userIdFetchQuery));
        String userInsertQuery = "INSERT INTO users (username) VALUES (?)";
        this.preparedStatements.put(INSERT_USER, connection.prepareStatement(userInsertQuery));
        String filesFetchQuery = "SELECT * FROM labeled_files where path = ?";
        this.preparedStatements.put(GET_ALL_LABEL_FILES, connection.prepareStatement(filesFetchQuery));
        String projectNamesFetchQuery = "select project_name from user_project_files upf, projects p where p.id = upf.project_id and user_id = ?";
        this.preparedStatements.put(GET_PROJECT_NAMES_USING_ID, connection.prepareStatement(projectNamesFetchQuery));
        String checkIfProjectExist = "select p.project_name from user_project_files upf, projects p where p.id = upf.project_id and user_id = ? and p.project_name = ?";
        this.preparedStatements.put(CHECK_IF_PROJECT_EXIST, connection.prepareStatement(checkIfProjectExist));
        String createProjectQuery = "INSERT INTO projects (project_name) VALUES (?)";
        this.preparedStatements.put(CREATE_PROJECT, connection.prepareStatement(createProjectQuery));
        String projectIDFetchQuery = "select id from projects where project_name = ? order by id desc limit 1";
        this.preparedStatements.put(GET_PROJECT_ID_USING_NAME, connection.prepareStatement(projectIDFetchQuery));
        String createLabeledFileQuery = "INSERT INTO labeled_files (label, path) VALUES (?, ?)";
        this.preparedStatements.put(CREATE_LABEL_FILE, connection.prepareStatement(createLabeledFileQuery));
        String labelFileFetchQuery = "select id from labeled_files where path = ? order by id desc limit 1";
        this.preparedStatements.put(GET_LABEL_FILE_ID, connection.prepareStatement(labelFileFetchQuery));
        String insertIntoUserFileQuery = "INSERT INTO user_project_files VALUES (?, ?, ?)";
        this.preparedStatements.put(INSERT_INTO_USER_PROJECT_FILE, connection.prepareStatement(insertIntoUserFileQuery));
        String allLabeledFilesFetchQuery = "select upf.file_id, lf.label, lf.path from user_project_files upf, labeled_files lf where upf.user_id=? and lf.id=upf.file_id";
        this.preparedStatements.put(GET_ALL_LABELED_FILES_USER, connection.prepareStatement(allLabeledFilesFetchQuery));
        String labelNameUpdateQuery = "UPDATE labeled_files SET label = ? WHERE id = ?";
        this.preparedStatements.put(UPDATE_FILE, connection.prepareStatement(labelNameUpdateQuery));
        String projectNameUpdateQuery = "update projects set project_name = ? where id = ?";
        this.preparedStatements.put(UPDATE_PROJECT, connection.prepareStatement(projectNameUpdateQuery));
        String deleteFileFromUser = "DELETE from user_project_files where user_id = ? and file_id = ?";
        this.preparedStatements.put(DELETE_FILE_FROM_USER, connection.prepareStatement(deleteFileFromUser));
        String projectFetchQuery = "select DISTINCT id, project_name from user_project_files upf, projects p where p.id = upf.project_id and user_id = ?";
        this.preparedStatements.put(GET_PROJECT_USING_ID, connection.prepareStatement(projectFetchQuery));
        String projectFilesFetchQuery = "select lf.id, lf.label, lf.path from user_project_files upf, projects p, labeled_files lf where p.id = upf.project_id and user_id = ? and lf.id=file_id and upf.project_id=?";
        this.preparedStatements.put(INSERT_PROJECT_USING_ID, connection.prepareStatement(projectFilesFetchQuery));
        String checkIfProjectEmpty = "select * from user_project_files where user_id = ? and project_id=?";
        this.preparedStatements.put(CHECK_IF_PROJECT_EMPTY, connection.prepareStatement(checkIfProjectEmpty));
        String deleteEmptyProject = "DELETE FROM projects where id = ?";
        this.preparedStatements.put(DROP_EMPTY_PROJECT, connection.prepareStatement(deleteEmptyProject));
        String getProjectIdUsingFileId = "select project_id from user_project_files where file_id=?";
        this.preparedStatements.put(GET_PROJECT_NAME_USING_FILE_ID, connection.prepareStatement(getProjectIdUsingFileId));
        String deleteFromLabeled = "DELETE FROM labeled_files where id = ?";
        this.preparedStatements.put(DELETE_FROM_LABELED, connection.prepareStatement(deleteFromLabeled));
        String checkIfProjectHasAnalyse = "select p.id, p.id_project, p.unique_id type from user_project_files upf, process p where upf.user_id = ? and p.id_project=upf.project_id and ? = p.id_project and p.type = 'fastqc'";
        this.preparedStatements.put(CHECK_IF_PROJECT_HAS_ANALYSE, connection.prepareStatement(checkIfProjectHasAnalyse));
        String insertIntoProcess = "insert into process (type, unique_id, id_project) values (?, ?, ?)";
        this.preparedStatements.put(INSERT_INTO_PROCESS, connection.prepareStatement(insertIntoProcess));
        String deleteDownloadProcess = "delete from process where id_project = ?";
        this.preparedStatements.put(DELETE_DOWNLOAD_PROCESS, connection.prepareStatement(deleteDownloadProcess));
        String getProcessUser = "select p.id_project, p.id, p.unique_id from process p,user_project_files upf where upf.user_id = ? and type = ? group by p.unique_id";
        this.preparedStatements.put(GET_PROCESS_USER, connection.prepareStatement(getProcessUser));
        String getProject = "select * from projects where id = ?";
        this.preparedStatements.put(GET_PROJECT_USING_PROJECT_ID, connection.prepareStatement(getProject));
        String getProjectFileLength = "select count(*) as file_num from user_project_files where project_id = ?";
        this.preparedStatements.put(GET_PROJECT_FILE_LENGTH, connection.prepareStatement(getProjectFileLength));
        String getProjectIDUsingFileID = "select project_id from user_project_files where file_id = ?";
        this.preparedStatements.put(GET_PROJECT_ID_USING_FILE_ID, connection.prepareStatement(getProjectIDUsingFileID));
        String deleteProcessUniqueId = "delete from process where unique_id = ?";
        this.preparedStatements.put(DROP_PROCESS_UNIQUE, connection.prepareStatement(deleteProcessUniqueId));
        String getUniqueCodeAll = "select unique_id from process where id_project = ?";
        this.preparedStatements.put(GET_PROCESS_UNIQUE_ID_USING_PROJECT_ID, connection.prepareStatement(getUniqueCodeAll));
        String getUniqueCodeDownload = "select unique_id from process where id_project = ? and type = 'zip'";
        this.preparedStatements.put(GET_PROCESS_DOWNLOAD_UNIQUE_ID_USING_PROJECT_ID, connection.prepareStatement(getUniqueCodeDownload));
    }

    /**
     * Gets a user by giving a specific user id.
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public User getUser(int id) throws SQLException {
        // prepare query statement
        PreparedStatement ps = this.preparedStatements.get(GET_USER_USING_ID);

        //set data on the "?" placeholders of the prepared statement
        ps.setString(1, String.valueOf(id));

        //execute
        ResultSet rs = ps.executeQuery();

        //next result
        rs.next();

        //if there is data, process it
        String userName = rs.getString("username");

        return new User(id, userName);
    }

    /**
     * Gets a user by giving a specific username.
     *
     * @param userName
     * @return
     * @throws SQLException
     */
    public User getUser(String userName) throws SQLException {
        // prepare query statement
        PreparedStatement ps = this.preparedStatements.get(GET_USER_USING_NAME);

        //set data on the "?" placeholders of the prepared statement
        ps.setString(1, userName);

        //execute
        ResultSet rs = ps.executeQuery();

        //next result
        rs.next();
        return new User(rs.getInt("id"), userName);
    }

    /**
     * Inserts a new user in the database
     *
     * @param userName
     * @throws DatabaseException
     */
    public void insertUser(String userName) throws DatabaseException {
        try{
            //Prepare statement
            //!! Doing this within this method is extremely inefficient !!
            PreparedStatement ps = this.preparedStatements.get(INSERT_USER);

            //set data on the "?" placeholders of the prepared statement
            ps.setString(1, userName);

            //do the actual insert
            ps.executeUpdate();

            //close resources
            ps.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DatabaseException("Something is wrong with the database, see cause Exception",
                    ex.getCause());
        }
    }

    /**
     * Returns a list with all the unique hash codes of a project
     * @param projectID
     * @return A list with all the unique hash codes of a project
     * @throws SQLException
     */
    public ArrayList<String> getUniqueCodeAll(int projectID) throws SQLException {
        ArrayList<String> uniqueList = new ArrayList<String>();
        PreparedStatement ps = this.preparedStatements.get(GET_PROCESS_UNIQUE_ID_USING_PROJECT_ID);
        ps.setInt(1, projectID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            uniqueList.add(rs.getString("unique_id"));
        }
        return uniqueList;
    }

    /**
     * Return the project id when given a file id
     * @param fileId
     * @return The project id when given a file id
     * @throws SQLException
     */
    public int getProjectIdFromFile(int fileId) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(GET_PROJECT_NAME_USING_FILE_ID);
        ps.setInt(1, fileId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            return rs.getInt("project_id");
        }
        return -1;
    }

    /**
     * Delete a process given the has code
     * @param code
     * @throws SQLException
     */
    public void deleteProcessUnique(String code) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(DROP_PROCESS_UNIQUE);
        ps.setString(1, code);
        ps.executeUpdate();
    }

    /**
     * Deletes the whole project from given a file id
     * @param id
     * @param fileId
     * @throws SQLException
     */
    public void deleteWholeProjectUsingFileID(int id, int fileId) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(GET_PROJECT_ID_USING_FILE_ID);
        ps.setInt(1, fileId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int projectId = rs.getInt("project_id");
        deleteProcess(projectId);

        ps = this.preparedStatements.get(DELETE_FILE_FROM_USER);;
        ps.setInt(1, id);
        ps.setInt(2, fileId);
        ps.executeUpdate();

        ps = this.preparedStatements.get(DELETE_FROM_LABELED);;
        ps.setInt(1, fileId);
        ps.executeUpdate();

        ps = this.preparedStatements.get(DROP_EMPTY_PROJECT);
        ps.setInt(1, projectId);
        ps.executeUpdate();
    }

    /**
     * Returns if the file is the last file from a project
     * @param projectId
     * @return True or false if it is the last file
     * @throws SQLException
     */
    public boolean isLastFIle(int projectId) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(GET_PROJECT_FILE_LENGTH);
        ps.setInt(1, projectId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int fileLength = rs.getInt("file_num");
        return fileLength < 2;
    }

    /**
     * Deletes the file from a project if it is not the last one
     * @param id
     * @param fileId
     * @return
     * @throws SQLException
     */
    public boolean deleteFileFromProject(int id, int fileId) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(GET_PROJECT_NAME_USING_FILE_ID);
        ps.setInt(1, fileId);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int projectId = rs.getInt("project_id");
        if (isLastFIle(projectId)) {
            return true;
        }
        deleteFromProject(id, fileId);
        return false;
    }

    /**
     * Deletes the process given the project id
     * @param projectId
     * @throws SQLException
     */
    public void deleteProcess(int projectId) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(DELETE_DOWNLOAD_PROCESS);
        ps.setInt(1, projectId);
        ps.executeUpdate();
    }

    /**
     * Return a list with all process given a user id and a string type
     * @param id
     * @param type
     * @return List with all process given a user id and a string type
     * @throws SQLException
     */
    public ArrayList<Process> getProcess(int id, String type) throws SQLException {
        ArrayList<Process> results = new ArrayList<>();
        PreparedStatement ps = this.preparedStatements.get(GET_PROCESS_USER);
        ps.setInt(1, id);
        ps.setString(2, type);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Process process;
            int projectId = rs.getInt("id_project");
            ps = this.preparedStatements.get(GET_PROJECT_USING_PROJECT_ID);
            ps.setInt(1, projectId);
            ResultSet projectSet = ps.executeQuery();
            while (projectSet.next()) {
                ArrayList<LabeledFile> labeledFiles = new ArrayList<>();
                String projectName = projectSet.getString("project_name");
                ps = this.preparedStatements.get(INSERT_PROJECT_USING_ID);
                ps.setInt(1, id);
                ps.setInt(2, projectId);
                ResultSet filesResult = ps.executeQuery();
                while (filesResult.next()) {
                    labeledFiles.add(new LabeledFile(filesResult.getInt("id"),
                            filesResult.getString("label"),
                            filesResult.getString("path")));
                }
                Project project = new Project(projectId, projectName, labeledFiles);
                process = new Process(rs.getInt("id"), type, 1, projectId, rs.getString("unique_id"));
                process.setProject(project);
                results.add(process);
            }
        }
        return results;
    }

    /**
     * Insert a process given the type, hash code and project id
     * @param type
     * @param uniqueId
     * @param projectId
     * @throws SQLException
     */
    public void insertProcess(String type, String uniqueId, int projectId) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(INSERT_INTO_PROCESS);
        ps.setString(1, type);
        ps.setString(2, uniqueId);
        ps.setInt(3, projectId);
        ps.executeUpdate();
    }

    /**
     * Return a list with all the file names
     * @param id
     * @param projectId
     * @return A list with all the file names
     * @throws SQLException
     */
    public List<String> getFilesProject(int id, int projectId) throws SQLException {
        List<String> fileList = new ArrayList<>();
        PreparedStatement ps = this.preparedStatements.get(INSERT_PROJECT_USING_ID);
        ps.setInt(1, id);
        ps.setInt(2, projectId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            fileList.add(rs.getString("path"));
        }
        return fileList;
    }

    /**
     * Deletes the project
     * @param id
     * @param projectId
     * @throws SQLException
     */
    public void deleteProject(int id, int projectId) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(DELETE_DOWNLOAD_PROCESS);
        ps.setInt(1, projectId);
        ps.executeUpdate();

        ps = this.preparedStatements.get(INSERT_PROJECT_USING_ID);
        ps.setInt(1, id);
        ps.setInt(2, projectId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int fileId = rs.getInt("id");
            deleteFromProject(id, fileId);
        }
    }

    /**
     * Checks if a project has a analyse
     * @param id
     * @param projectId
     * @return If the project has a analyse
     * @throws SQLException
     */
    public boolean checkIfProjectHasAnalyse (int id, int projectId) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(CHECK_IF_PROJECT_HAS_ANALYSE);
        ps.setInt(1, id);
        ps.setInt(2, projectId);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if a project is empty
     * @param id
     * @param projectId
     * @return If the project is empty
     * @throws SQLException
     */
    public boolean checkIfProjectEmpty (int id, int projectId) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(CHECK_IF_PROJECT_EMPTY);
        ps.setInt(1, id);
        ps.setInt(2, projectId);
        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a list with all the projects
     * @param id
     * @return get all the projects
     * @throws SQLException
     */
    public List<Project> getAllProjects(int id) throws SQLException {
        List<Project> projectList = new ArrayList<>();
        PreparedStatement ps = this.preparedStatements.get(GET_PROJECT_USING_ID);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int projectId = rs.getInt("id");
            ArrayList<LabeledFile> labeledFiles = new ArrayList<>();
            String projectName = rs.getString("project_name");
            ps = this.preparedStatements.get(INSERT_PROJECT_USING_ID);
            ps.setInt(1, id);
            ps.setInt(2, projectId);
            ResultSet filesResult = ps.executeQuery();
            while (filesResult.next()) {
                labeledFiles.add(new LabeledFile(filesResult.getInt("id"),
                        filesResult.getString("label"),
                        filesResult.getString("path")));
            }
            projectList.add(new Project(projectId, projectName, labeledFiles));
        }
        return projectList;
    }

    /**
     * Deletes a file from a project
     * @param id
     * @param fileId
     * @throws SQLException
     */
    public void deleteFromProject(int id, int fileId) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(GET_PROJECT_NAME_USING_FILE_ID);
        ps.setInt(1, fileId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int projectId = rs.getInt("project_id");

            ps = this.preparedStatements.get(DELETE_FILE_FROM_USER);;
            ps.setInt(1, id);
            ps.setInt(2, fileId);
            ps.executeUpdate();

            ps = this.preparedStatements.get(DELETE_FROM_LABELED);;
            ps.setInt(1, fileId);
            ps.executeUpdate();

            if (checkIfProjectEmpty(id, projectId)) {
                ps = this.preparedStatements.get(DROP_EMPTY_PROJECT);
                ps.setInt(1, projectId);
                ps.executeUpdate();
            }
        }
    }

    /**
     * Updates the label name given the file id
     * @param name
     * @param id
     * @throws SQLException
     */
    public void updateLabelName(String name, int id) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(UPDATE_FILE);
        ps.setString(1, name);
        ps.setInt(2, id);
        ps.executeUpdate();
    }

    /**
     * Updates the project name given the porject id
     * @param name
     * @param id
     * @throws SQLException
     */
    public void updateProjectName(String name, int id) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(UPDATE_PROJECT);
        ps.setString(1, name);
        ps.setInt(2, id);
        ps.executeUpdate();
    }

    /**
     * Returns a list with the labeled files
     * @param id
     * @return
     * @throws SQLException
     */
    public List<LabeledFile> getAllLabeledFiles(int id) throws SQLException {
        List<LabeledFile> labeledFiles = new ArrayList<>();
        PreparedStatement ps = this.preparedStatements.get(GET_ALL_LABELED_FILES_USER);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            labeledFiles.add(new LabeledFile(rs.getInt("file_id"), rs.getString("label"), rs.getNString("path")));
        }
        return labeledFiles;
    }

    /**
     * Create a project object with all the files
     * @param id
     * @param name
     * @param fileNames
     * @throws SQLException
     */
    public void insertProject(int id, String name, String[] fileNames) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(GET_PROJECT_ID_USING_NAME);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int projectId = rs.getInt("id");
        int fileId;

        for (String file: fileNames) {
            ps = this.preparedStatements.get(CREATE_LABEL_FILE);
            ps.setString(1, file);
            ps.setString(2, file);
            ps.executeUpdate();

            ps = this.preparedStatements.get(GET_LABEL_FILE_ID);
            ps.setString(1, file);
            rs = ps.executeQuery();
            rs.next();
            fileId = rs.getInt("id");

            ps = this.preparedStatements.get(INSERT_INTO_USER_PROJECT_FILE);
            ps.setInt(1, id);
            ps.setInt(2, projectId);
            ps.setInt(3, fileId);
            ps.executeUpdate();
        }
    }

    /**
     * Creates a new project
     * @param name
     * @throws SQLException
     */
    public void createProject(String name) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(CREATE_PROJECT);
        ps.setString(1, name);
        ps.executeUpdate();
    }

    /**
     * Checks if a project exist
     * @param id
     * @param name
     * @return
     * @throws SQLException
     */
    public boolean checkIfProjectExist(int id, String name) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(CHECK_IF_PROJECT_EXIST);
        ps.setInt(1, id);
        ps.setString(2, name);
        ResultSet rs = ps.executeQuery();
        rs.next();
        try {
            rs.getString(1);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * gets the project name
     * @param id
     * @return
     * @throws SQLException
     */
    public List<String> getProjectNames(int id) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(GET_PROJECT_NAMES_USING_ID);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        List<String> projectNames = new ArrayList<>();
        while (rs.next()) {
            projectNames.add(rs.getString("project_name"));
        }
        return projectNames;
    }

    /**
     * count how many times a file is used
     * @param path
     * @return
     * @throws SQLException
     */
    public Enums.Used getTimesHardFileIsUsed(String path) throws SQLException {
        PreparedStatement ps = this.preparedStatements.get(GET_ALL_LABEL_FILES);

        //set data on the "?" placeholders of the prepared statement
        ps.setString(1, path);

        //execute
        ResultSet rs = ps.executeQuery();

        int count = 0;
        while (rs.next()) {
            count += 1;
        }
        if (count < 1) {
            return Enums.Used.NOT_USED;
        } else {
            return Enums.Used.USED;
        }
    }

    /**
     * close the connection!
     * @throws DatabaseException
     */
    public void disconnect() throws DatabaseException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
