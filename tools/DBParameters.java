package tools;

import lib.DB.SQLDatabaseParam;

/**
 * Created by oleh on 21.12.2016.
 */
public class DBParameters {

    /**
     * SET PARAMS TO
     * CONNECT TO DB
     */
    public static void setConnectionValues(){
        SQLDatabaseParam.setHost("localhost");
        SQLDatabaseParam.setPort("3306");
        SQLDatabaseParam.setDbuser("oleh");
        SQLDatabaseParam.setDbpass("oleh123");
        SQLDatabaseParam.setDbname("bioimageapp");

        DBConnect dbConnect = new DBConnect();
        dbConnect.checkDbConnection();
    }
}
