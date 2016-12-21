package lib.DB.storeData;

import lib.DB.SQLDatabase;;

/**
 * Created by oleh on 19.12.2016.
 */
public class storeData extends SQLDatabase {

    public int id; // ідентифікатор запису

    public storeData(){sqlSetConnect();}

    public int insertInputValues(float histogram, double red, double green, double blue){

        try {
            sqlInsertExecute("INSERT INTO processinginputvalues (histogram, red, green, blue)" +
                    "VALUES ("+"'"+histogram+"'," +"'"+red+"'," + "'"+green+"',"+"'"+blue+"' )");

            sqlExecute("SELECT id FROM processinginputvalues ORDER BY id DESC LIMIT 1");
            if(resultSet.next()) {
                id = Integer.valueOf(resultSet.getString("id"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    public void insertOutputValues(int lastID, String img_name, double lowThresh, double distance, double frag, int human_evaluate){
        try {
            sqlInsertExecute("INSERT INTO processingoutputvalues (input_id, img_name, distance, frag, lowthreshlevel, human_evaluate)" +
                    "VALUES ("+"'"+lastID+"'," +"'"+img_name+"'," + "'"+distance+"',"+"'"+frag+"',"+"'"+lowThresh+"',"+"'"+human_evaluate+"' )");
        }catch (Exception ex){
            System.err.println(ex);
        }
    }




}
