
package com.bengkelmotor.sihombing.ardilowes;

import java.util.List;
import java.util.Map;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;

public class ServiceFrame extends JFrame {

    private JTable tabelLayanan;
    private JLabel lblAdmin;

    public ServiceFrame(String username, LayananDAO layananDAO) {

        setTitle("Bengkel Motor Sihombing");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelUtama = new JPanel(new java.awt.BorderLayout(10, 15));
        panelUtama.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        );

        // HEADER
        JPanel panelHeader = new JPanel(new java.awt.BorderLayout());

        JLabel judul = new JLabel("BENGKEL MOTOR SIHOMBING");
        judul.setFont(new java.awt.Font(
            "Arial", java.awt.Font.BOLD, 24
        ));

        lblAdmin = new JLabel("Admin: " + username);

        JPanel panelJudul = new JPanel(new java.awt.GridLayout(2, 1, 5, 5));
        panelJudul.add(judul);
        panelJudul.add(lblAdmin);

        panelHeader.add(panelJudul, java.awt.BorderLayout.WEST);

        // JUDUL TABEL
        JLabel subJudul = new JLabel("DAFTAR LAYANAN SERVIS");
        subJudul.setFont(new java.awt.Font(
            "Arial", java.awt.Font.BOLD, 16
        ));

        subJudul.setBorder(
            BorderFactory.createEmptyBorder(10, 0, 10, 0)
        );

        // KOLOM TABEL
        String[] kolom = {
            "Nomor", "Nama Layanan", "Harga", "Pilih"
        };

        DefaultTableModel model = new DefaultTableModel(kolom, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        // MENGAMBIL DATA DARI MYSQL MELALUI DAO
        List<Map<String, Object>> dataLayanan =
                layananDAO.tampilLayanan();

        NumberFormat formatHarga =
                NumberFormat.getNumberInstance(new Locale("id", "ID"));

        formatHarga.setMaximumFractionDigits(0);

        for (Map<String, Object> layanan : dataLayanan) {

            int nomor = ((Number) layanan.get("id_layanan")).intValue();

            String nama = layanan.get("nama_layanan").toString();

            Number hargaDatabase = (Number) layanan.get("harga");

            String harga = "Rp " + formatHarga.format(hargaDatabase);

            model.addRow(new Object[]{
                nomor, nama, harga, "PILIH"
            });
        }

        tabelLayanan = new JTable(model);

        tabelLayanan.setRowHeight(35);

        tabelLayanan.setFont(new java.awt.Font(
            "Arial", java.awt.Font.PLAIN, 13
        ));

        tabelLayanan.getTableHeader().setFont(
            new java.awt.Font("Arial", java.awt.Font.BOLD, 13)
        );

        tabelLayanan.getColumnModel().getColumn(0).setPreferredWidth(60);
        tabelLayanan.getColumnModel().getColumn(1).setPreferredWidth(400);
        tabelLayanan.getColumnModel().getColumn(2).setPreferredWidth(120);
        tabelLayanan.getColumnModel().getColumn(3).setPreferredWidth(100);

        // TOMBOL PILIH
        tabelLayanan.getColumnModel().getColumn(3).setCellRenderer(
            new ButtonRenderer()
        );

        tabelLayanan.getColumnModel().getColumn(3).setCellEditor(
            new ButtonEditor(tabelLayanan)
        );

        JScrollPane scrollPane = new JScrollPane(tabelLayanan);

        // PANEL TENGAH
        JPanel panelTengah = new JPanel(new java.awt.BorderLayout());

        panelTengah.add(subJudul, java.awt.BorderLayout.NORTH);
        panelTengah.add(scrollPane, java.awt.BorderLayout.CENTER);

        // FOOTER
        JLabel footer = new JLabel(
            "Sistem Informasi Bengkel Motor Sihombing"
        );

        footer.setHorizontalAlignment(SwingConstants.CENTER);

        panelUtama.add(panelHeader, java.awt.BorderLayout.NORTH);
        panelUtama.add(panelTengah, java.awt.BorderLayout.CENTER);
        panelUtama.add(footer, java.awt.BorderLayout.SOUTH);

        add(panelUtama);
    }

    // TAMPILAN TOMBOL
    class ButtonRenderer extends JButton
            implements TableCellRenderer {

        public ButtonRenderer() {
            setText("PILIH");
            setBackground(new java.awt.Color(40, 120, 200));
            setForeground(java.awt.Color.WHITE);
            setFocusPainted(false);
        }

        @Override
        public java.awt.Component getTableCellRendererComponent(
                JTable table,
                Object value,
                boolean isSelected,
                boolean hasFocus,
                int row,
                int column) {

            return this;
        }
    }

    // PROSES KLIK TOMBOL PILIH
    class ButtonEditor extends AbstractCellEditor
            implements TableCellEditor {

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

                    String nama = table.getModel()
                            .getValueAt(row, 1).toString();

                    String harga = table.getModel()
                            .getValueAt(row, 2).toString();

                    JOptionPane.showMessageDialog(
                        ServiceFrame.this,
                        "Layanan Dipilih:\n"
                        + nama + "\n"
                        + "Harga: " + harga
                    );
                }

                fireEditingStopped();
            });
        }

        @Override
        public java.awt.Component getTableCellEditorComponent(
                JTable table,
                Object value,
                boolean isSelected,
                int row,
                int column) {

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "PILIH";
        }
    }
}
