package nl.bioinf.ngswebapp.dao;

import nl.bioinf.ngswebapp.db_objects.Process;
import nl.bioinf.ngswebapp.db_utils.DbCredentials;
import nl.bioinf.ngswebapp.db_utils.DbUser;
import nl.bioinf.ngswebapp.db_objects.LabeledFile;
import nl.bioinf.ngswebapp.db_objects.Project;
import nl.bioinf.ngswebapp.db_objects.User;
import nl.bioinf.ngswebapp.model.Enums;
import nl.bioinf.ngswebapp.servlets.AllPersonalFilesServlet;
import nl.bioinf.ngswebapp.servlets.AllPersonalProjectsServlet;
import nl.bioinf.ngswebapp.servlets.NewFileTabServlet;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class VerySimpleDbConnector {
    private final String url;
    private final String dbUser;
    private final String dbPassword;
    private Connection connection;

    private final Map<String, PreparedStatement> preparedStatements = new HashMap();

    private static final String GET_USER_USING_ID = "get_user_using_id";
    private static final String GET_USER_USING_NAME = "get_userid";
    private static final String GET_PROJECT_USING_PROJECT_ID = "get_project_using_project_id";
    private static final String GET_PROJECTS_USING_USER_ID = "get_project_using_user_id";
    private static final String GET_PROJECTS_USING_PROJECT_ID = "get_projects_using_project_id";
    private static final String GET_FILE_USING_FILE_ID = "get_file_using_file_id";
    private static final String GET_ALL_LABEL_FILES = "get_all_label_files";
    private static final String INSERT_USER = "insert_user";
    private static final String INSERT_PROJECT = "insert_project";
    private static final String INSERT_FILE = "insert_file";
    private static final String UPDATE_PROJECT = "update_project";
    private static final String DELETE_PROJECT = "delete_project";
    private static final String UPDATE_FILE = "update_file";
    private static final String DELETE_FILE = "delete_file";
    private static final String SELECT_PROJECT_FROM_NAME = "select_project_from_name";

    private static final String SELECT_PROJECTS_FROM_USER = "select_projects_from_user";
    private static final String SELECT_LABEL_FILES_FROM_USER = "select_labeled_from_user";

    private static final String INSERT_PROCESS = "insert_process";
    private static final String INSERT_PROCESS_NO_PROJECT = "insert_process_no_project";
    private static final String MAX_PROCESS = "max_process";
    private static final String SELECT_ALL_PROCESS = "select_process";

    private static final String SELECT_ALL_PROCESS_FROM_PROJECT = "select_process_project";
    private static final String DELETE_PROCESS_QUERY = "delete_process";
    private static final String UPDATE_PROCESS = "update_process";

    /**
     * a main for demonstration purposes
     *
     * @param args
     */
    public static void main(String[] args) {
        try {

            //connect
            VerySimpleDbConnector connector = new VerySimpleDbConnector();

            //make an user object
            System.out.println(connector.getLabelFilesFromProject(1));

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
    public VerySimpleDbConnector() throws DatabaseException, IOException, NoSuchFieldException {
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

        String projectIdFetchQuery = "SELECT * FROM projects WHERE id = ?";
        this.preparedStatements.put(GET_PROJECT_USING_PROJECT_ID, connection.prepareStatement(projectIdFetchQuery));

        String projectUserIdFetchQuery = "select * from labeled_files,projects,users where projects.user_id = ? and projects.id = labeled_files.project_id and users.id = projects.user_id";
        this.preparedStatements.put(GET_PROJECTS_USING_USER_ID, connection.prepareStatement(projectUserIdFetchQuery));

        String selectProjectsFromUserQuery = "select * from projects where projects.user_id = ?";
        this.preparedStatements.put(SELECT_PROJECTS_FROM_USER, connection.prepareStatement(selectProjectsFromUserQuery));

        String FilesFromProjectId = "select * from labeled_files,projects where projects.id = ? and labeled_files.project_id = projects.id";
        this.preparedStatements.put(GET_PROJECTS_USING_PROJECT_ID, connection.prepareStatement(FilesFromProjectId));

        String fileIdFetchQuery = "SELECT * FROM labeled_files WHERE id = ?";
        this.preparedStatements.put(GET_FILE_USING_FILE_ID, connection.prepareStatement(fileIdFetchQuery));

        String filesFetchQuery = "SELECT * FROM labeled_files where path = ?";
        this.preparedStatements.put(GET_ALL_LABEL_FILES, connection.prepareStatement(filesFetchQuery));

        String userInsertQuery = "INSERT INTO users (username) VALUES (?)";
        this.preparedStatements.put(INSERT_USER, connection.prepareStatement(userInsertQuery));

        String projectInsertQuery = "INSERT INTO projects (project_name, user_id) VALUES (?, ?)";
        this.preparedStatements.put(INSERT_PROJECT, connection.prepareStatement(projectInsertQuery));

        String deleteProjectQuery = "DELETE from projects where id = ?";
        this.preparedStatements.put(DELETE_PROJECT, connection.prepareStatement(deleteProjectQuery));

        String fileInsertQuery = "INSERT INTO labeled_files (label, path, project_id) VALUES (?, ?, ?)";
        this.preparedStatements.put(INSERT_FILE, connection.prepareStatement(fileInsertQuery));

        String projectNameUpdateQuery = "UPDATE projects set project_name = ? where id = ?;";
        this.preparedStatements.put(UPDATE_PROJECT, connection.prepareStatement(projectNameUpdateQuery));

        String labelNameUpdateQuery = "UPDATE labeled_files set label = ? where id = ?;";
        this.preparedStatements.put(UPDATE_FILE, connection.prepareStatement(labelNameUpdateQuery));

        String labelNameDeleteQuery = "DELETE from labeled_files where id = ?;";
        this.preparedStatements.put(DELETE_FILE, connection.prepareStatement(labelNameDeleteQuery));

        String projectIDFetchQueryFromName = "SELECT id from projects where user_id = ? and project_name = ?";
        this.preparedStatements.put(SELECT_PROJECT_FROM_NAME, connection.prepareStatement(projectIDFetchQueryFromName));

        String labeledFetchQueryFromUser = "SELECT * from labeled_files,projects where labeled_files.project_id = projects.id and user_id = ?";
        this.preparedStatements.put(SELECT_LABEL_FILES_FROM_USER, connection.prepareStatement(labeledFetchQueryFromUser));

        String processInsertQuery = "INSERT INTO process (type, user_id, project_id, unique_id) VALUES (?, ?, ?, ?)";
        this.preparedStatements.put(INSERT_PROCESS, connection.prepareStatement(processInsertQuery));

        String processInsertWithoutProjectQuery = "INSERT INTO process (type) VALUES (?)";
        this.preparedStatements.put(INSERT_PROCESS_NO_PROJECT, connection.prepareStatement(processInsertWithoutProjectQuery));

        String maxProcessIdQuery = "SELECT max(id) from process";
        this.preparedStatements.put(MAX_PROCESS, connection.prepareStatement(maxProcessIdQuery));

        String getAllProcessFromUser = "select * from process where user_id = ? and process.type = ?;";
        this.preparedStatements.put(SELECT_ALL_PROCESS, connection.prepareStatement(getAllProcessFromUser));

        String getAllProcessFromProject = "select * from process where project_id = ?";
        this.preparedStatements.put(SELECT_ALL_PROCESS_FROM_PROJECT, connection.prepareStatement(getAllProcessFromProject));

        String deleteProcessQuery = "DELETE from process where id = ?";
        this.preparedStatements.put(DELETE_PROCESS_QUERY, connection.prepareStatement(deleteProcessQuery));

        String updateProcessRemoveProject = "UPDATE process set project_id = null where id = ?;";
        this.preparedStatements.put(UPDATE_PROCESS, connection.prepareStatement(updateProcessRemoveProject));
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
     * Gets a project object by giving an id
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Project getProject(int id) throws SQLException {
        // prepare query statement
        PreparedStatement ps = this.preparedStatements.get(GET_PROJECT_USING_PROJECT_ID);

        //set data on the "?" placeholders of the prepared statement
        ps.setString(1, String.valueOf(id));

        //execute
        ResultSet rs = ps.executeQuery();

        //next result
        rs.next();

        //if there is data, process it
        String projectName = rs.getString("project_name");
        int userId = rs.getInt("user_id");
        return new Project(id, projectName, userId);
    }

    /**
     * Gets a project from a specific user by giving a project name and userid.
     *
     * @param name
     * @param userID
     * @return
     * @throws SQLException
     */
    public Project getProject(String name, int userID) throws SQLException {
        // prepare query statement
        PreparedStatement ps = this.preparedStatements.get(SELECT_PROJECT_FROM_NAME);

        //set data on the "?" placeholders of the prepared statement
        ps.setString(1, String.valueOf(userID));
        ps.setString(2, name);

        //execute
        ResultSet rs = ps.executeQuery();

        //next result
        rs.next();

        //if there is data, process it
        int id = rs.getInt("id");
        return new Project(id, name, userID);
    }

    /**
     * Inserts a project into the database
     *
     * @param projectName
     * @param userId
     * @throws DatabaseException
     */
    public void insertProject(String projectName, int userId) throws DatabaseException {
        try{
            //Prepare statement
            //!! Doing this within this method is extremely inefficient !!
            PreparedStatement ps = this.preparedStatements.get(INSERT_PROJECT);

            //set data on the "?" placeholders of the prepared statement
            ps.setString(1, projectName);
            ps.setInt(2, userId);

            //do the actual insert
            ps.executeUpdate();;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DatabaseException("Something is wrong with the database, see cause Exception",
                    ex.getCause());
        }
    }

    /**
     * Updates a project by giving it a new name.
     *
     * @param newName
     * @param projectId
     * @throws DatabaseException
     */
    public void updateProject(String newName, int projectId) throws DatabaseException {
        try{
            //Prepare statement
            //!! Doing this within this method is extremely inefficient !!
            PreparedStatement ps = this.preparedStatements.get(UPDATE_PROJECT);

            //set data on the "?" placeholders of the prepared statement
            ps.setString(1, newName);
            ps.setInt(2, projectId);

            //do the actual insert
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DatabaseException("Something is wrong with the database, see cause Exception",
                    ex.getCause());
        }
    }

    /**
     * Deletes a project from the database.
     *
     * @param projectId
     * @throws DatabaseException
     */
    public void deleteProject(int projectId) throws DatabaseException {
        try{
            //Prepare statement
            PreparedStatement ps = this.preparedStatements.get(DELETE_PROJECT);

            //set data on the "?" placeholders of the prepared statement
            ps.setInt(1, projectId);

            //do the actual insert
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DatabaseException("Something is wrong with the database, see cause Exception",
                    ex.getCause());
        }
    }

    /**
     * Gets a labeled file by giving an id.
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public LabeledFile getLabeledFile(int id) throws SQLException {
        // prepare query statement
        PreparedStatement ps = this.preparedStatements.get(GET_FILE_USING_FILE_ID);

        //set data on the "?" placeholders of the prepared statement
        ps.setString(1, String.valueOf(id));

        //execute
        ResultSet rs = ps.executeQuery();

        //next result
        rs.next();

        //if there is data, process it
        String label = rs.getString("label");
        String path = rs.getString("path");
        int userId = rs.getInt("project_id");
        return new LabeledFile(id, label, path, userId);
    }

    /**
     * Inserts a labeled file into the database
     *
     * @param label
     * @param path
     * @param projectId
     * @throws DatabaseException
     */
    public void insertLabeledFile(String label, String path, int projectId) throws DatabaseException {
        try{
            //Prepare statement
            //!! Doing this within this method is extremely inefficient !!
            PreparedStatement ps = this.preparedStatements.get(INSERT_FILE);

            //set data on the "?" placeholders of the prepared statement
            ps.setString(1, label);
            ps.setString(2, path);
            ps.setInt(3, projectId);

            //do the actual insert
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DatabaseException("Something is wrong with the database, see cause Exception",
                    ex.getCause());
        }
    }

    /**
     * Updates a labeled file by giving it a new label.
     *
     * @param newLabel
     * @param labelId
     * @throws DatabaseException
     */
    public void updateLabelName(String newLabel, int labelId) throws DatabaseException {
        try{
            //Prepare statement
            PreparedStatement ps = this.preparedStatements.get(UPDATE_FILE);

            //set data on the "?" placeholders of the prepared statement
            ps.setString(1, newLabel);
            ps.setInt(2, labelId);

            //do the actual insert
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DatabaseException("Something is wrong with the database, see cause Exception",
                    ex.getCause());
        }
    }

    /**
     * Deletes a specific labeled file from the database
     *
     * @param labelId
     * @throws DatabaseException
     */
    public void deleteLabeledFile(int labelId) throws DatabaseException {
        try{
            //Prepare statement
            PreparedStatement ps = this.preparedStatements.get(DELETE_FILE);

            //set data on the "?" placeholders of the prepared statement
            ps.setInt(1, labelId);

            //do the actual insert
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DatabaseException("Something is wrong with the database, see cause Exception",
                    ex.getCause());
        }
    }

    /**
     * Gets a hashmap of all the projects from a specific user. It consists of the project and all the labeled file
     * that are in the project. Example: {SomeProject: [labeledFile1, labeledFile2, labeledFile3]}
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Map<Project, ArrayList<LabeledFile>> getProjectsFromUser(int id) throws SQLException {

        PreparedStatement preparedStatement = this.preparedStatements.get(SELECT_PROJECTS_FROM_USER);
        preparedStatement.setString(1, String.valueOf(id));

        Map<Project, ArrayList<LabeledFile>> results = new ConcurrentHashMap<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int project = resultSet.getInt("projects.id");
            String name = resultSet.getString("projects.project_name");
            results.put(new Project(project, name, id), new ArrayList<>());
        }


        PreparedStatement ps = this.preparedStatements.get(GET_PROJECTS_USING_USER_ID);
        ps.setString(1, String.valueOf(id));

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Set<Project> keys = results.keySet();

            boolean isUsed = false; // To check if a project exists already!
            for (Project key : keys) {
                if (rs.getInt("projects.id") == key.getProjectId()) {
                    isUsed = true;
                    break;
                }
            }

            if (!isUsed) {
                int project = rs.getInt("projects.id");
                String name = rs.getString("projects.project_name");
                int user = rs.getInt("users.id");
                results.put(new Project(project, name, user), new ArrayList<>());
            }

            // Add labeled file to project!
            int labelId = rs.getInt("labeled_files.id");
            String label = rs.getString("labeled_files.label");
            String path = rs.getString("labeled_files.path");
            int projectId = rs.getInt("projects.id");
            LabeledFile labeledFile = new LabeledFile(labelId, label, path, projectId);

            for (Project key : results.keySet()) {
                if (rs.getInt("projects.id") == key.getProjectId()) {
                    results.get(key).add(labeledFile);
                }
            }

        }

        return results;
    }

    /**
     * Gets all the labeled files from a specific project. As an arraylist
     *
     * @param projectId
     * @return
     * @throws SQLException
     */
    public ArrayList<LabeledFile> getLabelFilesFromProject(int projectId) throws SQLException {

        PreparedStatement ps = this.preparedStatements.get(GET_PROJECTS_USING_PROJECT_ID);
        ps.setString(1, String.valueOf(projectId));

        ArrayList<LabeledFile> results = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int labelId = rs.getInt("labeled_files.id");
            String label = rs.getString("labeled_files.label");
            String path = rs.getString("labeled_files.path");
            int id = rs.getInt("projects.id");
            results.add(new LabeledFile(labelId, label, path, id));
        }

        return results;
    }

    /**
     * Gets all the labeled files from a specific user.
     *
     * @param userID
     * @return
     * @throws SQLException
     */
    public ArrayList<LabeledFile> getLabeledFromUser(int userID) throws SQLException {

        PreparedStatement ps = this.preparedStatements.get(SELECT_LABEL_FILES_FROM_USER);
        ps.setString(1, String.valueOf(userID));

        ArrayList<LabeledFile> results = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int labelId = rs.getInt("labeled_files.id");
            String label = rs.getString("labeled_files.label");
            String path = rs.getString("labeled_files.path");
            int id = rs.getInt("projects.id");
            results.add(new LabeledFile(labelId, label, path, id));
        }

        return results;
    }

    /**
     * Gets the number of times a hard file (File path) is used.
     * By used it means it will see how much database labeled files have that specific path as their path.
     *
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
     * Inserts a process into the database
     *
     * @param type
     * @param userId
     * @param projectId
     * @param randomID
     * @throws DatabaseException
     */
    public void insertProcess(String type, int userId, Integer projectId, String randomID) throws DatabaseException {
        try{
            PreparedStatement ps = this.preparedStatements.get(INSERT_PROCESS);
            ps.setString(1, type);
            ps.setInt(2, userId);
            ps.setInt(3, projectId);
            ps.setString(4, randomID);
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DatabaseException("Something is wrong with the database, see cause Exception",
                    ex.getCause());
        }
    }

    /**
     * Gets all processes from a specific user. As an arrayList.
     *
     * @param userID
     * @param type
     * @return
     * @throws SQLException
     */
    public ArrayList<Process> getProcessFromUser(int userID, String type) throws SQLException {

        PreparedStatement ps = this.preparedStatements.get(SELECT_ALL_PROCESS);
        ps.setString(1, String.valueOf(userID));
        ps.setString(2, type);

        ArrayList<Process> results = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Process process;
            try {
                Project project = getProject(rs.getInt("project_id"));
                ArrayList<LabeledFile> files = getLabelFilesFromProject(project.getProjectId());
                project.setLabeledFiles(files);
                process = new Process(rs.getInt("process.id"), type, 1, project.getProjectId(), rs.getString("unique_id"));
                process.setProject(project);
            } catch (SQLException e) {
                process = new Process(rs.getInt("process.id"), type, 1, null, rs.getString("unique_id"));
                process.setProject(new Project(0, "NonExistingProject", 1));
            }


            results.add(process);
        }

        return results;
    }

    /**
     * Gets all processes from a specific project. This could be used to see if a project already has processes active.
     *
     * @param projectID
     * @return
     * @throws SQLException
     */
    public ArrayList<Process> getAllProcessFromProject(int projectID) throws SQLException {

        PreparedStatement ps = this.preparedStatements.get(SELECT_ALL_PROCESS_FROM_PROJECT);
        ps.setString(1, String.valueOf(projectID));

        ArrayList<Process> results = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("id");
            String type = rs.getString("type");
            String uniqueUUID = rs.getString("unique_id");
            int userId = rs.getInt("user_id");
            Process process = new Process(id, type, userId, projectID, uniqueUUID);
            results.add(process);
        }

        return results;
    }

    /**
     * Deletes a process from the database.
     *
     * @param processId
     * @throws DatabaseException
     */
    public void deleteProcess(int processId) throws DatabaseException {
        try{
            //Prepare statement
            PreparedStatement ps = this.preparedStatements.get(DELETE_PROCESS_QUERY);

            //set data on the "?" placeholders of the prepared statement
            ps.setInt(1, processId);

            //do the actual insert
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DatabaseException("Something is wrong with the database, see cause Exception",
                    ex.getCause());
        }
    }

    /**
     * When a processes project gets deleted this function needs to be used. It will set the foreign key to null.
     *
     * @param processId
     * @throws DatabaseException
     */
    public void updateProcess(int processId) throws DatabaseException {
        try{
            //Prepare statement
            //!! Doing this within this method is extremely inefficient !!
            PreparedStatement ps = this.preparedStatements.get(UPDATE_PROCESS);

            //set data on the "?" placeholders of the prepared statement
            ps.setInt(1, processId);

            //do the actual insert
            ps.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new DatabaseException("Something is wrong with the database, see cause Exception",
                    ex.getCause());
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
