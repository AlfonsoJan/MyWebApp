package nl.bioinf.ngswebapp.db_utils;

public class DbUser{
    String url;
    String userName;
    String databasePassword;

    @Override
    public String toString() {
        return "User [url=" + url + ", userName=" + userName + "]";
    }


    /**
     * @param url the host to set
     */
    public void setUrl(String url) {
        this.url = url;
    }


    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }


    /**
     * @param databasePassword the databasePassword to set
     */
    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }


    /**
     * @return the host
     */
    public String getUrl() {
        return url;
    }
    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }
    /**
     * @return the databasePassword
     */
    public String getDatabasePassword() {
        return databasePassword;
    }


}
