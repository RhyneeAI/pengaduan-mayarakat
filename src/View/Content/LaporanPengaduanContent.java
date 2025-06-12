package View.Content;

import Controller.PengaduanController;
import Helper.ColorHelper;
import Helper.TimeHelper;
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
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import javax.swing.table.DefaultTableCellRenderer;


public class LaporanPengaduanContent extends JInternalFrame {
    public final JTable table;
    private final DefaultTableModel tableModel;
    private JDateChooser dateChooserStart;
    private JDateChooser dateChooserEnd;
    private JComboBox<String> comboStatus;
    public JDesktopPane desktopPane;
    
    PengaduanController pc = new PengaduanController();
    TimeHelper th = new TimeHelper();
    
    private String currentId = null;

    public LaporanPengaduanContent(JDesktopPane desktopPane) {
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
        JLabel titleLabel = new JLabel("Laporan Pengaduan");
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
        JLabel tglAwal = new JLabel("Tanggal");
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

        gbc.gridx = 4;
        gbc.weightx = 0.2;
        comboStatus = new JComboBox<>(new String[]{
            "Semua Status", "Terbaru", "Diproses", "Diterima", "Ditolak", "Selesai"
        });
        comboStatus.setPreferredSize(new Dimension(120, 25));
        panelForm.add(comboStatus, gbc);

        // Tombol Filter
        gbc.gridx = 5;
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
        
        gbc.gridx = 6;
        gbc.weightx = 0.2;
        JButton btnExport = new JButton("Export PDF");
        panelForm.add(btnExport, gbc);
        

        // === ROW 3: Table ===
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        tableModel = new DefaultTableModel(new String[]{"No", "Tanggal", "Judul", "Kategori", "Status", "ID"}, 0);
        
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

        // Sembunyikan kolom ID (kolom ke-6 / index 6)
        table.removeColumn(table.getColumnModel().getColumn(5));
        table.setShowGrid(true);
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true); 
        
