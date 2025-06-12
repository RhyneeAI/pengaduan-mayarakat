package View;

import Controller.UserController;
import Helper.MessageHelper;
import Lib.ArrayBuilder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.toedter.calendar.JDateChooser;
import java.util.Date;

public class Register extends JFrame {
    public Register() {
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = screenSize.width / 2;
        int height = screenSize.height / 2;

        setTitle("Register");
        setSize(width, height + 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel kiri dengan gambar
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setLayout(new BorderLayout());

        // Ganti path gambar ini sesuai lokasi gambarmu
        ImageIcon icon = new ImageIcon("src/Assets/kandes-cibarengkok.jpg"); 
        // Jika gambarnya terlalu besar, bisa di-resize:
        Image img = icon.getImage().getScaledInstance(width / 2, height + 120, Image.SCALE_AREA_AVERAGING);
        JLabel lblImage = new JLabel(new ImageIcon(img));
        lblImage.setHorizontalAlignment(SwingConstants.CENTER);
        lblImage.setVerticalAlignment(SwingConstants.CENTER);
        leftPanel.add(lblImage, BorderLayout.CENTER);

        // Panel kanan dengan form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setPreferredSize(new Dimension((int)(width * 0.66), height + 100));
        formPanel.setBorder(BorderFactory.createEmptyBorder(22, 15, 22, 15));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
//        gbc.weightx = 1.0; 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNIK = new JLabel("NIK ");
        JTextField txtNIK = new JTextField(15);

        JLabel lblNama = new JLabel("Nama ");
        JTextField txtNama = new JTextField(15);

        JLabel lblTglLahir = new JLabel("Tanggal Lahir ");
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");

        JLabel lblKategori = new JLabel("Kategori Usia ");
        JComboBox<String> cmbKategori = new JComboBox<>(new String[]{"Anak", "Remaja", "Dewasa", "Lanjut Usia"});
        cmbKategori.setEnabled(false);

        JLabel lblJK = new JLabel("Jenis Kelamin ");
        JComboBox<String> cmbJK = new JComboBox<>(new String[]{"Laki-Laki", "Perempuan"});

        JLabel lblTelp = new JLabel("No Telp ");
        JTextField txtTelp = new JTextField(15);

        JLabel lblAlamat = new JLabel("Alamat ");
        JTextArea txtAlamat = new JTextArea(3, 15);
        JScrollPane scrollAlamat = new JScrollPane(txtAlamat);

        JLabel lblUsername = new JLabel("Username ");
        JTextField txtUsername = new JTextField(15);

        JLabel lblPassword = new JLabel("Password ");
        JPasswordField txtPassword = new JPasswordField(15);

        JLabel lblConfirm = new JLabel("Confirm Password ");
        JPasswordField txtConfirm = new JPasswordField(15);

        JButton btnRegister = new JButton("Register");
        JLabel lblLogin = new JLabel("<html><u align='center'>Sudah punya akun? Login!</u></html>");
        lblLogin.setForeground(Color.BLUE);
        lblLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLogin.setPreferredSize(new Dimension(200, 30));
        lblLogin.setHorizontalAlignment(SwingConstants.CENTER);

        // Dynamic Age Category
        dateChooser.getDateEditor().getUiComponent().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Date selectedDate = dateChooser.getDate();
                if (selectedDate != null) {
                    LocalDate birthDate = new java.sql.Date(selectedDate.getTime()).toLocalDate();
                    int age = Period.between(birthDate, LocalDate.now()).getYears();
                    if (age <= 12) cmbKategori.setSelectedItem("Anak");
                    else if (age <= 17) cmbKategori.setSelectedItem("Remaja");
                    else if (age <= 59) cmbKategori.setSelectedItem("Dewasa");
                    else cmbKategori.setSelectedItem("Lanjut Usia");
                }
            }
        });

        int y = 0;
        gbc.insets = new Insets(4, 12, 8, 10);
        
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST; 
        formPanel.add(lblNIK, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(txtNIK, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lblNama, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(txtNama, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lblTglLahir, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(dateChooser, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lblKategori, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(cmbKategori, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lblJK, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(cmbJK, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lblTelp, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(txtTelp, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lblAlamat, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(scrollAlamat, gbc);
        gbc.weighty = 0; 

        gbc.gridx = 0; gbc.gridy = ++y; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lblUsername, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lblPassword, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lblConfirm, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(txtConfirm, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.gridwidth = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(btnRegister, gbc);

        gbc.gridx = 0; gbc.gridy = ++y; gbc.gridwidth = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(lblLogin, gbc);


        // SplitPane for left (image) and right (form)
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, formPanel);
        splitPane.setResizeWeight(0.35);  // kiri 33%, kanan 67%
        splitPane.setDividerLocation((int)(width * 0.33));
        splitPane.setEnabled(false); 

        add(splitPane);
        setVisible(true);
        

        // Register Action
        btnRegister.addActionListener(e -> {
            String password = new String(txtPassword.getPassword());
            String confirm = new String(txtConfirm.getPassword());

            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Password tidak cocok");
                return;
            }

            List<ArrayBuilder> data = new ArrayList<>();
            data.add(new ArrayBuilder("nik", txtNIK.getText()));
            data.add(new ArrayBuilder("name", txtNama.getText()));
            
            Date selectedDate = dateChooser.getDate();
            String birthDateStr = selectedDate == null ? "" : new java.text.SimpleDateFormat("yyyy-MM-dd").format(selectedDate);

            data.add(new ArrayBuilder("birth_date", birthDateStr));
            data.add(new ArrayBuilder("age_category", (String) cmbKategori.getSelectedItem()));
            data.add(new ArrayBuilder("gender", (String) cmbJK.getSelectedItem()));
            data.add(new ArrayBuilder("phone_number", txtTelp.getText()));
            data.add(new ArrayBuilder("address", txtAlamat.getText()));
            data.add(new ArrayBuilder("username", txtUsername.getText()));
            data.add(new ArrayBuilder("password", password));

            UserController uc = new UserController();
            Map<String, Object> result = uc.register(data);
            MessageHelper.showMessageFromResult(result);

            if (Boolean.TRUE.equals(result.get("status"))) {
                this.dispose();
                new Login().setVisible(true);
            }
        });

        lblLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                dispose();
                new Login().setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Register::new);
    }
}
