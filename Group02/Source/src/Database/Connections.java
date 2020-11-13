package Database;


import java.sql.Connection;
import java.sql.DriverManager;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Nhan Nguyen
 */
public class Connections {
     //static String userName = "sa";
     //static String passWord = "sa";
     //static String url = "jdbc:sqlserver://localhost:1433;databaseName=ca";
     static String dbUrl="jdbc:odbc:Driver={SQL Server};Server=IT\\SQLEXPRESS;Database=Caro;UserName=sa;Password=bug!@123";
     
     public static Connection Newconnect(){
        Connection con = null;
        try
        {
            String url="sun.jdbc.odbc.JdbcOdbcDriver";
            Class.forName(url);
            con=DriverManager.getConnection(dbUrl);
        }
        catch (Exception ex)
        {
            System.out.println( "Conection fail!");
        }
         return con;
    }
}
