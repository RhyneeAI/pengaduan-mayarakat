package Model;

import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DBConnection {
    public java.sql.Connection connection;

    public DBConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/pengaduan", "root", "");
//            JOptionPane.showMessageDialog(null, "Terhubung ke database");
        }
        catch(SQLException se) {
            JOptionPane.showMessageDialog(null, "Gagal terhubung ke dalam database" + se.getMessage(), "Pesan", 0);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public java.sql.Connection getConnection() {
        return connection;
    }

    public static void main(String[] args) {
        DBConnection conn = new DBConnection(); 
    }
}
