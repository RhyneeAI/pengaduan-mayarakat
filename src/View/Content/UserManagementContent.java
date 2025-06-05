package View.Content;

import Controller.UserController;
import Helper.MessageHelper;
import Helper.TimeHelper;
import Helper.UIHelper;
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
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

public class UserManagementContent extends JInternalFrame {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private JDateChooser dateChooserStart;
    private JDateChooser dateChooserEnd;
    public JDesktopPane desktopPane;
    public JButton btnSimpan;
    public JTextField txtKategori;
    UserController uc = new UserController();
    TimeHelper th = new TimeHelper();

    public UserManagementContent(JDesktopPane desktopPane) {
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
        JLabel titleLabel = new JLabel("Manajemen User");
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
        

        // === ROW 3: Table ===
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        tableModel = new DefaultTableModel(new String[]{"No", "NIK", "Nama", "Alamat", "Jenis Kelamin", "Usia", "Aksi", "ID", "Is_Active"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; 
            }
        };

        // Inisialisasi table SEKALI saja di sini
        loadDataTable();
        table = new JTable(tableModel);
        table.setRowHeight(24);

        // Center kolom "No"
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // No

        // Kolom "Aksi" pakai tombol
        table.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new ButtonPanelEditor());

        // Sembunyikan kolom ID & Is Active (kolom ke-7 & 8)
        table.removeColumn(table.getColumnModel().getColumn(8));
        table.removeColumn(table.getColumnModel().getColumn(7));
        table.setShowGrid(true);
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true); 

        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // No
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // NIK
        table.getColumnModel().getColumn(2).setPreferredWidth(230); // Nama
        table.getColumnModel().getColumn(3).setPreferredWidth(210); // Alamat
        table.getColumnModel().getColumn(4).setPreferredWidth(50);  // Jenis Kelamin
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Usia
        table.getColumnModel().getColumn(6).setPreferredWidth(140); // Aksi


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
        List<Map<String, Object>> userList = uc.getUserList();
        tableModel.setRowCount(0); // Reset
        int no = 1;

        if (userList != null && !userList.isEmpty()) {
            for (Map<String, Object> row : userList) {
                Object[] rowData = new Object[]{
                    no++,
                    row.get("nik"),
                    row.get("name"),
                    row.get("address"),
                    row.get("gender"),
                    row.get("birth_date"),
                    "Edit",
                    row.get("id"),
                    row.get("is_active").toString()
                };
                tableModel.addRow(rowData);
            }
        }
    }

    // Button Renderer (tampilkan tombol dengan padding di tengah cell)
    public class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton btnIsActive = new JButton();

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            btnIsActive.setPreferredSize(new Dimension(115, 20));
            add(btnIsActive);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                    boolean isSelected, boolean hasFocus,
                                                    int row, int column) {
            this.setOpaque(true);
            this.setBackground(Color.WHITE);
            this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, table.getGridColor()));

            // Ambil status is_active dari model (kolom ke-8)
            int modelRow = table.convertRowIndexToModel(row);
            String isActive = table.getModel().getValueAt(modelRow, 8).toString();
            boolean aktif = isActive.equals("1") || isActive.equalsIgnoreCase("true") || isActive.equals("Y");
            btnIsActive.setText(aktif ? "Aktifkan" : "Non Aktifkan");

            return this;
        }
    }

    class ButtonPanelEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        private final JButton btnIsActive = new JButton();
        private int editingRow = -1;

        public ButtonPanelEditor() {
            panel.add(btnIsActive);

            btnIsActive.addActionListener(e -> {
                if (editingRow == -1) return;
                int modelRow = table.convertRowIndexToModel(editingRow);
                String id = table.getModel().getValueAt(modelRow, 7).toString();
                String isActive = table.getModel().getValueAt(modelRow, 8).toString();
                boolean aktif = isActive.equals("1") || isActive.equalsIgnoreCase("true") || isActive.equals("Y");

                System.out.println(aktif);
                String msg = aktif ? "Non Aktifkan pengguna?" : "Aktifkan pengguna?";

                int confirm = JOptionPane.showConfirmDialog(panel, msg);
                if (confirm == JOptionPane.YES_OPTION) {
                    Map<String, Object> result = uc.activateUser(id, isActive);
                    MessageHelper.showMessageFromResult(result);

                    // Perubahan teks dilakukan setelah message muncul
                    SwingUtilities.invokeLater(() -> {
                        btnIsActive.setText(isActive.equals("1") ? "Non Aktifkan" : "Aktifkan");
                    });

                    fireEditingStopped();
                    loadDataTable();
                } else {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            editingRow = row;
            int modelRow = table.convertRowIndexToModel(row);
            String isActive = table.getModel().getValueAt(modelRow, 8).toString();
            btnIsActive.setText(isActive.equals("1") ? "Aktifkan" : "Non Aktifkan");
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
