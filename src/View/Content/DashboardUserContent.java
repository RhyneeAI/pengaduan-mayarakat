package View.Content;

import Controller.PengaduanController;
import Lib.ArrayBuilder;
import com.toedter.calendar.JDateChooser;
import Helper.TimeHelper;
import View.Content.Pengaduan.KomenPengaduanForm;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DashboardUserContent extends JInternalFrame {
    private final PengaduanController pc = new PengaduanController();
    private JPanel gridPanel;
    private JScrollPane scrollPane;
    private JDateChooser dateFrom, dateTo;
    private JButton btnPrev, btnNext;
    private int currentPage = 1;
    private int pageSize = 9;
    private List<Map<String, Object>> pengaduanList = null;
    private JDesktopPane desktopPane;

    public DashboardUserContent(JDesktopPane desktopPane) {
        super("", false, false, false, false);
        this.desktopPane = desktopPane;
        setBorder(null);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Filter Panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        filterPanel.add(new JLabel("Tanggal :"));

        dateFrom = new JDateChooser();
        dateFrom.setDate(TimeHelper.getFirstDayOfMonth());
        dateFrom.setPreferredSize(new Dimension(150, 25)); // Lebar 150px, tinggi 25px
        dateFrom.getDateEditor().setEnabled(false);

        dateTo = new JDateChooser();
        dateTo.setDate(TimeHelper.getDateNow());
        dateTo.setPreferredSize(new Dimension(150, 25));
        dateTo.getDateEditor().setEnabled(false);

        filterPanel.add(dateFrom);
        filterPanel.add(new JLabel(" s/d "));
        filterPanel.add(dateTo);

        JButton btnFilter = new JButton("Filter");
        filterPanel.add(btnFilter);

        add(filterPanel, BorderLayout.NORTH);

        // Grid Panel 3x3
        gridPanel = new JPanel(new GridLayout(3, 3, 16, 16));
        gridPanel.setBorder(new EmptyBorder(16, 16, 16, 16));
        gridPanel.setBackground(Color.WHITE);

        scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Paging Panel
        JPanel pagingPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPrev = new JButton("Sebelumnya");
        btnPrev.setPreferredSize(new Dimension(150, 25));
        btnNext = new JButton("Selanjutnya");
        btnNext.setPreferredSize(new Dimension(150, 25));
        pagingPanel.add(btnPrev);
        pagingPanel.add(btnNext);
        add(pagingPanel, BorderLayout.SOUTH);

        // Load data awal
        loadPengaduan();

        // Filter action
        btnFilter.addActionListener((ActionEvent e) -> {
            Date startDate = dateFrom.getDate();
            Date endDate = dateTo.getDate();

            if (startDate != null && endDate != null) {
                if (startDate.after(endDate)) {
                    JOptionPane.showMessageDialog(null, "Tanggal mulai tidak boleh lebih dari tanggal akhir.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
            currentPage = 1;
            loadPengaduan();
        });

        btnPrev.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                showPage();
            }
        });

        btnNext.addActionListener(e -> {
            if (pengaduanList != null && currentPage * pageSize < pengaduanList.size()) {
                currentPage++;
                showPage();
            }
        });
    }

    private void loadPengaduan() {
        gridPanel.removeAll();
        ArrayBuilder[] condition = {
            new ArrayBuilder("date >=", TimeHelper.setYMD(dateFrom.getDate())),
            new ArrayBuilder("date <=", TimeHelper.setYMD(dateTo.getDate()))
        };
        pengaduanList = pc.getPengaduan(condition, new ArrayBuilder("id", "DESC"));
        currentPage = 1;
        showPage();
    }

    private void showPage() {
        gridPanel.removeAll();
        if (pengaduanList == null || pengaduanList.isEmpty()) {
            JLabel lbl = new JLabel("Tidak ada pengaduan.", SwingConstants.CENTER);
            lbl.setFont(new Font("SansSerif", Font.PLAIN, 18));
            gridPanel.add(lbl);
        } else {
            int start = (currentPage - 1) * pageSize;
            int end = Math.min(start + pageSize, pengaduanList.size());
            int displayedCount = 0;

            for (int i = start; i < end; i++) {
                Map<String, Object> pengaduan = pengaduanList.get(i);

                // Cek apakah is_public bernilai true
                if (Boolean.parseBoolean(pengaduan.get("is_public").toString())) {
                    gridPanel.add(createPengaduanCard(pengaduan));
                    displayedCount++;
                }
            }

            // Jika kurang dari 9, tambahkan panel kosong agar grid tetap 3x3
            for (int i = displayedCount; i < pageSize; i++) {
                gridPanel.add(new JPanel());
            }
        }
        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createPengaduanCard(Map<String, Object> data) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(8, 8));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xCCCCCC), 1, true),
                new EmptyBorder(12, 12, 12, 12)
        ));
        card.setBackground(Color.WHITE);

        // Tanggal
        TimeHelper th = new TimeHelper();
        JLabel lblTanggal = new JLabel(th.humanizeDate(th.parseDate(data.get("date").toString())));
        lblTanggal.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblTanggal.setForeground(new Color(0x888888));

        // Nama Pembuat
        JLabel lblName = new JLabel("Oleh: " + data.get("name").toString());
        lblName.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblName.setForeground(new Color(0x555555));

        // Title
        JLabel lblTitle = new JLabel(data.get("title").toString());
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));

        // Body (potong jika terlalu panjang)
        String body = data.get("body").toString();
        String shortBody = body.length() > 80 ? body.substring(0, 80) + "..." : body;
        JTextArea txtBody = new JTextArea(shortBody);
        txtBody.setLineWrap(true);
        txtBody.setWrapStyleWord(true);
        txtBody.setEditable(false);
        txtBody.setFont(new Font("SansSerif", Font.PLAIN, 13));
        txtBody.setBackground(Color.WHITE);
        txtBody.setBorder(null);

        // Tombol lihat detail
        JButton btnDetail = new JButton("Lihat Detail");
        btnDetail.addActionListener(e -> {
            KomenPengaduanForm komenForm = new KomenPengaduanForm(desktopPane, data.get("id").toString());
            desktopPane.removeAll();
            desktopPane.repaint();
            desktopPane.add(komenForm);
            komenForm.setVisible(true);
        });

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(lblTanggal, BorderLayout.WEST);
        // topPanel.add(lblName, BorderLayout.SOUTH); 

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.add(lblTitle, BorderLayout.NORTH);
        centerPanel.add(txtBody, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.add(btnDetail);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        return card;
    }

}