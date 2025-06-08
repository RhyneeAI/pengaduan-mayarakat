package View.Content.Pengaduan;
import com.toedter.calendar.JDateChooser;

import Lib.ArrayBuilder;
import Helper.ColorHelper;
import Helper.RoundedButton;
import Helper.TimeHelper;
import Helper.UIHelper;
import Controller.PengaduanController;
import Helper.MessageHelper;
import Lib.Session;
import View.Content.PengaduanContent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EditPengaduanForm extends JInternalFrame {
    public EditPengaduanForm(JDesktopPane desktopPane, String idPengaduan) {
        super("", false, false, false, false);
        
        PengaduanController pc = new PengaduanController();
        System.out.println(idPengaduan);
        Map<String, Object> pengaduanData = pc.getPengaduanById(idPengaduan);
        
        setBorder(null);
        setLayout(new BorderLayout());
        
        int width = desktopPane.getWidth();
        int height = desktopPane.getWidth();
        setBounds(0, 0, width, height);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // === Row 0: Title (4 kolom)
        JLabel titleLabel = new JLabel("Edit Pengaduan");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5; // Span 4 columns
        panelForm.add(titleLabel, gbc);

        // === Row 1: Spacer
        gbc.gridy++;
        gbc.gridwidth = 5;
        panelForm.add(Box.createVerticalStrut(35), gbc);

        // === Row 2: Judul
        JLabel judul = new JLabel("Judul");
        judul.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panelForm.add(judul, gbc);

        JTextField txtJudul = new JTextField();
        txtJudul.setText(pengaduanData.get("title").toString());
        gbc.gridx = 1;
        gbc.gridwidth = 4;
        panelForm.add(txtJudul, gbc);

        // === Row 3: Tanggal & Kategori
        JLabel tanggal = new JLabel("Tanggal");
        tanggal.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panelForm.add(tanggal, gbc);

        JDateChooser dcTanggal = new JDateChooser();
        dcTanggal.getDateEditor().setEnabled(false);
        dcTanggal.setPreferredSize(new Dimension(150, 25));
        dcTanggal.setDate(TimeHelper.parseDate(pengaduanData.get("date").toString()));
        gbc.gridx = 1;
        panelForm.add(dcTanggal, gbc);
        
        // Spacer
        gbc.gridx = 2;
        panelForm.add(new JLabel(""), gbc);

        JLabel kategori = new JLabel("Kategori");
        kategori.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtJudul.setText(pengaduanData.get("title").toString());
        gbc.gridx = 3;
        panelForm.add(kategori, gbc);

        String[] kategoriOptions = pc.getKategoriOptions();
        JComboBox<String> cbKategori = new JComboBox<>(kategoriOptions);
        cbKategori.setSelectedItem(pengaduanData.get("category_name").toString());
        gbc.gridx = 4;
        panelForm.add(cbKategori, gbc);

        // === Row 4: Isi
        JLabel isi = new JLabel("Isi");
        isi.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridy++;
        gbc.gridx = 0;
        panelForm.add(isi, gbc);

        JTextArea txtaIsi = new JTextArea(4, 20);
        txtaIsi.setText(pengaduanData.get("body").toString());
        JScrollPane scrollIsi = new JScrollPane(txtaIsi);
        gbc.gridx = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        panelForm.add(scrollIsi, gbc);
        
        // === Row 5: Public Checkbox
        gbc.gridy++;
        gbc.gridx = 1;
//        gbc.gridwidth = 1;

        JCheckBox chcbPublic = new JCheckBox("Tampilkan ke Publik?");
        chcbPublic.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chcbPublic.setSelected((boolean) pengaduanData.get("is_public"));
        panelForm.add(chcbPublic, gbc);

        // === Row 6: Spacer
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(Box.createVerticalStrut(10), gbc);

        // === Row 6: Button di kanan bawah
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        RoundedButton btnKembali = new RoundedButton("Kembali");
        btnKembali.setBackground(ColorHelper.SECONDARY);
        btnKembali.setForeground(ColorHelper.getContrastColor(ColorHelper.SECONDARY));
        btnKembali.addActionListener(e -> {
            PengaduanContent pcon = new PengaduanContent(desktopPane);
            desktopPane.removeAll();
            desktopPane.repaint();
            desktopPane.add(pcon);
            pcon.setVisible(true);
        });
        btnPanel.add(btnKembali);

        RoundedButton btnSimpan = new RoundedButton("Simpan");
        btnSimpan.setBackground(ColorHelper.SUCCESS);
        btnSimpan.setForeground(ColorHelper.getContrastColor(ColorHelper.SUCCESS));
        btnSimpan.addActionListener(e -> {
            List<ArrayBuilder> data = new ArrayList<>();
            String tanggalPengaduan = dcTanggal.getDate() == null ? "" : new java.text.SimpleDateFormat("yyyy-MM-dd").format(dcTanggal.getDate());

            data.add(new ArrayBuilder("title", txtJudul.getText()));
            data.add(new ArrayBuilder("date", tanggalPengaduan));
            data.add(new ArrayBuilder("body", txtaIsi.getText()));
            data.add(new ArrayBuilder("category", (String) cbKategori.getSelectedItem()));
            data.add(new ArrayBuilder("status", "New"));
            data.add(new ArrayBuilder("is_public", chcbPublic.isSelected() ? "1" : "0"));
            data.add(new ArrayBuilder("user_id", Session.get("id")));
            
            Map<String, Object> result = pc.updatePengaduan(idPengaduan, data);
            MessageHelper.showMessageFromResult(result);

            if (Boolean.TRUE.equals(result.get("status"))) {
                PengaduanContent pcon = new PengaduanContent(desktopPane);
                desktopPane.removeAll();
                desktopPane.repaint();
                desktopPane.add(pcon);
                pcon.setVisible(true);
            }
        });
        btnPanel.add(btnSimpan);

        btnPanel.setBackground(Color.WHITE);
        gbc.gridy++;
        gbc.gridx = 4;  // kolom terakhir
        gbc.gridwidth = 1;
        panelForm.add(btnPanel, gbc);

        // === Tambahkan ke frame ===
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(Color.decode("#FFFFFF")); 
        wrapperPanel.add(panelForm, BorderLayout.NORTH);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        wrapperPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Panel luar sebagai pembungkus wrapper
        JPanel outerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 30));
        outerPanel.setBackground(new Color(240, 240, 240)); 
        outerPanel.add(wrapperPanel);
        
        add(outerPanel, BorderLayout.CENTER);
        
        UIHelper.syncInternalFrameWithDesktop(this, desktopPane, outerPanel, wrapperPanel);
    }
}
