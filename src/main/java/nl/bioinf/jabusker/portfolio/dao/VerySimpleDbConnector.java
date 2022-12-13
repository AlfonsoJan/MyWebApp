package nl.bioinf.jabusker.portfolio.dao;

import nl.bioinf.jabusker.portfolio.db_utils.DbCredentials;
import nl.bioinf.jabusker.portfolio.db_utils.DbUser;
import nl.bioinf.jabusker.portfolio.db_objects.LabeledFile;
import nl.bioinf.jabusker.portfolio.db_objects.Project;
import nl.bioinf.jabusker.portfolio.db_objects.User;

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
    private static final String GET_PROJECT_USING_USER_ID = "get_project_using_user_id";
    private static final String GET_FILE_USING_FILE_ID = "get_file_using_file_id";
    private static final String GET_FILE_USING_PROJECT_ID = "get_file_using_project_id";

    private static final String INSERT_USER = "insert_user";
    private static final String INSERT_PROJECT = "insert_project";
    private static final String INSERT_FILE = "insert_file";

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
            User someUser = connector.getUser(1);

            //print username
            System.out.println(someUser.getName());

            System.out.println(connector.getProjectsFrom(2));

            //a catch-all for database interaction exceptions
        } catch (DatabaseException | IOException | NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public Map<Project, ArrayList<LabeledFile>> getProjectsFrom(int id) throws SQLException {

        String fetchQuery = "select * from label_files,projects,users where projects.user_id = ? and projects.id = label_files.id and users.id = ?";
        PreparedStatement ps = connection.prepareStatement(fetchQuery);
        ps.setString(1, String.valueOf(id));
        ps.setString(2, String.valueOf(id));

        Map<Project, ArrayList<LabeledFile>> results = new ConcurrentHashMap<>();
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
            int labelId = rs.getInt("label_files.id");
            String label = rs.getString("label_files.label");
            String path = rs.getString("label_files.path");
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


    private void prepareStatements() throws SQLException {
        String userNameFetchQuery = "SELECT * FROM users WHERE id = ?";
        this.preparedStatements.put(GET_USER_USING_ID, connection.prepareStatement(userNameFetchQuery));

        String userIdFetchQuery = "SELECT * FROM users WHERE username = ?";
        this.preparedStatements.put(GET_USER_USING_NAME, connection.prepareStatement(userIdFetchQuery));

        String projectIdFetchQuery = "SELECT * FROM projects WHERE id = ?";
        this.preparedStatements.put(GET_PROJECT_USING_PROJECT_ID, connection.prepareStatement(projectIdFetchQuery));

        String projectUserIdFetchQuery = "SELECT * FROM projects WHERE user_id = ?";
        this.preparedStatements.put(GET_PROJECT_USING_USER_ID, connection.prepareStatement(projectUserIdFetchQuery));

        String fileIdFetchQuery = "SELECT * FROM labeled_files WHERE id = ?";
        this.preparedStatements.put(GET_FILE_USING_FILE_ID, connection.prepareStatement(fileIdFetchQuery));

        String fileProjectIdFetchQuery = "SELECT * FROM labeled_files WHERE project_id = ?";
        this.preparedStatements.put(GET_FILE_USING_PROJECT_ID, connection.prepareStatement(fileProjectIdFetchQuery));

        String userInsertQuery = "INSERT INTO users (username) VALUES (?)";
        this.preparedStatements.put(INSERT_USER, connection.prepareStatement(userInsertQuery));

        String projectInsertQuery = "INSERT INTO projects (project_name, user_id) VALUES (?, ?)";
        this.preparedStatements.put(INSERT_PROJECT, connection.prepareStatement(projectInsertQuery));

        String fileInsertQuery = "INSERT INTO labeledfiles (label, path, project_id) VALUES (?, ?)";
        this.preparedStatements.put(INSERT_FILE, connection.prepareStatement(fileInsertQuery));
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

    public void insertUser(String userName) throws DatabaseException {
        try{
            //Prepare statement
            //!! Doing this within this method is extremely inefficient !!
            String insertQuery = "INSERT INTO Users (username) VALUES (?)";
            PreparedStatement ps = connection.prepareStatement(insertQuery);

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
