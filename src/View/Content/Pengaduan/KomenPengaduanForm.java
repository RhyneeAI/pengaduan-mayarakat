package View.Content.Pengaduan;
import Helper.ColorHelper;
import Helper.RoundedButton;
import Helper.TimeHelper;
import Helper.UIHelper;
import Controller.PengaduanController;
import Helper.MessageHelper;
import Lib.ArrayBuilder;
import Lib.Session;
import View.Content.DashboardUserContent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyVetoException;
import java.util.ArrayList;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KomenPengaduanForm extends JInternalFrame {
    private List<Map<String, Object>> komentarList = null;
    PengaduanController pc = new PengaduanController();
    TimeHelper th = new TimeHelper();
    JPanel komentarPanel = new JPanel();
    String idPengaduan;
    
    public KomenPengaduanForm(JDesktopPane desktopPane, String idPengaduan) {
        super("", false, false, false, false);
        this.idPengaduan = idPengaduan;
        
        komentarList = new PengaduanController().getKomenPengaduanById(idPengaduan);
        int komentarCount = komentarList != null ? komentarList.size() : 1;
        int totalHeight = komentarCount * 65;
        int emptyBorderTop = 0;
        if(komentarCount != 0) {
            emptyBorderTop = totalHeight * 2;
        }
//        System.out.println(idPengaduan);
        Map<String, Object> pengaduanData = pc.getPengaduanById(idPengaduan);
        Map<String, Object> tanggapanData = pc.getTanggapanById(idPengaduan);
        
        Font txtFont = new Font("SansSerif", Font.PLAIN, 16);
        
        setBorder(null);
        setLayout(new BorderLayout());
        
        int width = desktopPane.getWidth();
        int height = desktopPane.getWidth();
        setBounds(0, 0, width, height);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(emptyBorderTop, 50, 20, 60));
        panelForm.setBackground(Color.WHITE);
//        panelForm.setPreferredSize(new Dimension(600, panelForm.getPreferredSize().height));
//        panelForm.setPreferredSize(new Dimension(600, 2400));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // === Row 0: Title (span 5 kolom)
        JLabel titleLabel = new JLabel(pengaduanData.get("title").toString());
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        panelForm.add(titleLabel, gbc);

        // === Row 1: Spacer
        gbc.gridy++;
        gbc.gridwidth = 4;
        panelForm.add(Box.createVerticalStrut(55), gbc);

        // === Row 2: Tanggal
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        JLabel lblTanggal = new JLabel("Tanggal :");
        lblTanggal.setFont(txtFont);
        panelForm.add(lblTanggal, gbc);

        JLabel lblTanggalPengaduan = new JLabel(TimeHelper.humanizeDate(TimeHelper.parseDate(pengaduanData.get("date").toString())));
        lblTanggalPengaduan.setFont(txtFont);
        gbc.gridx = 1;
        panelForm.add(lblTanggalPengaduan, gbc);

        // === Row 2: Kategori
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblKategori = new JLabel("Kategori :");
        lblKategori.setFont(txtFont);
        panelForm.add(lblKategori, gbc);

        JLabel lblKategoriPengaduan = new JLabel(pengaduanData.get("category_name").toString());
        lblKategoriPengaduan.setFont(txtFont);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panelForm.add(lblKategoriPengaduan, gbc);

        // === Row 4: Spacer
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        panelForm.add(Box.createVerticalStrut(10), gbc);

        // === Row 5: Isi
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblIsi = new JLabel("Isi :");
        lblIsi.setFont(txtFont);
        panelForm.add(lblIsi, gbc);

        JLabel lblIsiPengaduan = new JLabel("<html><div style='width:90%'>" + pengaduanData.get("body").toString().replace("\n", "<br>") + "</div></html>");
        lblIsiPengaduan.setFont(txtFont);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(lblIsiPengaduan, gbc);

        // === Row 6: Spacer
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        panelForm.add(Box.createVerticalStrut(10), gbc);

        // === Row 6: Status Publik
//        gbc.gridy++;
//        gbc.gridx = 0;
//        gbc.gridwidth = 1;
//        panelForm.add(new JLabel("Status:"), gbc);
//
//        String publicText = (boolean) pengaduanData.get("is_public") ? "Ditampilkan ke Publik" : "Tidak Ditampilkan ke Publik";
//        JLabel lblPublic = new JLabel(publicText);
//        lblPublic.setFont(new Font("SansSerif", Font.ITALIC, 14));
//        gbc.gridx = 1;
//        gbc.gridwidth = 4;
//        panelForm.add(lblPublic, gbc);

        // === Row 7: Spacer
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        panelForm.add(Box.createVerticalStrut(10), gbc);

        // === Row 8: Tanggapan
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        JLabel lblTanggapan = new JLabel("Tanggapan : ");
        lblTanggapan.setFont(txtFont);
        panelForm.add(lblTanggapan, gbc);

        String tanggapanText = (tanggapanData != null && tanggapanData.get("summary") != null)
            ? tanggapanData.get("summary").toString()
            : "-";
        JLabel lblTanggapanPengaduan = new JLabel("<html><div style='width:90%'>" + tanggapanText.replace("\n", "<br>") + "</div></html>");
        lblTanggapanPengaduan.setFont(txtFont);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panelForm.add(lblTanggapanPengaduan, gbc);

        // === Row 9: Spacer sebelum komentar
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 5;
        panelForm.add(Box.createVerticalStrut(20), gbc);

        // === Row 10: Label Komentar
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        JLabel lblKomentar = new JLabel("Komentar");
        lblKomentar.setFont(new Font("SansSerif", Font.BOLD, 18));
        panelForm.add(lblKomentar, gbc);
        
        // === Row 11: Tambah komentar
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.ipady = 30;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 0.0;
        gbc.insets = new Insets(0, 0, -45, 0);

        JTextArea txtaKomentar = new JTextArea(4, 50); // ini akan dihitung kira-kira 4 baris
        txtaKomentar.setLineWrap(true);
        txtaKomentar.setWrapStyleWord(true);

        JScrollPane scrollIsi = new JScrollPane(txtaKomentar);
        scrollIsi.setPreferredSize(new Dimension(400, 180)); // ini akan dihormati
        panelForm.add(scrollIsi, gbc);
        
        JButton btnKirimKomentar = new JButton("Berikan Komentar");
        btnKirimKomentar.addActionListener(e -> {
            String komentarBaru = txtaKomentar.getText().trim();
            List<ArrayBuilder> data = new ArrayList<>();
            data.add(new ArrayBuilder("complaint_id", idPengaduan));
            data.add(new ArrayBuilder("user_id", Session.get("id")));
            data.add(new ArrayBuilder("date", th.getYMD()));
            data.add(new ArrayBuilder("comment", txtaKomentar.getText()));
            Map<String, Object> result = pc.addKomentar(data);
            MessageHelper.showMessageFromResult(result);
            loadKomentarData();
        });

        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(15, 20, 15, 0);
        gbc.ipady = 10;
        panelForm.add(btnKirimKomentar, gbc);

        
        
        // === Row 12: Panel List Komentar (scrollable)
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4; // Pastikan sama dengan kolom utama
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        
        
        komentarPanel.setLayout(new BoxLayout(komentarPanel, BoxLayout.Y_AXIS));
        komentarPanel.setBackground(Color.WHITE); // Lebih netral

        loadKomentarData();

        // Scroll komentar, biarkan layout yang mengatur tinggi
        JScrollPane komentarScroll = new JScrollPane(komentarPanel);
        komentarScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        komentarScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        gbc.insets = new Insets(totalHeight, 0, 0, 0);
        gbc.ipady = totalHeight;
        panelForm.add(komentarScroll, gbc);

        // === Row 13: Spacer
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        panelForm.add(Box.createVerticalStrut(20), gbc);
        
        // === Row 14: Button Kembali di kanan bawah
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        RoundedButton btnKembali = new RoundedButton("Kembali");
        btnKembali.setBackground(ColorHelper.SECONDARY);
        btnKembali.setForeground(ColorHelper.getContrastColor(ColorHelper.SECONDARY));
        btnKembali.addActionListener(e -> {
            DashboardUserContent duccon = new DashboardUserContent(desktopPane);
            desktopPane.removeAll();
            desktopPane.repaint();
            desktopPane.add(duccon);
            try { 
                duccon.setMaximum(true);
            } catch (PropertyVetoException ex) {
                Logger.getLogger(KomenPengaduanForm.class.getName()).log(Level.SEVERE, null, ex);
            }
            duccon.setVisible(true);
        });
        btnPanel.add(btnKembali);

        btnPanel.setBackground(Color.WHITE);
        gbc.gridy++;
        gbc.gridx = 3;  // kolom terakhir
        panelForm.add(btnPanel, gbc);
        
        // Spacer
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        panelForm.add(new JLabel("<html><div style='height: 500px'></div></html>"), gbc);

        // === Tambahkan ke frame ===
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setPreferredSize(new Dimension(800, 1600));
        wrapperPanel.setBackground(Color.decode("#FFFFFF")); 
        wrapperPanel.add(panelForm, BorderLayout.CENTER);
        wrapperPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, -30, -10));
