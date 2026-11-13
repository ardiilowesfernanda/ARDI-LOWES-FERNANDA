
package com.bengkelmotor.sihombing.ardilowes;

import javax.swing.JOptionPane;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LoginForm extends javax.swing.JFrame {

    private javax.swing.JTextField txtUsername;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JButton btnSubmit;

    private AnnotationConfigApplicationContext springContext;

    public LoginForm() {

        setTitle("Login - Bengkel Motor Sihombing");
        setSize(450, 330);
        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        javax.swing.JPanel panel = new javax.swing.JPanel();

        panel.setBackground(new java.awt.Color(220, 224, 230));
        panel.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(30, 40, 30, 40)
        );
        panel.setLayout(new java.awt.GridLayout(4, 2, 15, 20));

        javax.swing.JLabel title = new javax.swing.JLabel(
            "LOGIN ADMIN",
            javax.swing.SwingConstants.CENTER
        );

        title.setFont(new java.awt.Font(
            "Arial", java.awt.Font.BOLD, 20
        ));

        txtUsername = new javax.swing.JTextField();
        txtPassword = new javax.swing.JPasswordField();

        btnSubmit = new javax.swing.JButton("SUBMIT");
        btnSubmit.addActionListener(e -> prosesLogin());

        panel.add(title);
        panel.add(new javax.swing.JLabel(""));
        panel.add(new javax.swing.JLabel("Username"));
        panel.add(txtUsername);
        panel.add(new javax.swing.JLabel("Password"));
        panel.add(txtPassword);
        panel.add(new javax.swing.JLabel(""));
        panel.add(btnSubmit);

        add(panel);
    }

    private void prosesLogin() {

        String username = txtUsername.getText().trim();

        String password = new String(
            txtPassword.getPassword()
        ).trim();

        if (username.isEmpty() || password.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Username dan password wajib diisi!"
            );

            return;
        }

        String url = "jdbc:mysql://localhost:3306/fernanda";

        try (
            java.sql.Connection conn =
                java.sql.DriverManager.getConnection(
                    url, "root", ""
                );

            java.sql.PreparedStatement ps =
                conn.prepareStatement(
                    "SELECT * FROM pengguna "
                    + "WHERE username = ? AND password = ?"
                )
        ) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (java.sql.ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    try {

                        // MEMBUAT SPRING CONTEXT
                        springContext =
                            new AnnotationConfigApplicationContext(
                                DatabaseConfig.class
                            );

                        // MENGAMBIL DAO
                        LayananDAO layananDAO =
                            springContext.getBean(LayananDAO.class);

                        // MEMBUKA HALAMAN UTAMA
                        ServiceFrame halaman =
                            new ServiceFrame(username, layananDAO);

                        halaman.setVisible(true);

                        this.dispose();

                    } catch (Exception e) {

                        if (springContext != null) {
                            springContext.close();
                            springContext = null;
                        }

                        JOptionPane.showMessageDialog(
                            this,
                            "Gagal mengakses Spring DAO: "
                            + e.getMessage()
                        );
                    }

                } else {

                    JOptionPane.showMessageDialog(
                        this,
                        "Username atau password salah!"
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
