/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Nguyen Cao Ky
 */
public class DbConnect {
    /*
     * To change this template, choose Tools | Templates
     * and open the template in the editor.
     */

    public boolean checkLogin(String username, String password) {
        try {
           

             Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//Data Source=CAO_KY\SQLEXPRESS;Initial Catalog=MonNgonVenDuong;Integrated Security=True
            String dbUrl = "jdbc:sqlserver://localhost:1433;DatabaseName=caro;instance=MSSQLSERVER2019;user=sa;password=123456";

            Connection con = DriverManager.getConnection(dbUrl);
            String sql = "Select * From Users Where username = ? and password = ?";
            PreparedStatement stm = con.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);
            ResultSet rs = stm.executeQuery();
            boolean result = rs.next();
            rs.close();
            stm.close();
            con.close();
            if (result) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
