
package com.bengkelmotor.sihombing.ardilowes;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

public class LayananDAO {

    private final JdbcTemplate jdbcTemplate;

    public LayananDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // MENAMPILKAN DAFTAR LAYANAN
    public List<Map<String, Object>> tampilLayanan() {

        String sql = "SELECT * FROM layanan "
                + "ORDER BY id_layanan ASC";

        return jdbcTemplate.queryForList(sql);
    }
}
