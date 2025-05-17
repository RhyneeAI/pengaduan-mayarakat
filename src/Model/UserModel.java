package Model;

import Helper.MessageHelper;
import java.sql.*;
import javax.swing.JOptionPane;

public class UserModel {
    DBConnection db = new DBConnection();
    private Connection con = db.getConnection();
    private String nik, name, birthDate, ageCategory, gender, phoneNumber, address, username, password;
    private String accessLevel = "USER";
    
    public UserModel() {
        if(con == null) {
            MessageHelper.showError("Tidak terhubung ke database");
        }
    }

    public UserModel(Builder builder) {
        this.nik = builder.nik;
        this.name = builder.name;
        this.birthDate = builder.birthDate;
        this.ageCategory = builder.ageCategory;
        this.gender = builder.gender;
        this.phoneNumber = builder.phoneNumber;
        this.address = builder.address;
        this.username = builder.username;
        this.password = builder.password;
        this.accessLevel = builder.accessLevel != null ? builder.accessLevel : "USER"; 
    }
    
    public boolean login(String username, String password) {
        if (con == null) {
            JOptionPane.showMessageDialog(null, "Koneksi database null.");
            return false;
        }

        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "Login berhasil! Selamat datang, " + username);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Login gagal! Username atau password salah.");
                    return false;
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error saat login: " + e.getMessage() +
                "\nSQLState: " + e.getSQLState() +
                "\nErrorCode: " + e.getErrorCode());
            return false;
        }
    }

    public boolean insertUser() {
        if (con == null) {
            JOptionPane.showMessageDialog(null, "Koneksi database null.");
            return false;
        }

        String query = "INSERT INTO users (nik, name, birth_date, age_category, gender, phone_number, address, username, password, access_level) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, this.nik);
            ps.setString(2, this.name);
            ps.setString(3, this.birthDate);
            ps.setString(4, this.ageCategory);
            ps.setString(5, this.gender);
            ps.setString(6, this.phoneNumber);
            ps.setString(7, this.address);
            ps.setString(8, this.username);
            ps.setString(9, this.password);
            ps.setString(10, this.accessLevel);

            int result = ps.executeUpdate(); 

            if (result > 0) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "Insert gagal: Tidak ada baris yang ditambahkan.");
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Error saat insert data: " + e.getMessage() +
                "\nSQLState: " + e.getSQLState() +
                "\nErrorCode: " + e.getErrorCode());
            return false;
        }
    }

    public void selectAllUsers() {
        if (con == null) {
            JOptionPane.showMessageDialog(null, "Koneksi database tidak tersedia.");
            return;
        }

        String query = "SELECT nik, name, birth_date, age_category, gender, phone_number, address FROM users";

        try {
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String nik = rs.getString("nik");
                String name = rs.getString("name");
                String birthDate = rs.getString("birth_date");
                String ageCategory = rs.getString("age_category");
                String gender = rs.getString("gender");
                String phoneNumber = rs.getString("phone_number");
                String address = rs.getString("address");

                System.out.println("NIK: " + nik + ", Name: " + name + ", Birth Date: " + birthDate +
                                   ", Age Category: " + ageCategory + ", Gender: " + gender +
                                   ", Phone: " + phoneNumber + ", Address: " + address);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal menampilkan data: " + e.getMessage());
        }
    }
    
    public boolean isNikExists(String nik) {
        if (con == null) return false;

        String query = "SELECT nik FROM users WHERE nik = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, nik);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            return false;
        }
    }
    public static class Builder {
        private String nik, name, birthDate, ageCategory, gender, phoneNumber, address, password, username;
        private String accessLevel = "USER";

        public Builder nik(String nik) {
            this.nik = nik;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder ageCategory(String ageCategory) {
            this.ageCategory = ageCategory;
            return this;
        }

        public Builder birthDate(String birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        public Builder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }
        
        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder accessLevel(String accessLevel) {
            this.accessLevel = accessLevel;
            return this;
        }

        public UserModel build() {
            return new UserModel(this);
        }
    }
}
