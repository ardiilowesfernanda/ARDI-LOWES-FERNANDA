package com.bengkelmotor.sihombing.ardilowes;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public class PelangganDAO {

    private final JdbcTemplate jdbcTemplate;

    public PelangganDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // MENAMBAH DATA PELANGGAN + TANGGAL + LAYANAN + TOTAL BIAYA
    public int tambahPelanggan(
            String tanggal,
            String nama,
            String noHp,
            String noPolisi,
            String jenisMotor,
            String layananDipilih,
            int totalBiaya) {

        String sql = "INSERT INTO pelanggan "
                + "(tanggal, nama_pelanggan, no_hp, no_polisi, jenis_motor, layanan_dipilih, total_biaya) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(
                sql, tanggal, nama, noHp, noPolisi, jenisMotor, layananDipilih, totalBiaya
        );
    }

    // MENGHAPUS DATA PELANGGAN BERDASARKAN ID
    public int hapusPelanggan(int idPelanggan) {
        String sql = "DELETE FROM pelanggan WHERE id_pelanggan = ?";
        return jdbcTemplate.update(sql, idPelanggan);
    }

    // MENAMPILKAN DATA PELANGGAN
    public List< Map<String, Object> > tampilPelanggan() {
        String sql = "SELECT * FROM pelanggan ORDER BY id_pelanggan DESC";
        return jdbcTemplate.queryForList(sql);
    }
}