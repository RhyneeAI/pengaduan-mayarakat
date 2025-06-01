package View.Content.Pengaduan;
import Controller.PengaduanController;
import Helper.TimeHelper;
import Helper.UIHelper;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class TambahPengaduanForm extends JInternalFrame {

    public TambahPengaduanForm(JDesktopPane desktopPane) {
        super("", false, false, false, false);
        setBorder(null);
        setLayout(new BorderLayout());
        
        int width = desktopPane.getWidth();
        int height = desktopPane.getWidth();
        setBounds(0, 0, width, height);

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelForm.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 2, 10, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // === Row 0: Title (4 kolom)
        JLabel titleLabel = new JLabel("Buat Pengaduan");
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

        JTextField tJudul = new JTextField();
        gbc.gridx = 1;
        gbc.gridwidth = 4;
        panelForm.add(tJudul, gbc);

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
        dcTanggal.setDate(TimeHelper.getDateNow());
        gbc.gridx = 1;
        panelForm.add(dcTanggal, gbc);
        
        // Spacer
        gbc.gridx = 2;
        panelForm.add(new JLabel(""), gbc);

        JLabel kategori = new JLabel("Kategori");
        kategori.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 3;
        panelForm.add(kategori, gbc);

        PengaduanController pc = new PengaduanController();
        String[] kategoriOptions = pc.getKategoriOptions();
        JComboBox<String> cbKategori = new JComboBox<>(kategoriOptions);
        gbc.gridx = 4;
        panelForm.add(cbKategori, gbc);

        // === Row 4: Isi
        JLabel isi = new JLabel("Isi");
        isi.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridy++;
        gbc.gridx = 0;
        panelForm.add(isi, gbc);

        JTextArea tIsi = new JTextArea(4, 20);
        JScrollPane scrollIsi = new JScrollPane(tIsi);
        gbc.gridx = 1;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        panelForm.add(scrollIsi, gbc);

        // === Row 5: Spacer
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panelForm.add(Box.createVerticalStrut(10), gbc);

        // === Row 6: Button di kanan bawah
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(new JButton("Simpan"));
        btnPanel.add(new JButton("Kembali"));
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
