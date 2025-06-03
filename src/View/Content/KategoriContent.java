package View.Content;

import Controller.KategoriController;
import Helper.MessageHelper;
import Helper.UIHelper;
import Lib.ArrayBuilder;
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

public class KategoriContent extends JInternalFrame {
    private JTable table;
    private final DefaultTableModel tableModel;
    public JDesktopPane desktopPane;
    public JButton btnSimpan;
    public JTextField txtKategori;
    KategoriController kc = new KategoriController();

    // Tambahkan field ini:
    private String currentId = null;

    public KategoriContent(JDesktopPane desktopPane) {
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
        JLabel titleLabel = new JLabel("Kategori Pengaduan");
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

        // === ROW 2: Inputan Kategori dan Tombol Simpan ===
        txtKategori = new JTextField(20); // Input kategori
        btnSimpan = new JButton("Simpan"); // Tombol simpan
        
        btnSimpan.addActionListener(e -> {
            if(btnSimpan.getText().equals("Simpan")) {
                // aksi simpan
                List<ArrayBuilder> kategoriData = new ArrayList<>();
                kategoriData.add(new ArrayBuilder("category_name", txtKategori.getText()));
                Map<String, Object> result = kc.setKategori(kategoriData);
                MessageHelper.showMessageFromResult(result);
                txtKategori.setText("");
                loadDataTable();
            } else if(btnSimpan.getText().equals("Update")) {
                // aksi update
                List<ArrayBuilder> kategoriData = new ArrayList<>();
                kategoriData.add(new ArrayBuilder("category_name", txtKategori.getText()));
                Map<String, Object> result = kc.updateKategori(currentId, kategoriData);
                MessageHelper.showMessageFromResult(result);
                txtKategori.setText("");
                btnSimpan.setText("Simpan");
                loadDataTable();
                currentId = null; // reset setelah update
            }
        });
            

        // Tambahkan ke panel
        JPanel filterPanel = new JPanel(new GridBagLayout()); // Gunakan GridBag untuk kontrol lebih fleksibel
        GridBagConstraints gbcFilter = new GridBagConstraints();
        gbcFilter.insets = new Insets(5, 5, 5, 5); // Spasi antar elemen

        // Input Kategori di sisi kiri
        gbcFilter.gridx = 0;
        gbcFilter.gridy = 0;
        gbcFilter.weightx = 1.0; // Agar input lebih fleksibel
        gbcFilter.fill = GridBagConstraints.HORIZONTAL;
        filterPanel.add(txtKategori, gbcFilter);

        // Tombol Simpan di ujung kanan
        gbcFilter.gridx = 1;
        gbcFilter.weightx = 0; // Tombol tidak fleksibel, ukurannya tetap
        gbcFilter.anchor = GridBagConstraints.EAST; // Menempatkan tombol di kanan
        filterPanel.add(btnSimpan, gbcFilter);

        // Tambahkan ke `panelForm` pada posisi row 2
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 7; // Lebarkan agar mencakup seluruh lebar panel
        panelForm.add(filterPanel, gbc);


        // === ROW 3: Table ===
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        tableModel = new DefaultTableModel(new String[]{"No", "Nama Kategori", "Aksi", "ID"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; 
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
        table.getColumnModel().getColumn(2).setCellRenderer(new ButtonRenderer());
        table.getColumnModel().getColumn(2).setCellEditor(new ButtonPanelEditor());

        // Sembunyikan kolom ID (kolom ke-3 / index 3)
        table.removeColumn(table.getColumnModel().getColumn(3));
        table.setShowGrid(true);
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true); 
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // No
        table.getColumnModel().getColumn(1).setPreferredWidth(250); // Category
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Aksi

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
        List<Map<String, Object>> kategoriList = kc.getKategori();
        tableModel.setRowCount(0); // Reset
        int no = 1;

        if (kategoriList != null && !kategoriList.isEmpty()) {
            for (Map<String, Object> row : kategoriList) {
                String category_name = row.get("category_name").toString();
                if (category_name.length() > 25) {
                    category_name = category_name.substring(0, 25) + "...";
                }
                Object[] rowData = new Object[]{
                    no++,
                    category_name,
                    "Edit",
                    row.get("id")
                };
                tableModel.addRow(rowData);
            }
        }
    }

    // Button Renderer (tampilkan tombol dengan padding di tengah cell)
    public class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton buttonEdit;
        private final JButton buttonDelete;

        public ButtonRenderer() {
            setLayout(new GridBagLayout()); // centering
            buttonEdit = new JButton("Edit");
            buttonEdit.setPreferredSize(new Dimension(70, 20));
            add(buttonEdit);
            
            buttonDelete = new JButton("Delete");
            buttonDelete.setPreferredSize(new Dimension(70, 20));
            add(buttonDelete);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            this.setOpaque(true);
            this.setBackground(Color.WHITE);

            // Set border manual agar terlihat seperti grid
            this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, table.getGridColor()));
            buttonEdit.setText("Edit");
            buttonDelete.setText("Delete");
            
            return this;
        }
    }

    class ButtonPanelEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        private final JButton btnEdit = new JButton("Edit");
        private final JButton btnDelete = new JButton("Delete");

        public ButtonPanelEditor() {
            panel.add(btnEdit);
            panel.add(btnDelete);

            btnEdit.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = table.convertRowIndexToModel(selectedRow);
                    String namaKategori = (String) table.getModel().getValueAt(modelRow, 1);
                    txtKategori.setText(namaKategori);
                    // Set currentId dari kolom ID (kolom ke-3 di model)
                    currentId = table.getModel().getValueAt(modelRow, 3).toString();
                }
                btnSimpan.setText("Update");
                fireEditingStopped();
            });

            btnDelete.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(panel, "Yakin hapus data?");
                if (confirm == JOptionPane.YES_OPTION) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = table.convertRowIndexToModel(selectedRow);
                        String id = table.getModel().getValueAt(modelRow, 3).toString();
                        kc.deleteKategori(id);
                        loadDataTable();
                    }
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            // Tidak perlu set currentId di sini, sudah diatur di btnEdit
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
