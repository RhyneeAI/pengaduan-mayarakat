package View.Content;

import Controller.PengaduanController;
import Helper.ColorHelper;
import Helper.TimeHelper;
import Helper.UIHelper;
import View.Content.Pengaduan.TambahPengaduanForm;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

public class PengaduanContent extends JInternalFrame {
    private JTable table;
    private final DefaultTableModel tableModel;
    private JDateChooser dateChooserStart;
    private JDateChooser dateChooserEnd;
    
    PengaduanController pc = new PengaduanController();
    TimeHelper th = new TimeHelper();

    public PengaduanContent(JDesktopPane desktopPane) {
        super("", false, false, false, false);
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
        dateChooserStart.setDate(th.getFirstDayOfMonth());
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
        dateChooserEnd.setDate(th.getDateNow());
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
        btnTambah.addActionListener(e -> {
            TambahPengaduanForm form = new TambahPengaduanForm(desktopPane);
            desktopPane.removeAll();
            desktopPane.repaint();
            desktopPane.add(form);
            form.setVisible(true);
        });
        panelForm.add(btnTambah, gbc);

        // === ROW 3: Table ===
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        tableModel = new DefaultTableModel(new String[]{"No", "Tanggal", "Judul", "Kategori", "Status", "Aksi", "ID"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; 
            }
        };
        loadDataTable();

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
    
    public final void loadDataTable() {
        List<Map<String, Object>> pengaduanList = pc.getPengaduan();

        tableModel.setRowCount(0); // Reset
        int no = 1;

        if (pengaduanList != null && !pengaduanList.isEmpty()) {
            for (Map<String, Object> row : pengaduanList) {
                String title = row.get("title").toString();
                if (title.length() > 15) {
                    title = title.substring(0, 15) + "...";
                }

                Object[] rowData = new Object[]{
                    no++,
                    TimeHelper.humanizeDate((Date) row.get("date")),
                    title,
                    row.get("category_name"),
                    row.get("status"),
                    "Edit",
                    row.get("id") // kolom ke-6 (index 6), disembunyikan
                };
                tableModel.addRow(rowData);
            }
        }

        // Inisialisasi tabel setelah isi data
        table = new JTable(tableModel);
        table.setRowHeight(24);

        // Center kolom "No"
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // No

        // Kolom "Status" pakai renderer warna
        table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());

        // Kolom "Aksi" pakai tombol
        table.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Sembunyikan kolom ID (kolom ke-6 / index 6)
        table.removeColumn(table.getColumnModel().getColumn(6));
        table.setShowGrid(true);
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true); 
        
        // Atur lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // No
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Date
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // Title
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // Category
        table.getColumnModel().getColumn(4).setPreferredWidth(80);  // Status
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Aksi (Edit button)

    }
    
    // Status cell renderer (warna background biru muda & teks tengah)
    class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {

            JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setOpaque(true);
            label.setBackground(Color.WHITE);

            String status = value.toString().toLowerCase();

            switch (status) {
                case "new" -> label.setForeground(ColorHelper.INFO);
                case "process" -> label.setForeground(ColorHelper.WARNING);
                case "accepted" -> label.setForeground(ColorHelper.SUCCESS);
                case "rejected" -> label.setForeground(ColorHelper.DANGER);
                default -> label.setForeground(ColorHelper.PRIMARY);
            }

            return label;
        }
    }


    // Button Renderer (tampilkan tombol dengan padding di tengah cell)
    public class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton button;

        public ButtonRenderer() {
            setLayout(new GridBagLayout()); // centering
            button = new JButton("Edit");
            button.setPreferredSize(new Dimension(80, 20));
            add(button);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            this.setOpaque(true);
            this.setBackground(Color.WHITE);

            // Set border manual agar terlihat seperti grid
            this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, table.getGridColor()));

            button.setText((value == null) ? "Edit" : value.toString());
            return this;
        }
    }


    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean clicked;
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setMargin(new Insets(2, 8, 2, 8)); // agar tombol tidak full 100% lebar
            button.setFont(button.getFont().deriveFont(Font.PLAIN, 12));
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.row = row;
            clicked = true;

            // Tambahkan border agar terlihat grid-nya
            button.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, table.getGridColor()));

            Object idObj = table.getModel().getValueAt(table.convertRowIndexToModel(row), 6);
            button.putClientProperty("id", idObj);

            label = (value == null) ? "Edit" : value.toString();
            button.setText(label);
            return button;
        }


        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                Object idObj = button.getClientProperty("id");
                if (idObj != null) {
                    String id = idObj.toString();
                    Map<String, Object> data = pc.getPengaduanById(id);
                    if (data != null) {
                        StringBuilder info = new StringBuilder();
                        info.append("ID: ").append(data.get("id")).append("\n");
                        info.append("Date: ").append(data.get("date")).append("\n");
                        info.append("Title: ").append(data.get("title")).append("\n");
                        info.append("Category: ").append(data.get("category_name")).append("\n");
                        info.append("Status: ").append(data.get("status")).append("\n");

                        JOptionPane.showMessageDialog(button, info.toString(), "Detail Pengaduan", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(button, "Data tidak ditemukan untuk ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(button, "ID tidak tersedia", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            clicked = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}
