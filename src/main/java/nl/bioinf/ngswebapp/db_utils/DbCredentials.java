package nl.bioinf.ngswebapp.db_utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class DbCredentials {
    public static String getMySQLdbPassword() throws PasswordRetrievalException, FileNotFoundException, IOException {

        DbUser u;
        try {
            u = getMySQLuser();
        } catch (NoSuchFieldException e) {
            throw new PasswordRetrievalException(e.getMessage());
        }
        return u.databasePassword;

    }

    /**
     *
     * @return @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchFieldException
     */
    public static DbUser getMySQLuser() throws FileNotFoundException, IOException, NoSuchFieldException {
        String passPropsFileName = System.getProperty("user.home") + File.separator + ".my.cnf";
        Properties properties = new Properties();
        properties.load(new FileInputStream(passPropsFileName));

        DbUser u = new DbUser();
        if (!properties.containsKey("user")) {
            throw new NoSuchFieldException("field \"user\" is not found in the configuration");
        }
        u.userName = properties.getProperty("user");

        if (!properties.containsKey("password")) {
            throw new NoSuchFieldException("field \"password\" is not found in the configuration");
        }
        u.databasePassword = properties.getProperty("password");

        if (!properties.containsKey("url")) {
            throw new NoSuchFieldException("field \"url\" is not found in the configuration");
        }
        u.url = properties.getProperty("url");

        return u;
    }

}
