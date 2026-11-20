package com.bengkelmotor.sihombing.ardilowes;

import java.util.Map;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;

public class ServiceFrame extends JFrame {

    private JTable tabelLayanan;
    private JTable tabelPelanggan;
    private DefaultTableModel modelPelanggan;
    private JLabel lblAdmin;

    // [PROGRESS 3.4] KOMPONEN FORM PELANGGAN
    private JTextField txtNama;
    private JTextField txtNoHp;
    private JTextField txtNoPolisi;
    private JTextField txtJenisMotor;
    private JButton btnSimpanPelanggan;

    private final LayananDAO layananDAO;
    private final PelangganDAO pelangganDAO;

    public ServiceFrame(String username, LayananDAO layananDAO, PelangganDAO pelangganDAO) {

        this.layananDAO = layananDAO;
        this.pelangganDAO = pelangganDAO;

        setTitle("Bengkel Motor Sihombing");
        setSize(950, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelUtama = new JPanel(new java.awt.BorderLayout(10, 15));
        panelUtama.setBorder(
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        );

        // ================= HEADER =================
        JPanel panelHeader = new JPanel(new java.awt.BorderLayout());

        JLabel judul = new JLabel("BENGKEL MOTOR SIHOMBING");
        judul.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));

        lblAdmin = new JLabel("Admin: " + username);

        JPanel panelJudul = new JPanel(new java.awt.GridLayout(2, 1, 5, 5));
        panelJudul.add(judul);
        panelJudul.add(lblAdmin);

        panelHeader.add(panelJudul, java.awt.BorderLayout.WEST);

        // ================= BAGIAN TENGAH =================
        JPanel panelKonten = new JPanel();
        panelKonten.setLayout(new BoxLayout(panelKonten, BoxLayout.Y_AXIS));

