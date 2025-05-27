package View;

import com.formdev.flatlaf.FlatLightLaf;
import Controller.UserController;
import Helper.MessageHelper;
import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Dashboard extends JFrame {
    private JDesktopPane desktopPane;

    public Dashboard() {
        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // === Navbar ===
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(new Color(45, 118, 232));
        navbar.setPreferredSize(new Dimension(0, 50));

        JLabel titleLabel = new JLabel("Selamat Datang", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        navbar.add(titleLabel, BorderLayout.CENTER);

        add(navbar, BorderLayout.NORTH);

        // === Sidebar ===
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BorderLayout());
        sidebar.setBackground(new Color(35, 45, 65));
        sidebar.setPreferredSize(new Dimension(180, 0));

        // Panel atas sidebar
        JPanel topMenuPanel = new JPanel();
        topMenuPanel.setLayout(new GridLayout(2, 1, 0, 5));
        topMenuPanel.setBackground(new Color(35, 45, 65));
        topMenuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        JButton btnDashboard = new JButton("Dashboard");
        JButton btnPengaduan = new JButton("Pengaduan");
        styleSidebarButton(btnDashboard, new Color(52, 73, 94));
        styleSidebarButton(btnPengaduan, new Color(41, 128, 185));

        topMenuPanel.add(btnDashboard);
        topMenuPanel.add(btnPengaduan);

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
        add(desktopPane, BorderLayout.CENTER);

        btnDashboard.addActionListener(e -> showInternal(new DashboardContent()));
        btnPengaduan.addActionListener(e -> showInternal(new PengaduanContent()));
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

        showInternal(new DashboardContent());
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
            frame.setMaximum(true); // agar fullscreen dalam desktopPane
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // === Dummy Internal Frames for Content ===
    class DashboardContent extends JInternalFrame {
        public DashboardContent() {
            super("Dashboard", false, false, false, false); // not resizable, not closable, not maximizable, not iconifiable
            setBounds(0, 0, desktopPane.getWidth(), desktopPane.getHeight());
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(new JLabel("Konten Dashboard di sini", SwingConstants.CENTER), BorderLayout.CENTER);
        }
    }

    class PengaduanContent extends JInternalFrame {
        public PengaduanContent() {
            super("Pengaduan", false, false, false, false);
            setBounds(0, 0, desktopPane.getWidth(), desktopPane.getHeight());
            getContentPane().setLayout(new BorderLayout());
            getContentPane().add(new JLabel("Form atau tabel pengaduan di sini", SwingConstants.CENTER), BorderLayout.CENTER);
        }
    }

    // Dummy login for logout redirection
    class LoginForm extends JFrame {
        public LoginForm() {
            setTitle("Login");
            setSize(300, 150);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            add(new JLabel("Login Form Placeholder", SwingConstants.CENTER));
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}
