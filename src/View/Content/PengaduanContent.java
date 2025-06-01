package View.Content;

import Helper.UIHelper;
import View.Content.Pengaduan.TambahPengaduanForm;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;

public class PengaduanContent extends JInternalFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private JDateChooser dateChooserStart;
    private JDateChooser dateChooserEnd;
    private JDesktopPane desktopPane;

    public PengaduanContent(JDesktopPane desktopPane) {
        super("", false, false, false, false);
        this.desktopPane = desktopPane;
        Color bgColor = Color.WHITE;

        setBorder(null);
        setLayout(new BorderLayout());

        // === Panel utama yang membungkus semuanya ===
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelForm.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // === ROW 0: Title ===
        JLabel titleLabel = new JLabel("Pengaduan");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 7;
        gbc.anchor = GridBagConstraints.CENTER;
        panelForm.add(titleLabel, gbc);

        // === ROW 1: Spacer ===
        gbc.gridy = 1;
        panelForm.add(Box.createVerticalStrut(35), gbc);

        // === ROW 2: Filter Bar ===
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Label Tanggal Awal
        gbc.gridx = 0;
        gbc.weightx = 0;
        panelForm.add(new JLabel("Tanggal Awal"), gbc);

        // DateChooser Start
        gbc.gridx = 1;
        gbc.weightx = 0.2;
        dateChooserStart = new JDateChooser();
        dateChooserStart.getDateEditor().setEnabled(false);
        dateChooserStart.setPreferredSize(new Dimension(150, 25));
        panelForm.add(dateChooserStart, gbc);

        // Label Tanggal Akhir
        gbc.gridx = 2;
        gbc.weightx = 0;
        panelForm.add(new JLabel("Tanggal Akhir"), gbc);

        // DateChooser End
        gbc.gridx = 3;
        gbc.weightx = 0.2;
        dateChooserEnd = new JDateChooser();
        dateChooserEnd.getDateEditor().setEnabled(false);
        dateChooserEnd.setPreferredSize(new Dimension(150, 25));
        panelForm.add(dateChooserEnd, gbc);

        // Tombol Filter
        gbc.gridx = 4;
        JButton btnFilter = new JButton("Filter");
        panelForm.add(btnFilter, gbc);
        
        // Spacer
        gbc.gridx = 5;
        panelForm.add(new JLabel(""), gbc);

        // Tombol Tambah (di kanan)
        gbc.gridx = 6;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnTambah = new JButton("Buat Pengaduan");
        panelForm.add(btnTambah, gbc);

        // === ROW 3: Table ===
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        tableModel = new DefaultTableModel(new String[]{"No", "Tanggal", "Judul", "Kategori", "Status", "Aksi"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(20);

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(null);
        tableScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panelForm.add(tableScrollPane, gbc);


        // === Wrapper Panel ===
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(bgColor);
        wrapperPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbcWrapper = new GridBagConstraints();
        gbcWrapper.gridx = 0;
        gbcWrapper.gridy = 0;
        gbcWrapper.fill = GridBagConstraints.BOTH;
        gbcWrapper.weightx = 1.0;
        gbcWrapper.weighty = 1.0;
        wrapperPanel.add(panelForm, gbcWrapper);

        // === Outer Panel ===
        JPanel outerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        outerPanel.setBackground(new Color(240, 240, 240));
        outerPanel.add(wrapperPanel);

        add(outerPanel, BorderLayout.CENTER);

        UIHelper.syncInternalFrameWithDesktop(this, desktopPane, outerPanel, wrapperPanel);
    }

    private Date getFirstDayOfMonth(Date date) {
        return new Date(date.getYear(), date.getMonth(), 1);
    }
}
