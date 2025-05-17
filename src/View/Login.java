package View;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {
    public Login() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Ukuran sepertiga layar
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width / 2;
        int height = screenSize.height / 2;

        setTitle("Login");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Tengah layar

        // Panel utama (menggunakan BorderLayout)
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel kiri untuk gambar
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(width / 3, height));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setIcon(new ImageIcon("assets/login_photo.png")); // Ganti path sesuai gambar Anda
        mainPanel.add(imageLabel, BorderLayout.WEST);

        // Panel kanan untuk form login
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Komponen form
        JLabel lblUsername = new JLabel("Username:");
        JTextField txtUsername = new JTextField(15);

        JLabel lblPassword = new JLabel("Password:");
        JPasswordField txtPassword = new JPasswordField(15);

        JButton btnLogin = new JButton("Login");

        JLabel lblRegister = new JLabel("<html><u>Belum mempunyai akun? Register!</u></html>");
        lblRegister.setForeground(Color.BLUE);
        lblRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Menambahkan komponen ke form
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(lblUsername, gbc);
        gbc.gridy++;
        formPanel.add(txtUsername, gbc);
        gbc.gridy++;
        formPanel.add(lblPassword, gbc);
        gbc.gridy++;
        formPanel.add(txtPassword, gbc);
        gbc.gridy++;
        formPanel.add(btnLogin, gbc);
        gbc.gridy++;
        formPanel.add(lblRegister, gbc);

        // Event Login
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());

            if (username.equals("admin") && password.equals("password123")) {
                JOptionPane.showMessageDialog(this, "Login Berhasil!");
            } else {
                JOptionPane.showMessageDialog(this, "Username atau Password salah!");
            }
        });

        // Event klik "Register"
        lblRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JOptionPane.showMessageDialog(null, "Arahkan ke halaman registrasi.");
            }
        });

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}
