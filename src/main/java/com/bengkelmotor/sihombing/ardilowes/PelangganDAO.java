
package com.bengkelmotor.sihombing.ardilowes;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public class PelangganDAO {

    private final JdbcTemplate jdbcTemplate;

    public PelangganDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // MENAMBAH DATA PELANGGAN
    public int tambahPelanggan(
            String nama,
            String noHp,
            String noPolisi,
            String jenisMotor) {

        String sql = "INSERT INTO pelanggan "
                + "(nama_pelanggan, no_hp, no_polisi, jenis_motor) "
                + "VALUES (?, ?, ?, ?)";

        return jdbcTemplate.update(
                sql, nama, noHp, noPolisi, jenisMotor
        );
    }

    // MENAMPILKAN DATA PELANGGAN
    public List<Map<String, Object>> tampilPelanggan() {

        String sql = "SELECT * FROM pelanggan "
                + "ORDER BY id_pelanggan DESC";

        return jdbcTemplate.queryForList(sql);
    }
}
