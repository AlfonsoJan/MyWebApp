package nl.bioinf.jabusker.portfolio.dao;

import nl.bioinf.jabusker.portfolio.db_utils.DbCredentials;
import nl.bioinf.jabusker.portfolio.db_utils.DbUser;
import nl.bioinf.jabusker.portfolio.model.User;

import java.io.IOException;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class VerySimpleDbConnector {
    private final String url;
    private final String dbUser;
    private final String dbPassword;
    private Connection connection;

    private final Map<String, PreparedStatement> preparedStatements = new HashMap();

    private static final String GET_USER = "get_user";
    private static final String GET_PROJECT = "get_project";
    private static final String GET_FILE = "get_file";

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
            DbUser dbUser = DbCredentials.getMySQLuser();
            String user = dbUser.getUserName();
            String passWrd = dbUser.getDatabasePassword();
            String url = dbUser.getUrl();

            //connect
            VerySimpleDbConnector connector = new VerySimpleDbConnector(url, user, passWrd);

            //make an user object
            User someUser = connector.getUser(1);

            //print username
            System.out.println(someUser.getName());

            //a catch-all for database interaction exceptions
        } catch (DatabaseException | IOException | NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public VerySimpleDbConnector(String url, String dbUser, String dbPassword) throws DatabaseException {
        this.url = url;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;

        //make the connection
        connect();
    }

    private void connect() throws DatabaseException {
        try {
            //load driver class
            Class.forName("com.mysql.cj.jdbc.Driver");
            //create connection
            connection = DriverManager.getConnection(url, dbUser, dbPassword);
            //..which is risky
        } catch (Exception e) {
            e.printStackTrace();
            throw new DatabaseException("Something is wrong with the database, see cause Exception",
                    e.getCause());
        }
    }

    private void prepareStatements() throws SQLException {
        String userFetchQuery = "SELECT * FROM users WHERE id = ?";
        this.preparedStatements.put(GET_USER, connection.prepareStatement(userFetchQuery));

        String projectFetchQuery = "SELECT * FROM projects WHERE id = ?";
        this.preparedStatements.put(GET_PROJECT, connection.prepareStatement(projectFetchQuery));

        String fileFetchQuery = "SELECT * FROM labeledfiles WHERE id = ?";
        this.preparedStatements.put(GET_FILE, connection.prepareStatement(fileFetchQuery));

        String userInsertQuery = "INSERT INTO users (username) VALUES (?)";
        this.preparedStatements.put(INSERT_USER, connection.prepareStatement(userInsertQuery));

        String projectInsertQuery = "INSERT INTO projects (project_name, user_id) VALUES (?, ?)";
        this.preparedStatements.put(INSERT_PROJECT, connection.prepareStatement(projectInsertQuery));

        String fileInsertQuery = "INSERT INTO labeledfiles (label, path, project_id) VALUES (?, ?)";
        this.preparedStatements.put(INSERT_FILE, connection.prepareStatement(fileInsertQuery));
    }

    public User getUser(int id) throws SQLException {
        // prepare query statement
        PreparedStatement ps = this.preparedStatements.get(GET_USER);

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
