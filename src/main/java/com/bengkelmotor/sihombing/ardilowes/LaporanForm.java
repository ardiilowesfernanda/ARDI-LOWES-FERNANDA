package com.bengkelmotor.sihombing.ardilowes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class LaporanForm extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/fernanda";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    private static final String REPORT_PATH = "reports/laporan_pelanggan.jrxml";

    public LaporanForm() {
        setTitle("Cetak Laporan - Bengkel Motor Sihombing");
        setSize(460, 260);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelUtama = new JPanel(new BorderLayout(10, 15));
        panelUtama.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        panelUtama.setBackground(new Color(240, 244, 248));

        // JUDUL FORM
        JLabel lblJudul = new JLabel("CETAK LAPORAN APLIKASI", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Arial", Font.BOLD, 18));
        lblJudul.setForeground(new Color(30, 58, 95));

        JLabel lblSub = new JLabel("Laporan Data Pelanggan - Bengkel Motor Sihombing", SwingConstants.CENTER);
        lblSub.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel panelHeader = new JPanel(new GridLayout(2, 1, 5, 5));
        panelHeader.setBackground(new Color(240, 244, 248));
        panelHeader.add(lblJudul);
        panelHeader.add(lblSub);

        // TOMBOL AKSI
        JPanel panelTombol = new JPanel(new GridLayout(2, 1, 10, 12));
        panelTombol.setBackground(new Color(240, 244, 248));

        JButton btnPreview = new JButton("PREVIEW & CETAK LAPORAN");
        btnPreview.setFont(new Font("Arial", Font.BOLD, 13));
        btnPreview.setBackground(new Color(40, 120, 200));
        btnPreview.setForeground(Color.WHITE);
        btnPreview.setFocusPainted(false);
        btnPreview.addActionListener(e -> prosesLaporan(false));

        JButton btnExportPdf = new JButton("SIMPAN LAPORAN KE PDF");
        btnExportPdf.setFont(new Font("Arial", Font.BOLD, 13));
        btnExportPdf.setBackground(new Color(40, 167, 69));
        btnExportPdf.setForeground(Color.WHITE);
        btnExportPdf.setFocusPainted(false);
        btnExportPdf.addActionListener(e -> prosesLaporan(true));

        panelTombol.add(btnPreview);
        panelTombol.add(btnExportPdf);

        panelUtama.add(panelHeader, BorderLayout.NORTH);
        panelUtama.add(panelTombol, BorderLayout.CENTER);

        add(panelUtama);
    }

    private void prosesLaporan(boolean exportPdf) {
        File fileJrxml = new File(REPORT_PATH);
        if (!fileJrxml.exists()) {
            JOptionPane.showMessageDialog(
                this,
                "File laporan tidak ditemukan di:\n" + fileJrxml.getAbsolutePath()
            );
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            // COMPILE JRXML & ISI DATA DARI DATABASE FERNANDA
            JasperReport jasperReport = JasperCompileManager.compileReport(REPORT_PATH);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                new HashMap<>(),
                conn
            );

            if (exportPdf) {
                String outputPdf = "reports/Laporan_Pelanggan.pdf";
                JasperExportManager.exportReportToPdfFile(jasperPrint, outputPdf);
                JOptionPane.showMessageDialog(
                    this,
                    "PDF berhasil disimpan di:\n" + new File(outputPdf).getAbsolutePath()
                );
            } else {
                // false agar saat JasperViewer ditutup, aplikasi utama tidak ikut tertutup
                JasperViewer.viewReport(jasperPrint, false);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Gagal memproses laporan: " + ex.getMessage()
            );
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LaporanForm().setVisible(true);
        });
    }
}