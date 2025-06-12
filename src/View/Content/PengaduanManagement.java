package View.Content;

import Controller.PengaduanController;
import Helper.ColorHelper;
import Helper.TimeHelper;
import Helper.UIHelper;
import Lib.ArrayBuilder;
import View.Content.Pengaduan.ProsesPengaduanForm;
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
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;


public class PengaduanManagement extends JInternalFrame {
    public final JTable table;
    private final DefaultTableModel tableModel;
    private JDateChooser dateChooserStart;
    private JDateChooser dateChooserEnd;
    public JDesktopPane desktopPane;
    
    PengaduanController pc = new PengaduanController();
    TimeHelper th = new TimeHelper();
    
    private String currentId = null;

    public PengaduanManagement(JDesktopPane desktopPane) {
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
        JLabel titleLabel = new JLabel("Manajemen Pengaduan");
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
        JLabel tglAwal = new JLabel("Tanggal Awal");
        gbc.gridx = 0;
        gbc.weightx = 0;
        panelForm.add(tglAwal, gbc);

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
        panelForm.add(new JLabel(" s/d "), gbc);

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
        gbc.weightx = 0.2;
        JButton btnFilter = new JButton("Filter");
        panelForm.add(btnFilter, gbc);
        
        btnFilter.addActionListener((ActionEvent e) -> {
            Date startDate = dateChooserStart.getDate();
            Date endDate = dateChooserEnd.getDate();

            if (startDate != null && endDate != null) {
                if (startDate.after(endDate)) {
                    JOptionPane.showMessageDialog(null, "Tanggal mulai tidak boleh lebih dari tanggal akhir.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            
            loadDataTable();
        });
        
        gbc.gridx = 5;
        gbc.gridwidth = 2;
        panelForm.add(new JLabel(""), gbc);

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
        table.getColumnModel().getColumn(5).setCellEditor(new ButtonPanelEditor(desktopPane));

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
        table.getColumnModel().getColumn(5).setPreferredWidth(160); // Aksi (Edit button)

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
        ArrayBuilder[] condition = {
            new ArrayBuilder("date >=", TimeHelper.setYMD(dateChooserStart.getDate())),
            new ArrayBuilder("date <=", TimeHelper.setYMD(dateChooserEnd.getDate()))
        };

        List<Map<String, Object>> pengaduanList = pc.getPengaduan(condition, new ArrayBuilder("newest", ""));

        tableModel.setRowCount(0); // Reset
        int no = 1;

        if (pengaduanList != null && !pengaduanList.isEmpty()) {
            for (Map<String, Object> row : pengaduanList) {
                String title = row.get("title").toString();
                if (title.length() > 40) {
                    title = title.substring(0, 40) + "...";
                }

                Object[] rowData = new Object[]{
                    no++,
                    TimeHelper.humanizeDate((Date) row.get("date")),
                    title,
                    row.get("category_name"),
                    row.get("status"),
                    "Terima",
                    row.get("id") // kolom ke-6 (index 6), disembunyikan
                };
                tableModel.addRow(rowData);
            }
        }
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
        private final JButton buttonApprove = new JButton("Terima");
        private final JButton buttonNotApprove = new JButton("Tolak");
        private final JButton buttonView = new JButton("Lihat");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            setPreferredSize(new Dimension(160, 30));
            buttonApprove.setPreferredSize(new Dimension(70, 20));
            buttonNotApprove.setPreferredSize(new Dimension(70, 20));
            buttonView.setPreferredSize(new Dimension(70, 20));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                    boolean isSelected, boolean hasFocus,
                                                    int row, int column) {
            this.removeAll();
            this.setOpaque(true);
            this.setBackground(Color.WHITE);
            this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, table.getGridColor()));

            // Ambil status dari model (kolom ke-4/index 4)
            int modelRow = table.convertRowIndexToModel(row);
            String status = table.getModel().getValueAt(modelRow, 4).toString();

            if ("New".equalsIgnoreCase(status)) {
                this.add(buttonApprove);
                this.add(buttonNotApprove);
            } else {
                this.add(buttonView);
            }
            return this;
        }
    }

    class ButtonPanelEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        private final JButton buttonApprove = new JButton("Terima");
        private final JButton buttonNotApprove = new JButton("Tolak");
        private final JButton buttonView = new JButton("Lihat");

        public ButtonPanelEditor(JDesktopPane desktopPane) {
            panel.setPreferredSize(new Dimension(160, 30));

            buttonApprove.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(panel, "Terima pengaduan?");
                if (confirm == JOptionPane.YES_OPTION) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = table.convertRowIndexToModel(selectedRow);
                        String id = table.getModel().getValueAt(modelRow, 6).toString();
                        List<ArrayBuilder> data = new ArrayList<>();
                        data.add(new ArrayBuilder("status", "Accepted"));
                        pc.updatePengaduan(id, data);
                        fireEditingStopped();
                        loadDataTable();
                    }
                }
            });

            buttonNotApprove.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(panel, "Tolak pengaduan?");
                if (confirm == JOptionPane.YES_OPTION) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = table.convertRowIndexToModel(selectedRow);
                        String id = table.getModel().getValueAt(modelRow, 6).toString();
                        List<ArrayBuilder> data = new ArrayList<>();
                        data.add(new ArrayBuilder("status", "Rejected"));
                        pc.updatePengaduan(id, data);
                        fireEditingStopped();
                        loadDataTable();
                    }
                }
            });

            buttonView.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = table.convertRowIndexToModel(selectedRow);
                    currentId = table.getModel().getValueAt(modelRow, 6).toString();
                    
                    String id = table.getModel().getValueAt(modelRow, 6).toString();
                    List<ArrayBuilder> data = new ArrayList<>();
                    data.add(new ArrayBuilder("status", "Process"));
                    pc.updatePengaduan(id, data);

                    ProsesPengaduanForm form = new ProsesPengaduanForm(desktopPane, currentId);
                    desktopPane.removeAll();
                    desktopPane.repaint();
                    desktopPane.add(form);
                    form.setVisible(true);
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            panel.removeAll();
            int modelRow = table.convertRowIndexToModel(row);
            String status = table.getModel().getValueAt(modelRow, 4).toString();

            if ("New".equalsIgnoreCase(status)) {
                panel.add(buttonApprove);
                panel.add(buttonNotApprove);
            } else {
                panel.add(buttonView);
            }
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
