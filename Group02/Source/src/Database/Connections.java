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
     static String dbUrl="jdbc:sqlserver://localhost:1433;DatabaseName=caro;instance=MSSQLSERVER2019;user=sa;password=123456";
     
     public static Connection Newconnect(){
        Connection con = null;
        try
        {
            
             Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con=DriverManager.getConnection(dbUrl);
        }
        catch (Exception ex)
        {
            System.out.println( "Conection fail!");
        }
         return con;
    }
}
