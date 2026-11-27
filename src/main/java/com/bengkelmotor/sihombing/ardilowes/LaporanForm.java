package com.bengkelmotor.sihombing.ardilowes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
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
        setSize(480, 310);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelUtama = new JPanel(new BorderLayout());
        panelUtama.setBackground(new Color(242, 245, 249));

        // HEADER NAVY MODERN
        JPanel panelHeader = new JPanel(new GridLayout(2, 1, 0, 4));
        panelHeader.setBackground(new Color(30, 58, 95));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel lblJudul = new JLabel("CETAK LAPORAN APLIKASI", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 19));
        lblJudul.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Laporan Pelanggan, Layanan & Total Biaya Servis", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSub.setForeground(new Color(205, 222, 240));

        panelHeader.add(lblJudul);
        panelHeader.add(lblSub);

        // KARTU TOMBOL PUTIH
        JPanel panelWrapper = new JPanel(new BorderLayout());
        panelWrapper.setBackground(new Color(242, 245, 249));
        panelWrapper.setBorder(BorderFactory.createEmptyBorder(20, 30, 25, 30));

        JPanel cardTombol = new JPanel(new GridLayout(2, 1, 0, 14));
        cardTombol.setBackground(Color.WHITE);
        cardTombol.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(215, 223, 232), 1),
            BorderFactory.createEmptyBorder(22, 25, 22, 25)
        ));

        JButton btnPreview = new JButton("PREVIEW & CETAK LAPORAN");
        btnPreview.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnPreview.setBackground(new Color(30, 95, 168));
        btnPreview.setForeground(Color.WHITE);
        btnPreview.setFocusPainted(false);
        btnPreview.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnPreview.setPreferredSize(new Dimension(320, 42));
        btnPreview.addActionListener(e -> prosesLaporan(false));

        JButton btnExportPdf = new JButton("SIMPAN LAPORAN KE PDF");
        btnExportPdf.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnExportPdf.setBackground(new Color(40, 167, 69));
        btnExportPdf.setForeground(Color.WHITE);
        btnExportPdf.setFocusPainted(false);
        btnExportPdf.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnExportPdf.setPreferredSize(new Dimension(320, 42));
        btnExportPdf.addActionListener(e -> prosesLaporan(true));

        cardTombol.add(btnPreview);
        cardTombol.add(btnExportPdf);

        panelWrapper.add(cardTombol, BorderLayout.CENTER);

        panelUtama.add(panelHeader, BorderLayout.NORTH);
        panelUtama.add(panelWrapper, BorderLayout.CENTER);

        add(panelUtama);
    }

    private void prosesLaporan(boolean exportPdf) {
        File fileJrxml = new File(REPORT_PATH);
        if (!fileJrxml.exists()) {
            JOptionPane.showMessageDialog(
                this,
                "File laporan tidak ditemukan di:\n" + fileJrxml.getAbsolutePath(),
                "File Tidak Ditemukan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

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
                    "PDF berhasil disimpan di:\n" + new File(outputPdf).getAbsolutePath(),
                    "Berhasil Export PDF",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JasperViewer.viewReport(jasperPrint, false);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this,
                "Gagal memproses laporan: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
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