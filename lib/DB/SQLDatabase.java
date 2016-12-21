package lib.DB;

import com.mysql.fabric.jdbc.FabricMySQLDriver;

import java.sql.*;

public class SQLDatabase {
    protected Connection connection;
    protected static Statement statement;
    protected ResultSet resultSet;

  public void sqlSetConnect() {
      try {
          Driver driver = new FabricMySQLDriver();
          DriverManager.registerDriver(driver);
          //connection = DriverManager.getConnection("jdbc:mysql://localhost/bioimageapp", "oleh", "oleh123");
          connection = DriverManager.getConnection("jdbc:mysql://"+ SQLDatabaseParam.getHost() +"/" +
                          SQLDatabaseParam.getDbname()+"?characterEncoding=utf8",
                  SQLDatabaseParam.getDbuser() , SQLDatabaseParam.getDbpass());

          statement = connection.createStatement();
      }
      catch(SQLException e) {System.err.println("SQL error Server is not responding SQL");}
  }

    public void sqlUpdateConnect()
    {
       try
       {
           if(connection.isClosed())
           {
               sqlSetConnect();
           }
       }
       catch(SQLException a)
       {
           a.printStackTrace();
       }
    }
    public void sqlInsertExecute(String query) throws  SQLException
    {
         statement.execute(query);
    }
    public static void CreateEvent(String query) throws  SQLException
    {
        statement.execute(query);
    }
    public void sqlExecute(String query)
    {
        try{
            resultSet = statement.executeQuery(query);
        }
        catch(SQLException f) {f.printStackTrace();}
    }
    public void updateExecute(String query)
    {
        try{
            statement.executeUpdate(query);
        }
        catch(Exception f) {f.printStackTrace();}
    }
    public void removeExecute(String query)
    {
        try{
            statement.executeUpdate(query);
        }
        catch(Exception f) {f.printStackTrace();}
    }
    public ResultSet returnResult()
    {
        return resultSet;
    }
}
