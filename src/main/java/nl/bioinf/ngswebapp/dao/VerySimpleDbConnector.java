package nl.bioinf.ngswebapp.dao;

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

    public static VerySimpleDbConnector getConnector(String connector) {
        switch(connector) {
            case "all-files":
                return AllPersonalFilesServlet.getConnector();
            case "all-projects":
                return AllPersonalProjectsServlet.getConnector();
        }
        return NewFileTabServlet.getConnector();
    }

    public VerySimpleDbConnector() throws DatabaseException, IOException, NoSuchFieldException {
        DbUser credentialsFinder = DbCredentials.getMySQLuser();

        this.url = credentialsFinder.getUrl();;
        this.dbUser = credentialsFinder.getUserName();
        this.dbPassword = credentialsFinder.getDatabasePassword();

        //make the connection
        connect();
    }

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

        String processInsertQuery = "INSERT INTO process (type, project_id) VALUES (?, ?)";
        this.preparedStatements.put(INSERT_PROCESS, connection.prepareStatement(processInsertQuery));
    }

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
            return Enums.Used.NONE;
        } else if (count < 2) {
            return Enums.Used.LOW;
        } else if (count < 5) {
            return Enums.Used.MEDIUM;
        } else {
            return Enums.Used.HIGH;
        }
    }

    public Process insertProcess(String type, int projectId) throws DatabaseException {
        try{
            //Prepare statement
            //!! Doing this within this method is extremely inefficient !!
            PreparedStatement ps = this.preparedStatements.get(INSERT_PROCESS);

            //set data on the "?" placeholders of the prepared statement
            ps.setString(type, projectId);

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


//    public User getUser(String userName, String userPass) throws DatabaseException  {
//        try {
//            //Prepare the SQL statement. The question marks are placeholders for repeated use with different data
//            //!! Doing this within this method is extremely inefficient !!
//            String fetchQuery = "SELECT * FROM Users WHERE user_name = ? AND user_password = ?";
//            PreparedStatement ps = connection.prepareStatement(fetchQuery);
//
//            //set data on the "?" placeholders of the prepared statement
//            ps.setString(1, userName);
//            ps.setString(2, userPass);
//
//            //execute
//            ResultSet rs = ps.executeQuery();
//
//            //if there is data, process it
//            while (rs.next()) {
//                String userMail = rs.getString("user_email");
//                String userIdStr = rs.getString("user_id");
//                String userRoleStr = rs.getString("user_role");
//                Role role = Role.valueOf(userRoleStr);
//                User user = new User(userName, userMail, userPass, role);
//                return user;
//            }
//
//            //close resources
//            rs.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw new DatabaseException("Something is wrong with the database, see cause Exception",
//                    e.getCause());
//        }
//        return null;
//    }
//
//    public void insertUser(String userName, String userPass, String email, Role role) throws DatabaseException  {
//        try{
//            //Prepare statement
//            //!! Doing this within this method is extremely inefficient !!
//            String insertQuery = "INSERT INTO Users (user_name, user_password, user_email, user_role) "
//                    + " VALUES (?, ?, ?, ?)";
//            PreparedStatement ps = connection.prepareStatement(insertQuery);
//
//            //set data on the "?" placeholders of the prepared statement
//            ps.setString(1, userName);
//            ps.setString(2, userPass);
//            ps.setString(3, email);
////            ps.setString(4, role.toString());
//
//            //do the actual insert
//            ps.executeUpdate();
//
//            //close resources
//            ps.close();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            throw new DatabaseException("Something is wrong with the database, see cause Exception",
//                    ex.getCause());
//        }
//    }

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
