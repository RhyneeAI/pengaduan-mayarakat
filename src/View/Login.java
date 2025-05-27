package View;

import Controller.UserController;
import Helper.MessageHelper;
import Lib.ArrayBuilder;
import Lib.Session;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

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
        imageLabel.setPreferredSize(new Dimension(width / 2, height));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        
        ImageIcon icon = new ImageIcon("src/Assets/sugih-mukti.png");
        imageLabel.setBorder(BorderFactory.createEmptyBorder(30, 5, 0, 0));
        Image scaledImage = icon.getImage().getScaledInstance(width / 3 - 30, height - 160, Image.SCALE_AREA_AVERAGING);
        imageLabel.setIcon(new ImageIcon(scaledImage));
        mainPanel.add(imageLabel, BorderLayout.WEST);

        // Panel kanan untuk form login
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Komponen form
        JLabel lblUsername = new JLabel("Username :");
        JTextField txtUsername = new JTextField(15);

        JLabel lblPassword = new JLabel("Password :");
        JPasswordField txtPassword = new JPasswordField(15);

        JLabel breakComp = new JLabel(" ");
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
        formPanel.add(breakComp, gbc);
        gbc.gridy++; 
        formPanel.add(btnLogin, gbc);
        gbc.gridy++; 
        formPanel.add(btnLogin, gbc);
        gbc.gridy++;
        formPanel.add(lblRegister, gbc);

        // Event Login
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            
            List<ArrayBuilder> userdata = new ArrayList<>();
            userdata.add(new ArrayBuilder("username", username));
            userdata.add(new ArrayBuilder("password", password));
            
            UserController userController = new UserController();
            Map<String, Object> result = userController.login(userdata);
            MessageHelper.showMessageFromResult(result);

            if (Boolean.TRUE.equals(result.get("status"))) {
                System.out.println(Session.get("name"));
                this.dispose(); 
                new LoginForm().setVisible(true); 
            }
        });

        // Event klik "Register"
        lblRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new Register().setVisible(true);
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
