package View;

import View.Content.PengaduanContent;

import com.formdev.flatlaf.FlatLightLaf;
import Controller.UserController;
import Helper.MessageHelper;
import Lib.Session;
import View.Content.DashboardAdminContent;
import View.Content.DashboardUserContent;
import View.Content.KategoriContent;
import View.Content.PengaduanManagement;
import View.Content.UserManagementContent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

public class Dashboard extends JFrame {
    private JDesktopPane desktopPane;

    public Dashboard() {
        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int width = (int) (screenSize.width * 0.85);
        int height = (int) (screenSize.height * 0.9);

        setSize(width, height);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === Navbar ===
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(new Color(45, 118, 232));
        navbar.setPreferredSize(new Dimension(0, 50));

        JLabel titleLabel = new JLabel("Selamat Datang", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        navbar.add(titleLabel, BorderLayout.CENTER);

        add(navbar, BorderLayout.NORTH);

        // === Sidebar ===
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setBackground(new Color(35, 45, 65));
        sidebar.setPreferredSize(new Dimension(180, 0));

        // Panel atas sidebar
        JPanel topMenuPanel = new JPanel();
        topMenuPanel.setLayout(new GridLayout(6, 1, 0, 5));
        topMenuPanel.setBackground(new Color(35, 45, 65));
        topMenuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JButton btnDashboardAdmin = new JButton("Dashboard");
        JButton btnDashboardUser = new JButton("Beranda");
        JButton btnKategori = new JButton("Kategori");
        JButton btnManajemenUser = new JButton("User");
        JButton btnManajemenPengaduan = new JButton("Pengaduan");
        JButton btnPengaduan = new JButton("Pengaduan");
        styleSidebarButton(btnDashboardAdmin, new Color(52, 73, 94));
        styleSidebarButton(btnDashboardUser, new Color(52, 73, 94));
        styleSidebarButton(btnManajemenUser, new Color(41, 128, 185));
        styleSidebarButton(btnManajemenPengaduan, new Color(41, 128, 185));
        styleSidebarButton(btnKategori, new Color(41, 128, 185));
        styleSidebarButton(btnPengaduan, new Color(41, 128, 185));
        
        JLabel lblManajemen = new JLabel("Manajemen");
        lblManajemen.setForeground(Color.WHITE);
        lblManajemen.setFont(new Font("SansSerif", Font.BOLD, 14));

        if("ADMIN".equals(Session.get("access_level"))) {
            topMenuPanel.add(btnDashboardAdmin);
            topMenuPanel.add(lblManajemen);
            topMenuPanel.add(btnManajemenUser);
            topMenuPanel.add(btnManajemenPengaduan);
            topMenuPanel.add(btnKategori);
        } else {
            topMenuPanel.add(btnDashboardUser);
            topMenuPanel.add(btnPengaduan);
        }

        // Panel bawah sidebar (logout)
        JPanel bottomMenuPanel = new JPanel(new BorderLayout());
        bottomMenuPanel.setBackground(new Color(35, 45, 65));
        bottomMenuPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 20, 10));

        JButton btnLogout = new JButton("Logout");
        styleSidebarButton(btnLogout, new Color(192, 57, 43));
        bottomMenuPanel.add(btnLogout, BorderLayout.SOUTH);

        sidebar.add(topMenuPanel, BorderLayout.NORTH);
        sidebar.add(bottomMenuPanel, BorderLayout.SOUTH);

        add(sidebar, BorderLayout.WEST);

        // === Desktop Pane ===
        desktopPane = new JDesktopPane() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(245, 245, 245));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        desktopPane.setBorder(null);
        add(desktopPane, BorderLayout.CENTER);

        btnDashboardAdmin.addActionListener(e -> {
            showInternal(new DashboardAdminContent(desktopPane));
        });
        btnDashboardUser.addActionListener(e -> {
            showInternal(new DashboardUserContent(desktopPane));
        });
        btnManajemenUser.addActionListener((ActionEvent e) -> {
            showInternal(new UserManagementContent(desktopPane));
        });
        btnManajemenPengaduan.addActionListener((ActionEvent e) -> {
            showInternal(new PengaduanManagement(desktopPane));
        });
        btnKategori.addActionListener((ActionEvent e) -> {
            showInternal(new KategoriContent(desktopPane));
        });
        btnPengaduan.addActionListener(e -> {
            showInternal(new PengaduanContent(desktopPane));
        });
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                Dashboard.this,
                "Apakah Anda yakin ingin logout?",
                "Konfirmasi Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                UserController uc = new UserController();
                Map<String, Object> result = uc.logout();
                MessageHelper.showMessageFromResult(result);

                if (Boolean.TRUE.equals(result.get("status"))) {
                    dispose();
                    new Login().setVisible(true);
                }
            }
        });

        showInternal(new DashboardAdminContent(desktopPane));
    }

    private void styleSidebarButton(JButton button, Color bgColor) {
        button.setFocusPainted(false);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.PLAIN, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setHorizontalAlignment(SwingConstants.LEFT);
    }

    private void showInternal(JInternalFrame frame) {
        desktopPane.removeAll();
        desktopPane.repaint();
        frame.setVisible(true);
        desktopPane.add(frame);
        try {
            frame.setMaximum(true); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
     public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Table.showUIManager.setLookAndFeel(new FlatLightLaf());Grid", true);
//            UIManager.put("Table.gridColor", Color.LIGHT_GRAY); 
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}
