package View.Content;

import Controller.DashboardController;
import Lib.ArrayBuilder;
import View.Dashboard;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author luhung
 */
public class DashboardAdminContent extends JInternalFrame {
    public JDesktopPane desktopPane;
    public DashboardController dc = new DashboardController();

    public DashboardAdminContent(JDesktopPane desktopPane) {
        super("", false, false, false, false);
        this.desktopPane = desktopPane;
        setBorder(null);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Panel utama
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Panel Card Statistik
        JPanel cardPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        cardPanel.setOpaque(false);
        
        // Data pengaduan
        String pengaduanMasuk = dc.pengaduanByStatus("New", "");
        String pengaduanDiproses = dc.pengaduanByStatus("Process", "Accepted");
        String pengaduanDitolak = dc.pengaduanByStatus("Rejected", "");
        String pengaduanSelesai = dc.pengaduanByStatus("Finished", "");

        // Perbaiki pemanggilan icon (gunakan PNG/JPG, dan resource path)
        cardPanel.add(createStatCard("Pengaduan Masuk", pengaduanMasuk, new Color(0x2196F3),
            new ImageIcon(getClass().getResource("/Assets/Icon/text-box-plus-outline.png"))));
        cardPanel.add(createStatCard("Diproses", pengaduanDiproses, new Color(0xFFC107),
            new ImageIcon(getClass().getResource("/Assets/Icon/file-sign.png"))));
        cardPanel.add(createStatCard("Ditolak", pengaduanDitolak, new Color(0xF44336),
            new ImageIcon(getClass().getResource("/Assets/Icon/text-box-remove-outline.png"))));
        cardPanel.add(createStatCard("Selesai", pengaduanSelesai, new Color(0x4CAF50),
            new ImageIcon(getClass().getResource("/Assets/Icon/text-box-check-outline.png"))));

        // Panel Pie Chart
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setOpaque(false);
        chartPanel.setBorder(new EmptyBorder(30, 0, 0, 0));
        chartPanel.add(new JLabel("Pie Chart Pelapor Berdasarkan Usia", SwingConstants.CENTER), BorderLayout.NORTH);

        ArrayBuilder[] chartData = { 
            new ArrayBuilder("Anak", dc.pengaduanByAgeCategory("Anak")),
            new ArrayBuilder("Remaja", dc.pengaduanByAgeCategory("Remaja")),
            new ArrayBuilder("Dewasa", dc.pengaduanByAgeCategory("Dewasa")),
            new ArrayBuilder("Lanjut Usia", dc.pengaduanByAgeCategory("Lanjut Usia"))
        };
        DummyPieChartPanel piePanel = new DummyPieChartPanel(chartData);
        chartPanel.add(piePanel, BorderLayout.CENTER);

        mainPanel.add(cardPanel, BorderLayout.NORTH);
        mainPanel.add(chartPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    // Card statistik sederhana
    private JPanel createStatCard(String title, String value, Color color, Icon icon) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(color);
        card.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        card.setPreferredSize(new Dimension(180, 100));
        card.setOpaque(true);

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblValue.setForeground(Color.WHITE);

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        lblTitle.setForeground(Color.WHITE);

        centerPanel.add(lblValue, BorderLayout.CENTER);
        centerPanel.add(lblTitle, BorderLayout.SOUTH);

        card.add(lblIcon, BorderLayout.WEST);
        card.add(centerPanel, BorderLayout.CENTER);

        return card;
    }

    // Dummy Pie Chart Panel (hanya tampilan statis)
    private static class DummyPieChartPanel extends JPanel {
        ArrayBuilder[] chartData;
        private int[] startAngles;
        private int[] arcAngles;

        public DummyPieChartPanel(ArrayBuilder[] chartData) {
            this.chartData = chartData;
            setOpaque(false);
            setToolTipText(""); // Aktifkan tooltip
            ToolTipManager.sharedInstance().registerComponent(this); // <-- Tambahkan baris ini
            ToolTipManager.sharedInstance().setInitialDelay(100);
            ToolTipManager.sharedInstance().setDismissDelay(4000);
            addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
                @Override
                public void mouseMoved(java.awt.event.MouseEvent e) {
                    int hovered = getSliceIndexAt(e.getX(), e.getY());
                    if (hovered != -1) {
                        setToolTipText(labels[hovered] + " : " + values[hovered]);
                    } else {
                        setToolTipText(null);
                    }
                }
            });
        }

        private int[] values;
        private String[] labels;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();
            int size = Math.min(w, h) - 40;
            int x = (w - size) / 2;
            int y = (h - size) / 2;

            values = new int[chartData.length];
            labels = new String[chartData.length];
            for (int i = 0; i < chartData.length; i++) {
                labels[i] = chartData[i].key;
                values[i] = Integer.parseInt(chartData[i].value.toString());
            }
            Color[] colors = {new Color(0x2196F3), new Color(0xFFC107), new Color(0x4CAF50), new Color(0xF44336)};
            int total = 0;
            for (int v : values) total += v;

            startAngles = new int[values.length];
            arcAngles = new int[values.length];

            int startAngle = 0;
            for (int i = 0; i < values.length; i++) {
                int arc = (int) Math.round(360.0 * values[i] / (total == 0 ? 1 : total));
                g2.setColor(colors[i]);
                g2.fillArc(x, y, size, size, startAngle, arc);
                startAngles[i] = startAngle;
                arcAngles[i] = arc;
                startAngle += arc;
            }
            // Legend (tidak berubah)
            int lx = x + size + 30, ly = y + 10;
            for (int i = 0; i < labels.length; i++) {
                g2.setColor(colors[i]);
                g2.fillRect(lx, ly + i * 30, 20, 20);
                g2.setColor(Color.DARK_GRAY);
                g2.drawString(labels[i], lx + 30, ly + 15 + i * 30);
            }
        }

        // Mendapatkan index slice berdasarkan posisi mouse
        private int getSliceIndexAt(int mx, int my) {
            int w = getWidth();
            int h = getHeight();
            int size = Math.min(w, h) - 40;
            int x = (w - size) / 2;
            int y = (h - size) / 2;
            int cx = x + size / 2;
            int cy = y + size / 2;
            int dx = mx - cx;
            int dy = my - cy;
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist > size / 2) return -1; // di luar lingkaran

            double angle = Math.toDegrees(Math.atan2(-dy, dx));
            if (angle < 0) angle += 360;

            for (int i = 0; i < startAngles.length; i++) {
                int sa = startAngles[i];
                int ea = sa + arcAngles[i];
                if (angle >= sa && angle < ea) {
                    return i;
                }
            }
            return -1;
        }
    }
}
