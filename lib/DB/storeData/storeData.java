package lib.DB.storeData;

import lib.DB.SQLDatabase;;import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by oleh on 19.12.2016.
 */
public class storeData extends SQLDatabase {

    public int id; // ідентифікатор запису

    public storeData(){sqlSetConnect();}

    public int insertInputValues(double histogram, double red, double green, double blue){

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

    public List<Integer> selectHistogramIDs(int histogramV) throws SQLException {

        sqlExecute("SELECT * FROM processinginputvalues WHERE histogram = "+histogramV+" ");
        List<Integer> IDs = new LinkedList<>();

        while(resultSet.next()) {
            IDs.add(resultSet.getInt("id"));
        }

        return IDs;
    }

    public int selectBlueValue(int id, int blue) throws SQLException {

        sqlExecute("SELECT * FROM processinginputvalues WHERE id = "+id+" AND blue = "+blue+" ");
        int newid = 0;
        if(resultSet.next()){
            newid = resultSet.getInt("id");
        }
        return newid;
    }


    public int getLowThresh(int priorityID) throws SQLException{
        sqlExecute("SELECT * FROM processingoutputvalues WHERE input_id = "+priorityID+" AND human_evaluate > 0");
        int lowlevel = 0;
        if(resultSet.next()){
            lowlevel = resultSet.getInt("lowthreshlevel");
        }
        return lowlevel;
    }


}
