package lib.DB;

/**
 * Created by oleh on 01.05.2016.
 */
public class SQLDatabaseParam {

    public static String host;
    public static String port;
    public static String dbuser;
    public static String dbpass;
    public static String dbname;

    public static void setHost(String host1){
        host = host1;
    }
    public static String getHost(){
        return host;
    }

    public static void setPort(String port1){
        port = port1;
    }

    public static String getPort(){
        return port;
    }

    public static void setDbuser(String dbuser1){
        dbuser = dbuser1;
    }

    public static String getDbuser(){
        return dbuser;
    }

    public static void setDbpass(String dbpass1){
        dbpass = dbpass1;
    }

    public static String getDbpass(){
        return dbpass;
    }

    public static void setDbname(String dbname1){
        dbname = dbname1;
    }

    public static String getDbname(){
        return dbname;
    }

}
