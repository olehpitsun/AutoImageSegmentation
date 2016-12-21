package tools;

import lib.DB.SQLDatabase;

/**
 * Created by oleh on 19.12.2016.
 */
public class DBConnect extends SQLDatabase {
    public boolean checkDbConnection(){
        sqlSetConnect();
        if(connection !=null){
            return true;
        }else{
            return false;
        }
    }
}