//        wrapperPanel.setBorder(BorderFactory.createCompoundBorder(
//            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
//            BorderFactory.createEmptyBorder(20, 20, 20, 20)
//        ));
        
        JScrollPane scrollPane = new JScrollPane(wrapperPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setPreferredSize(new Dimension(800, 9600));

        // Tambahkan scrollPane ke frame
        add(scrollPane, BorderLayout.CENTER);

        UIHelper.syncInternalFrameWithDesktop(this, desktopPane, wrapperPanel, panelForm);
    }
    
    public void loadKomentarData() {
        komentarList = new PengaduanController().getKomenPengaduanById(idPengaduan);
        komentarPanel.removeAll();          
        komentarPanel.revalidate(); 
        if (komentarList != null && !komentarList.isEmpty()) {
            for (Map<String, Object> komen : komentarList) {
                JPanel komenCard = new JPanel(new BorderLayout());
                komenCard.setBackground(new Color(245, 245, 245));
                komenCard.setBorder(BorderFactory.createEmptyBorder(1, 10, 10, 10));

                // Header: tanggal & pembuat
                String tanggalKomen = th.humanizeDate(th.parseDate(komen.get("date").toString()));
                String pembuat = komen.get("author") != null ? komen.get("author").toString() : "-";
                JLabel lblHeader = new JLabel(tanggalKomen + " - " + pembuat);
                lblHeader.setFont(new Font("SansSerif", Font.PLAIN, 12));
                lblHeader.setForeground(new Color(120, 120, 120));

                // Isi komentar
                String isiKomentar = komen.get("comment") != null ? komen.get("comment").toString() : "";
                JLabel lblIsiKomentar = new JLabel("<html>" + isiKomentar.replace("\n", "<br>") + "</html>");
                lblIsiKomentar.setFont(new Font("SansSerif", Font.PLAIN, 13));

                komenCard.add(lblHeader, BorderLayout.NORTH);
                komenCard.add(lblIsiKomentar, BorderLayout.CENTER);

                komentarPanel.add(komenCard);
                komentarPanel.add(Box.createVerticalStrut(8));
            }
        } else {
            JLabel lblKosong = new JLabel("Belum ada komentar.");
            lblKosong.setFont(new Font("SansSerif", Font.ITALIC, 15));
            lblKosong.setForeground(Color.GRAY);
            komentarPanel.add(lblKosong);
        }
    }
}