        // Atur lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(50);  // No
        table.getColumnModel().getColumn(1).setPreferredWidth(100); // Date
        table.getColumnModel().getColumn(2).setPreferredWidth(200); // Title
        table.getColumnModel().getColumn(3).setPreferredWidth(150); // Category
        table.getColumnModel().getColumn(4).setPreferredWidth(80);  // Status

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setBorder(null);
        tableScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panelForm.add(tableScrollPane, gbc);
        
        
        // Export PDF
        btnExport.addActionListener((ActionEvent e) -> {
            Date startDate = dateChooserStart.getDate();
            Date endDate = dateChooserEnd.getDate();

            if (startDate != null && endDate != null) {
                if (startDate.after(endDate)) {
                    JOptionPane.showMessageDialog(null, "Tanggal mulai tidak boleh lebih dari tanggal akhir.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            loadDataTable();
            
            String userHome = System.getProperty("user.home");
            String filePath = userHome + "/Downloads/laporan_pengaduan.pdf";
            exporToPDF(table, filePath);
        });

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
    
    public void exporToPDF(JTable table, String filePath) {
        try {
            String from = TimeHelper.humanizeDate(dateChooserStart.getDate());
            String end = TimeHelper.humanizeDate(dateChooserEnd.getDate());
            
            com.lowagie.text.Document document = new com.lowagie.text.Document();
            com.lowagie.text.pdf.PdfWriter.getInstance(document, new java.io.FileOutputStream(filePath + " " + from + " " + end));
            document.open();

            // Judul dokumen
            com.lowagie.text.Paragraph title = new com.lowagie.text.Paragraph(
                "Laporan Pengaduan",
                new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 18, com.lowagie.text.Font.BOLD)
            );
            title.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            title.setSpacingAfter(2);
            document.add(title);
            
            
            com.lowagie.text.Paragraph period = new com.lowagie.text.Paragraph(
                "Per tanggal " + from + " s/d " + end,
                new com.lowagie.text.Font(com.lowagie.text.Font.HELVETICA, 15, com.lowagie.text.Font.BOLD)
            );
            period.setAlignment(com.lowagie.text.Element.ALIGN_CENTER);
            period.setSpacingAfter(40);
            document.add(period);

            // Kolom yang akan diexport (tanpa "Aksi")
            int[] exportCols = {0, 1, 2, 3, 4}; // No, Tanggal, Judul, Kategori, Status

            com.lowagie.text.pdf.PdfPTable pdfTable = new com.lowagie.text.pdf.PdfPTable(exportCols.length);
            pdfTable.setWidthPercentage(100);
            pdfTable.setWidths(new float[]{15, 50, 130, 60, 40}); // Atur lebar kolom

            // Header
            for (int col : exportCols) {
                com.lowagie.text.pdf.PdfPCell cell = new com.lowagie.text.pdf.PdfPCell(
                    new com.lowagie.text.Phrase(table.getColumnName(col))
                );
                
                cell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                cell.setBackgroundColor(new java.awt.Color(220, 220, 220));
                cell.setFixedHeight(20f);
                pdfTable.addCell(cell);
            }

            // Rows
            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col : exportCols) {
                    Object value = table.getValueAt(row, col);
                    com.lowagie.text.pdf.PdfPCell cell = new com.lowagie.text.pdf.PdfPCell(
                        new com.lowagie.text.Phrase(value != null ? value.toString() : "")
                    );
                    
                    if(col == 2) {
                        cell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_LEFT);
                    } else {
                        cell.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
                    }
                    cell.setFixedHeight(18f);
                    pdfTable.addCell(cell);
                }
            }

            document.add(pdfTable);
            document.close();
            
            try {
                // Buka file PDF secara otomatis di Linux
                String fullPath = filePath + " " + from + " " + end;
                java.awt.Desktop.getDesktop().open(new java.io.File(fullPath));
            } catch (Exception ex) {
                // Jika gagal, tampilkan pesan error (misal xdg-open tidak tersedia)
                JOptionPane.showMessageDialog(null, "PDF berhasil dibuat, tapi gagal membuka otomatis: " + ex.getMessage());
            }

//            JOptionPane.showMessageDialog(null, "PDF berhasil dibuat di: " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal export PDF: " + e.getMessage());
        }
    }
    
    public final void loadDataTable() {
        ArrayList<ArrayBuilder> conditionList = new ArrayList<>();
        conditionList.add(new ArrayBuilder("date >=", TimeHelper.setYMD(dateChooserStart.getDate())));
        conditionList.add(new ArrayBuilder("date <=", TimeHelper.setYMD(dateChooserEnd.getDate())));

        // Ambil status dari combo box
        String selectedStatus = (String) comboStatus.getSelectedItem();
        if (selectedStatus != null && !"Semua Status".equals(selectedStatus)) {
            // Mapping ke value di database
            String dbStatus = switch (selectedStatus) {
                case "Terbaru" -> "New";
                case "Diproses" -> "Process";
                case "Diterima" -> "Accepted";
                case "Ditolak" -> "Rejected";
                case "Selesai" -> "Finished";
                default -> "";
            };
            if (!dbStatus.isEmpty()) {
                conditionList.add(new ArrayBuilder("status", dbStatus));
            }
        }

        ArrayBuilder[] condition = conditionList.toArray(new ArrayBuilder[0]);
        List<Map<String, Object>> pengaduanList = pc.getPengaduan(condition, new ArrayBuilder("newest", ""));

        tableModel.setRowCount(0); // Reset
        int no = 1;

        if (pengaduanList != null && !pengaduanList.isEmpty()) {
            for (Map<String, Object> row : pengaduanList) {
                String title = row.get("title").toString();
                if (title.length() > 40) {
                    title = title.substring(0, 40) + "...";
                }
                
                String status = row.get("status").toString();
                String localizedStatus = switch (status) {
                    case "New" -> "Terbaru";
                    case "Process" -> "Diproses";
                    case "Accepted" -> "Diterima";
                    case "Rejected" -> "Ditolak";
                    case "Finished" -> "Selesai";
                    default -> status;
                };

                Object[] rowData = new Object[]{
                    no++,
                    TimeHelper.humanizeDate((Date) row.get("date")),
                    title,
                    row.get("category_name"),
                    localizedStatus,
                    row.get("id") // kolom ke-5 (index 5), disembunyikan
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

            String status = value.toString();

            switch (status) {
                case "Terbaru" -> label.setForeground(ColorHelper.INFO);
                case "Diproses" -> label.setForeground(ColorHelper.WARNING);
                case "Diterima" -> label.setForeground(ColorHelper.PRIMARY);
                case "Ditolak" -> label.setForeground(ColorHelper.DANGER);
                default -> label.setForeground(ColorHelper.SUCCESS);
            }

            return label;
        }
    }
}
