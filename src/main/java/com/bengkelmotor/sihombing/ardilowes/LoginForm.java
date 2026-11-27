package com.bengkelmotor.sihombing.ardilowes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnSubmit;

    private AnnotationConfigApplicationContext springContext;

    public LoginForm() {

        setTitle("Login - Bengkel Motor Sihombing");
        setSize(460, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelUtama = new JPanel(new BorderLayout());
        panelUtama.setBackground(new Color(240, 244, 248));

        // HEADER NAVY
        JPanel panelHeader = new JPanel(new BorderLayout(0, 6));
        panelHeader.setBackground(new Color(30, 58, 95));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));

        JLabel lblBengkel = new JLabel("BENGKEL MOTOR SIHOMBING", SwingConstants.CENTER);
        lblBengkel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblBengkel.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Login Administrator Sistem Servis", SwingConstants.CENTER);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(200, 218, 235));

        panelHeader.add(lblBengkel, BorderLayout.CENTER);
        panelHeader.add(lblSub, BorderLayout.SOUTH);

        // FORM CARD PUTIH
        JPanel panelWrapper = new JPanel(new GridBagLayout());
        panelWrapper.setBackground(new Color(240, 244, 248));
        panelWrapper.setBorder(BorderFactory.createEmptyBorder(20, 35, 25, 35));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(215, 223, 232), 1),
            BorderFactory.createEmptyBorder(25, 28, 25, 28)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(new Color(50, 65, 85));

        txtUsername = new JTextField();
        txtUsername.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtUsername.setPreferredSize(new Dimension(280, 36));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(190, 200, 212)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(new Color(50, 65, 85));

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(280, 36));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(190, 200, 212)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        btnSubmit = new JButton("MASUK APLIKASI");
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSubmit.setBackground(new Color(30, 95, 168));
        btnSubmit.setForeground(Color.WHITE);
        btnSubmit.setPreferredSize(new Dimension(280, 40));
        btnSubmit.setFocusPainted(false);
        btnSubmit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSubmit.addActionListener(e -> prosesLogin());

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(lblUser, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 14, 0);
        card.add(txtUsername, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(lblPass, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        card.add(txtPassword, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(btnSubmit, gbc);

        panelWrapper.add(card);

        panelUtama.add(panelHeader, BorderLayout.NORTH);
        panelUtama.add(panelWrapper, BorderLayout.CENTER);

        add(panelUtama);

        // AGAR BISA TEKAN ENTER LANGSUNG LOGIN
        getRootPane().setDefaultButton(btnSubmit);
    }

    private void prosesLogin() {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Username dan password wajib diisi!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String url = "jdbc:mysql://localhost:3306/fernanda";

        try (
            java.sql.Connection conn =
                java.sql.DriverManager.getConnection(url, "root", "");
            java.sql.PreparedStatement ps =
                conn.prepareStatement(
                    "SELECT * FROM pengguna WHERE username = ? AND password = ?"
                )
        ) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (java.sql.ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    try {
                        springContext =
                            new AnnotationConfigApplicationContext(
                                DatabaseConfig.class
                            );

                        LayananDAO layananDAO =
                            springContext.getBean(LayananDAO.class);
                        PelangganDAO pelangganDAO =
                            springContext.getBean(PelangganDAO.class);

                        ServiceFrame halaman =
                            new ServiceFrame(username, layananDAO, pelangganDAO);

                        halaman.setVisible(true);
                        this.dispose();

                    } catch (Exception e) {
                        if (springContext != null) {
                            springContext.close();
                            springContext = null;
                        }
                        JOptionPane.showMessageDialog(
                            this,
                            "Gagal mengakses Spring DAO: " + e.getMessage()
                        );
                    }

                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Username atau password salah!",
                        "Login Gagal",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }

        } catch (java.sql.SQLException e) {
            JOptionPane.showMessageDialog(
                this,
                "Error Database: " + e.getMessage()
            );
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new LoginForm().setVisible(true);
        });
    }
}