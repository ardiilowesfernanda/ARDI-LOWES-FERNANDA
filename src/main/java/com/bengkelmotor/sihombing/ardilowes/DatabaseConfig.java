
package com.bengkelmotor.sihombing.ardilowes;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DatabaseConfig {

    // KONFIGURASI KONEKSI MYSQL
    @Bean
    public DataSource dataSource() {

        DriverManagerDataSource ds =
                new DriverManagerDataSource();

        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");

        ds.setUrl("jdbc:mysql://localhost:3306/fernanda");

        ds.setUsername("root");
        ds.setPassword("");

        return ds;
    }

    // KONFIGURASI JDBC TEMPLATE
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {

        return new JdbcTemplate(dataSource);
    }

    // DAO PELANGGAN
    @Bean
    public PelangganDAO pelangganDAO(JdbcTemplate jdbcTemplate) {

        return new PelangganDAO(jdbcTemplate);
    }

    // DAO LAYANAN
    @Bean
    public LayananDAO layananDAO(JdbcTemplate jdbcTemplate) {

        return new LayananDAO(jdbcTemplate);
    }
}