        // --- [PROGRESS 3.4] FORM INPUT DATA PELANGGAN ---
        JPanel panelFormPelanggan = new JPanel(new java.awt.BorderLayout(10, 10));
        panelFormPelanggan.setBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "FORM INPUT DATA PELANGGAN",
                0,
                0,
                new java.awt.Font("Arial", java.awt.Font.BOLD, 14)
            )
        );

        JPanel gridInput = new JPanel(new java.awt.GridLayout(2, 4, 10, 10));
        gridInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNama = new JTextField();
        txtNoHp = new JTextField();
        txtNoPolisi = new JTextField();
        txtJenisMotor = new JTextField();

        gridInput.add(new JLabel("Nama Pelanggan:"));
        gridInput.add(txtNama);
        gridInput.add(new JLabel("Nomor Polisi:"));
        gridInput.add(txtNoPolisi);

        gridInput.add(new JLabel("Nomor HP:"));
        gridInput.add(txtNoHp);
        gridInput.add(new JLabel("Jenis Motor:"));
        gridInput.add(txtJenisMotor);

        JPanel panelTombolSimpan = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        btnSimpanPelanggan = new JButton("SIMPAN PELANGGAN");
        btnSimpanPelanggan.setBackground(new java.awt.Color(40, 167, 69));
        btnSimpanPelanggan.setForeground(java.awt.Color.WHITE);
        btnSimpanPelanggan.setFocusPainted(false);
        btnSimpanPelanggan.addActionListener(e -> aksiSimpanPelanggan());
        panelTombolSimpan.add(btnSimpanPelanggan);

        panelFormPelanggan.add(gridInput, java.awt.BorderLayout.CENTER);
        panelFormPelanggan.add(panelTombolSimpan, java.awt.BorderLayout.SOUTH);

        // --- [PROGRESS 3.5] TABEL DATA PELANGGAN ---
        JPanel panelTabelPelanggan = new JPanel(new java.awt.BorderLayout(5, 5));
        panelTabelPelanggan.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel subJudulPelanggan = new JLabel("DAFTAR PELANGGAN TERDAFTAR");
        subJudulPelanggan.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 15));

        String[] kolomPelanggan = {
            "ID", "Nama Pelanggan", "Nomor HP", "Nomor Polisi", "Jenis Motor"
        };

        modelPelanggan = new DefaultTableModel(kolomPelanggan, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelPelanggan = new JTable(modelPelanggan);
        tabelPelanggan.setRowHeight(28);
        tabelPelanggan.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
        tabelPelanggan.getTableHeader().setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));

        JScrollPane scrollPelanggan = new JScrollPane(tabelPelanggan);
        scrollPelanggan.setPreferredSize(new java.awt.Dimension(850, 150));

        panelTabelPelanggan.add(subJudulPelanggan, java.awt.BorderLayout.NORTH);
        panelTabelPelanggan.add(scrollPelanggan, java.awt.BorderLayout.CENTER);

        // --- [PROGRESS 3.5] TABEL DAFTAR 6 LAYANAN SERVIS ---
        JPanel panelTabelLayanan = new JPanel(new java.awt.BorderLayout(5, 5));

        JLabel subJudulLayanan = new JLabel("DAFTAR LAYANAN SERVIS");
        subJudulLayanan.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 15));

        String[] kolomLayanan = {
            "Nomor", "Nama Layanan", "Harga", "Pilih"
        };

        DefaultTableModel modelLayanan = new DefaultTableModel(kolomLayanan, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        // MENGAMBIL DATA 6 LAYANAN DARI MYSQL MELALUI DAO
        var dataLayanan = layananDAO.tampilLayanan();

        NumberFormat formatHarga = NumberFormat.getNumberInstance(new Locale("id", "ID"));
        formatHarga.setMaximumFractionDigits(0);

        for (Map<String, Object> layanan : dataLayanan) {
            int nomor = ((Number) layanan.get("id_layanan")).intValue();
            String nama = layanan.get("nama_layanan").toString();
            Number hargaDatabase = (Number) layanan.get("harga");
            String harga = "Rp " + formatHarga.format(hargaDatabase);

            modelLayanan.addRow(new Object[]{
                nomor, nama, harga, "PILIH"
            });
        }

        tabelLayanan = new JTable(modelLayanan);
        tabelLayanan.setRowHeight(32);
        tabelLayanan.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 13));
        tabelLayanan.getTableHeader().setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 13));

        tabelLayanan.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabelLayanan.getColumnModel().getColumn(1).setPreferredWidth(400);
        tabelLayanan.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabelLayanan.getColumnModel().getColumn(3).setPreferredWidth(100);

        tabelLayanan.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
        tabelLayanan.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(tabelLayanan));

        JScrollPane scrollLayanan = new JScrollPane(tabelLayanan);
        scrollLayanan.setPreferredSize(new java.awt.Dimension(850, 230));

        panelTabelLayanan.add(subJudulLayanan, java.awt.BorderLayout.NORTH);
        panelTabelLayanan.add(scrollLayanan, java.awt.BorderLayout.CENTER);

        // GABUNGKAN KE PANEL KONTEN
        panelKonten.add(panelFormPelanggan);
        panelKonten.add(panelTabelPelanggan);
        panelKonten.add(panelTabelLayanan);

        // ================= FOOTER =================
        JLabel footer = new JLabel("Sistem Informasi Bengkel Motor Sihombing");
        footer.setHorizontalAlignment(SwingConstants.CENTER);

        panelUtama.add(panelHeader, java.awt.BorderLayout.NORTH);
        panelUtama.add(panelKonten, java.awt.BorderLayout.CENTER);
        panelUtama.add(footer, java.awt.BorderLayout.SOUTH);

        add(panelUtama);

        // MUAT DATA PELANGGAN SAAT PERTAMA KALI DIBUKA
        muatDataPelanggan();
    }

    // [PROGRESS 3.4] FUNGSI MENYIMPAN PELANGGAN KE MYSQL
    private void aksiSimpanPelanggan() {

        String nama = txtNama.getText().trim();
        String noHp = txtNoHp.getText().trim();
        String noPolisi = txtNoPolisi.getText().trim();
        String jenisMotor = txtJenisMotor.getText().trim();

        if (nama.isEmpty() || noHp.isEmpty() || noPolisi.isEmpty() || jenisMotor.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Semua kolom data pelanggan wajib diisi!"
            );
            return;
        }

        try {
            pelangganDAO.tambahPelanggan(nama, noHp, noPolisi, jenisMotor);

            JOptionPane.showMessageDialog(
                this,
                "Data pelanggan berhasil disimpan!"
            );

            // KOSONGKAN FORM SETELAH SIMPAN
            txtNama.setText("");
            txtNoHp.setText("");
            txtNoPolisi.setText("");
            txtJenisMotor.setText("");

            // REFRESH TABEL PELANGGAN OTOMATIS
            muatDataPelanggan();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Gagal menyimpan pelanggan: " + ex.getMessage()
            );
        }
    }

    // [PROGRESS 3.5] FUNGSI MENAMPILKAN DATA PELANGGAN DARI MYSQL
    private void muatDataPelanggan() {
        modelPelanggan.setRowCount(0);

        var daftarPelanggan = pelangganDAO.tampilPelanggan();

        for (Map<String, Object> p : daftarPelanggan) {
            Object id = p.get("id_pelanggan");
            Object nama = p.get("nama_pelanggan");
            Object hp = p.get("no_hp");
            Object nopol = p.get("no_polisi");
            Object motor = p.get("jenis_motor");

            modelPelanggan.addRow(new Object[]{id, nama, hp, nopol, motor});
        }
    }

    // TAMPILAN TOMBOL PILIH LAYANAN
    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setText("PILIH");
            setBackground(new java.awt.Color(40, 120, 200));
            setForeground(java.awt.Color.WHITE);
            setFocusPainted(false);
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // PROSES KLIK TOMBOL PILIH LAYANAN
    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {

        private final JButton button;
        private final JTable table;

        public ButtonEditor(JTable table) {
            this.table = table;

            button = new JButton("PILIH");
            button.setBackground(new java.awt.Color(40, 120, 200));
            button.setForeground(java.awt.Color.WHITE);
            button.setFocusPainted(false);

            button.addActionListener(e -> {
                int viewRow = table.getEditingRow();
                if (viewRow >= 0) {
                    int row = table.convertRowIndexToModel(viewRow);
                    String nama = table.getModel().getValueAt(row, 1).toString();
                    String harga = table.getModel().getValueAt(row, 2).toString();

                    JOptionPane.showMessageDialog(
                        ServiceFrame.this,
                        "Layanan Dipilih:\n" + nama + "\nHarga: " + harga
                    );
                }
                fireEditingStopped();
            });
        }

        @Override
        public java.awt.Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "PILIH";
        }
    }
}