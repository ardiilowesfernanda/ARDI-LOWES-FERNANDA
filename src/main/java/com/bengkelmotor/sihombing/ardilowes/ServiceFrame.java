package com.bengkelmotor.sihombing.ardilowes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class ServiceFrame extends JFrame {

    private JTable tabelLayanan;
    private DefaultTableModel modelLayanan;
    private JTable tabelPelanggan;
    private DefaultTableModel modelPelanggan;

    // KOMPONEN INPUT PELANGGAN
    private JTextField txtTanggal;
    private JTextField txtNama;
    private JTextField txtNoHp;
    private JTextField txtNoPolisi;
    private JTextField txtJenisMotor;

    // KOMPONEN RINGKASAN BIAYA
    private JLabel lblRincianLayanan;
    private JLabel lblTotalBiaya;

    private final LayananDAO layananDAO;
    private final PelangganDAO pelangganDAO;

    // MENYIMPAN HARGA ASLI (ANGKA) DARI DATABASE UNTUK PERHITUNGAN
    private final List<Integer> daftarHargaLayanan = new ArrayList<>();
    private final NumberFormat formatRupiah = NumberFormat.getNumberInstance(new Locale("id", "ID"));
    private final DateTimeFormatter formatWaktu = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    private int totalBiayaSekarang = 0;
    private String layananDipilihSekarang = "";

    public ServiceFrame(String username, LayananDAO layananDAO, PelangganDAO pelangganDAO) {

        this.layananDAO = layananDAO;
        this.pelangganDAO = pelangganDAO;
        formatRupiah.setMaximumFractionDigits(0);

        setTitle("Sistem Informasi - Bengkel Motor Sihombing");
        setSize(1120, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color warnaNavy = new Color(30, 58, 95);
        Color warnaBg = new Color(242, 245, 249);

        JPanel panelUtama = new JPanel(new BorderLayout());
        panelUtama.setBackground(warnaBg);

        // ================= HEADER MODERN =================
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(warnaNavy);
        panelHeader.setBorder(BorderFactory.createEmptyBorder(14, 24, 14, 24));

        JPanel panelJudul = new JPanel(new GridLayout(2, 1, 0, 3));
        panelJudul.setBackground(warnaNavy);

        JLabel judul = new JLabel("BENGKEL MOTOR SIHOMBING");
        judul.setFont(new Font("Segoe UI", Font.BOLD, 21));
        judul.setForeground(Color.WHITE);

        JLabel lblAdmin = new JLabel("Admin Aktif: " + username + "  |  Manajemen Pelanggan & Layanan Servis");
        lblAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblAdmin.setForeground(new Color(205, 222, 240));

        panelJudul.add(judul);
        panelJudul.add(lblAdmin);

        JButton btnBukaLaporan = new JButton("CETAK LAPORAN PDF");
        btnBukaLaporan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBukaLaporan.setBackground(new Color(0, 132, 255));
        btnBukaLaporan.setForeground(Color.WHITE);
        btnBukaLaporan.setFocusPainted(false);
        btnBukaLaporan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBukaLaporan.setPreferredSize(new Dimension(180, 36));
        btnBukaLaporan.addActionListener(e -> new LaporanForm().setVisible(true));

        JPanel panelTombolHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        panelTombolHeader.setBackground(warnaNavy);
        panelTombolHeader.add(btnBukaLaporan);

        panelHeader.add(panelJudul, BorderLayout.WEST);
        panelHeader.add(panelTombolHeader, BorderLayout.EAST);

        // ================= KONTEN TENGAH (BISA DI-SCROLL) =================
        JPanel panelKonten = new JPanel(new BorderLayout(0, 14));
        panelKonten.setBackground(warnaBg);
        panelKonten.setBorder(BorderFactory.createEmptyBorder(14, 22, 14, 22));

        // --- BAGIAN ATAS: FORM PELANGGAN (KIRI) + PILIH LAYANAN (KANAN) ---
        JPanel panelAtas = new JPanel(new GridLayout(1, 2, 16, 0));
        panelAtas.setBackground(warnaBg);
        panelAtas.setPreferredSize(new Dimension(1040, 295));

        // 1. KARTU FORM INPUT DATA PELANGGAN
        JPanel cardForm = buatCardPanel("1. FORM INPUT DATA PELANGGAN");

        JPanel gridInput = new JPanel(new GridLayout(5, 2, 8, 8));
        gridInput.setBackground(Color.WHITE);
        gridInput.setBorder(BorderFactory.createEmptyBorder(6, 4, 8, 4));

        txtTanggal = buatTextField();
        txtTanggal.setEditable(false);
        txtTanggal.setBackground(new Color(236, 240, 245));
        txtTanggal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        txtTanggal.setText(LocalDateTime.now().format(formatWaktu));

        txtNama = buatTextField();
        txtNoHp = buatTextField();
        txtNoPolisi = buatTextField();
        txtJenisMotor = buatTextField();

        gridInput.add(buatLabelForm("Tanggal & Jam (Otomatis):"));
        gridInput.add(txtTanggal);
        gridInput.add(buatLabelForm("Nama Pelanggan:"));
        gridInput.add(txtNama);
        gridInput.add(buatLabelForm("Nomor HP:"));
        gridInput.add(txtNoHp);
        gridInput.add(buatLabelForm("Nomor Polisi:"));
        gridInput.add(txtNoPolisi);
        gridInput.add(buatLabelForm("Jenis Motor:"));
        gridInput.add(txtJenisMotor);

        // RINGKASAN TOTAL BIAYA & TOMBOL SIMPAN DATA
        JPanel panelBawahForm = new JPanel(new BorderLayout(8, 6));
        panelBawahForm.setBackground(new Color(246, 249, 252));
        panelBawahForm.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(215, 225, 235)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        lblRincianLayanan = new JLabel("Layanan Dipilih: (Belum ada layanan dicentang)");
        lblRincianLayanan.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblRincianLayanan.setForeground(new Color(80, 95, 115));

        lblTotalBiaya = new JLabel("TOTAL BIAYA: Rp 0");
        lblTotalBiaya.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTotalBiaya.setForeground(new Color(30, 58, 95));

        JPanel panelInfoBiaya = new JPanel(new GridLayout(2, 1, 0, 2));
        panelInfoBiaya.setBackground(new Color(246, 249, 252));
        panelInfoBiaya.add(lblRincianLayanan);
        panelInfoBiaya.add(lblTotalBiaya);

        JButton btnSimpan = new JButton("SIMPAN DATA");
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSimpan.setBackground(new Color(40, 167, 69));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setFocusPainted(false);
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSimpan.setPreferredSize(new Dimension(130, 34));
        btnSimpan.addActionListener(e -> aksiSimpanPelanggan());

        panelBawahForm.add(panelInfoBiaya, BorderLayout.CENTER);
        panelBawahForm.add(btnSimpan, BorderLayout.EAST);

        cardForm.add(gridInput, BorderLayout.CENTER);
        cardForm.add(panelBawahForm, BorderLayout.SOUTH);

        // 2. KARTU DAFTAR LAYANAN SERVIS (CHECKBOX)
        JPanel cardLayanan = buatCardPanel("2. DAFTAR LAYANAN SERVIS (CENTANG YANG DIPILIH)");

        String[] kolomLayanan = {"Pilih", "No", "Nama Layanan", "Harga"};
        modelLayanan = new DefaultTableModel(kolomLayanan, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };

        var dataLayanan = layananDAO.tampilLayanan();
        for (Map<String, Object> layanan : dataLayanan) {
            int nomor = ((Number) layanan.get("id_layanan")).intValue();
            String nama = layanan.get("nama_layanan").toString();
            int hargaAngka = ((Number) layanan.get("harga")).intValue();
            daftarHargaLayanan.add(hargaAngka);

            String hargaTeks = "Rp " + formatRupiah.format(hargaAngka);
            modelLayanan.addRow(new Object[]{false, nomor, nama, hargaTeks});
        }

        modelLayanan.addTableModelListener(e -> hitungTotalLayanan());

        tabelLayanan = new JTable(modelLayanan);
        aturGayaTabel(tabelLayanan);
        tabelLayanan.setRowHeight(29);
        tabelLayanan.getColumnModel().getColumn(0).setPreferredWidth(48);
        tabelLayanan.getColumnModel().getColumn(0).setMaxWidth(55);
        tabelLayanan.getColumnModel().getColumn(1).setPreferredWidth(38);
        tabelLayanan.getColumnModel().getColumn(1).setMaxWidth(45);
        tabelLayanan.getColumnModel().getColumn(2).setPreferredWidth(275);
        tabelLayanan.getColumnModel().getColumn(3).setPreferredWidth(95);

        // PERATAAN CENTER/MIDDLE UNTUK TABEL 2 (NO, NAMA LAYANAN, HARGA)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tabelLayanan.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        tabelLayanan.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
        tabelLayanan.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);

        JScrollPane scrollLayanan = new JScrollPane(tabelLayanan);
        scrollLayanan.setPreferredSize(new Dimension(480, 210));
        scrollLayanan.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 234)));
        scrollLayanan.getViewport().setBackground(Color.WHITE);

        cardLayanan.add(scrollLayanan, BorderLayout.CENTER);

        panelAtas.add(cardForm);
        panelAtas.add(cardLayanan);

        // --- BAGIAN BAWAH: 3. DAFTAR PELANGGAN & RIWAYAT SERVIS ---
        JPanel cardPelanggan = buatCardPanel("3. DAFTAR PELANGGAN & RIWAYAT SERVIS");
        cardPelanggan.setPreferredSize(new Dimension(1040, 290));

        JPanel panelTombolTabel = new JPanel(new BorderLayout());
        panelTombolTabel.setBackground(Color.WHITE);
        panelTombolTabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JLabel lblPetunjukHapus = new JLabel("Klik salah satu baris pelanggan di bawah lalu tekan HAPUS DATA untuk menghapus.");
        lblPetunjukHapus.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblPetunjukHapus.setForeground(new Color(100, 112, 128));

        JButton btnHapus = new JButton("HAPUS DATA");
        btnHapus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnHapus.setBackground(new Color(220, 53, 69));
        btnHapus.setForeground(Color.WHITE);
        btnHapus.setFocusPainted(false);
        btnHapus.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHapus.setPreferredSize(new Dimension(130, 30));
        btnHapus.addActionListener(e -> aksiHapusPelanggan());

        panelTombolTabel.add(lblPetunjukHapus, BorderLayout.WEST);
        panelTombolTabel.add(btnHapus, BorderLayout.EAST);

        String[] kolomPelanggan = {
            "ID", "Tanggal & Jam", "Nama Pelanggan", "No. HP", "No. Polisi", "Jenis Motor", "Layanan Servis", "Total Biaya"
        };

        modelPelanggan = new DefaultTableModel(kolomPelanggan, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelPelanggan = new JTable(modelPelanggan);
        aturGayaTabel(tabelPelanggan);
        tabelPelanggan.getColumnModel().getColumn(0).setPreferredWidth(38);
        tabelPelanggan.getColumnModel().getColumn(0).setMaxWidth(48);
        tabelPelanggan.getColumnModel().getColumn(1).setPreferredWidth(130);
        tabelPelanggan.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabelPelanggan.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabelPelanggan.getColumnModel().getColumn(4).setPreferredWidth(85);
        tabelPelanggan.getColumnModel().getColumn(5).setPreferredWidth(95);
        tabelPelanggan.getColumnModel().getColumn(6).setPreferredWidth(260);
        tabelPelanggan.getColumnModel().getColumn(7).setPreferredWidth(100);

        // PERATAAN CENTER/MIDDLE SEMUA KOLOM TABEL 3 KECUALI LAYANAN SERVIS (INDEX 6)
        for (int i = 0; i < tabelPelanggan.getColumnCount(); i++) {
            if (i != 6) {
                tabelPelanggan.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        JScrollPane scrollPelanggan = new JScrollPane(tabelPelanggan);
        scrollPelanggan.setPreferredSize(new Dimension(1000, 200));
        scrollPelanggan.setBorder(BorderFactory.createLineBorder(new Color(220, 226, 234)));
        scrollPelanggan.getViewport().setBackground(Color.WHITE);

        JPanel isiCardPelanggan = new JPanel(new BorderLayout());
        isiCardPelanggan.setBackground(Color.WHITE);
        isiCardPelanggan.add(panelTombolTabel, BorderLayout.NORTH);
        isiCardPelanggan.add(scrollPelanggan, BorderLayout.CENTER);

        cardPelanggan.add(isiCardPelanggan, BorderLayout.CENTER);

        panelKonten.add(panelAtas, BorderLayout.NORTH);
        panelKonten.add(cardPelanggan, BorderLayout.CENTER);

        // BUNGKUS KONTEN DENGAN SCROLLPANE AGAR BISA DI-SCROLL KE BAWAH DI SEMUA UKURAN LAYAR
        JScrollPane scrollHalaman = new JScrollPane(panelKonten);
        scrollHalaman.setBorder(null);
        scrollHalaman.getVerticalScrollBar().setUnitIncrement(16);

        // ================= FOOTER =================
        JLabel footer = new JLabel("Sistem Informasi Bengkel Motor Sihombing");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footer.setForeground(new Color(110, 120, 135));
        footer.setHorizontalAlignment(SwingConstants.CENTER);
        footer.setBorder(BorderFactory.createEmptyBorder(6, 0, 8, 0));

        panelUtama.add(panelHeader, BorderLayout.NORTH);
        panelUtama.add(scrollHalaman, BorderLayout.CENTER);
        panelUtama.add(footer, BorderLayout.SOUTH);

        add(panelUtama);

        // TIMER JAM OTOMATIS SETIAP DETIK
        Timer timerJam = new Timer(1000, e -> txtTanggal.setText(LocalDateTime.now().format(formatWaktu)));
        timerJam.start();

        // MUAT DATA PELANGGAN SAAT PERTAMA KALI DIBUKA
        muatDataPelanggan();
    }

    private JPanel buatCardPanel(String judulCard) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(215, 223, 232), 1),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));

        JLabel lblJudul = new JLabel(judulCard);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblJudul.setForeground(new Color(30, 58, 95));
        lblJudul.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 235, 240)),
            BorderFactory.createEmptyBorder(0, 0, 5, 0)
        ));

        card.add(lblJudul, BorderLayout.NORTH);
        return card;
    }

    private JLabel buatLabelForm(String teks) {
        JLabel lbl = new JLabel(teks);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(55, 70, 90));
        return lbl;
    }

    private JTextField buatTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(195, 205, 218)),
            BorderFactory.createEmptyBorder(3, 8, 3, 8)
        ));
        return tf;
    }

    private void aturGayaTabel(JTable table) {
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setGridColor(new Color(232, 236, 242));
        table.setSelectionBackground(new Color(218, 234, 252));
        table.setSelectionForeground(new Color(20, 40, 70));
        table.setShowVerticalLines(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(43, 91, 132));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 30));
    }

    private void hitungTotalLayanan() {
        List<String> terpilih = new ArrayList<>();
        int total = 0;

        for (int i = 0; i < modelLayanan.getRowCount(); i++) {
            Boolean diceklis = (Boolean) modelLayanan.getValueAt(i, 0);
            if (Boolean.TRUE.equals(diceklis)) {
                String namaLayanan = modelLayanan.getValueAt(i, 2).toString();
                terpilih.add(namaLayanan);
                total += daftarHargaLayanan.get(i);
            }
        }

        totalBiayaSekarang = total;
        layananDipilihSekarang = String.join(", ", terpilih);

        if (terpilih.isEmpty()) {
            lblRincianLayanan.setText("Layanan Dipilih: (Belum ada layanan dicentang)");
        } else {
            lblRincianLayanan.setText("Layanan Dipilih (" + terpilih.size() + "): " + layananDipilihSekarang);
        }

        lblTotalBiaya.setText("TOTAL BIAYA: Rp " + formatRupiah.format(totalBiayaSekarang));
    }

    private void aksiSimpanPelanggan() {

        String tanggal = LocalDateTime.now().format(formatWaktu);
        String nama = txtNama.getText().trim();
        String noHp = txtNoHp.getText().trim();
        String noPolisi = txtNoPolisi.getText().trim();
        String jenisMotor = txtJenisMotor.getText().trim();

        if (nama.isEmpty() || noHp.isEmpty() || noPolisi.isEmpty() || jenisMotor.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Semua kolom data pelanggan wajib diisi!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (layananDipilihSekarang.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Silakan centang minimal 1 layanan servis pada tabel sebelah kanan!",
                "Layanan Belum Dipilih",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            pelangganDAO.tambahPelanggan(
                tanggal, nama, noHp, noPolisi, jenisMotor,
                layananDipilihSekarang, totalBiayaSekarang
            );

            JOptionPane.showMessageDialog(
                this,
                "Data pelanggan & layanan servis berhasil disimpan!\n"
                + "Total Biaya: Rp " + formatRupiah.format(totalBiayaSekarang),
                "Berhasil",
                JOptionPane.INFORMATION_MESSAGE
            );

            txtNama.setText("");
            txtNoHp.setText("");
            txtNoPolisi.setText("");
            txtJenisMotor.setText("");

            for (int i = 0; i < modelLayanan.getRowCount(); i++) {
                modelLayanan.setValueAt(false, i, 0);
            }
            hitungTotalLayanan();

            muatDataPelanggan();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Gagal menyimpan data: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void aksiHapusPelanggan() {
        int barisDipilih = tabelPelanggan.getSelectedRow();
        if (barisDipilih == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Pilih terlebih dahulu baris data pelanggan pada tabel yang ingin dihapus!",
                "Belum Memilih Data",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int idPelanggan = ((Number) modelPelanggan.getValueAt(barisDipilih, 0)).intValue();
        String namaPelanggan = modelPelanggan.getValueAt(barisDipilih, 2).toString();

        int konfirmasi = JOptionPane.showConfirmDialog(
            this,
            "Yakin ingin menghapus data pelanggan:\nID " + idPelanggan + " - " + namaPelanggan + "?",
            "Konfirmasi Hapus Data",
            JOptionPane.YES_NO_OPTION
        );

        if (konfirmasi == JOptionPane.YES_OPTION) {
            try {
                pelangganDAO.hapusPelanggan(idPelanggan);
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                muatDataPelanggan();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Gagal menghapus data: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    private void muatDataPelanggan() {
        modelPelanggan.setRowCount(0);

        var daftarPelanggan = pelangganDAO.tampilPelanggan();

        for (Map<String, Object> p : daftarPelanggan) {
            Object id = p.get("id_pelanggan");
            Object tanggal = p.get("tanggal") != null ? p.get("tanggal") : "-";
            Object nama = p.get("nama_pelanggan");
            Object hp = p.get("no_hp");
            Object nopol = p.get("no_polisi");
            Object motor = p.get("jenis_motor");
            Object layanan = p.get("layanan_dipilih") != null ? p.get("layanan_dipilih") : "-";

            int totalAngka = p.get("total_biaya") != null ? ((Number) p.get("total_biaya")).intValue() : 0;
            String totalTeks = "Rp " + formatRupiah.format(totalAngka);

            modelPelanggan.addRow(new Object[]{
                id, tanggal, nama, hp, nopol, motor, layanan, totalTeks
            });
        }
    }
}