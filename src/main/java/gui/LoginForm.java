package gui;

import dao.NhanVienDAO;
import model.NhanVien;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;
    private NhanVienDAO nhanVienDAO = new NhanVienDAO();

    public LoginForm() {
        setTitle("Đăng Nhập Hệ Thống - Coffee Shop");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initComponents();
    }

    private void initComponents() {
        JPanel panelMain = new JPanel(new BorderLayout(10, 10));
        panelMain.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Tiêu đề
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(new Color(139, 69, 19)); // Màu nâu cà phê
        panelMain.add(lblTitle, BorderLayout.NORTH);

        // Form nhập liệu
        JPanel panelForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dòng 1: Tên đăng nhập
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Tên đăng nhập:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtUsername = new JTextField(15);
        panelForm.add(txtUsername, gbc);

        // Dòng 2: Mật khẩu
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelForm.add(new JLabel("Mật khẩu:"), gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtPassword = new JPasswordField(15);
        panelForm.add(txtPassword, gbc);

        panelMain.add(panelForm, BorderLayout.CENTER);

        // Panel Nút bấm
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        btnLogin = new JButton("Đăng nhập");
        btnExit = new JButton("Thoát");

        // Styling nút
       // btnLogin.setBackground(new Color(139, 69, 19));
       // btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);

        panelButtons.add(btnLogin);
        panelButtons.add(btnExit);
        panelMain.add(panelButtons, BorderLayout.SOUTH);

        add(panelMain);

        // Sự kiện
        btnLogin.addActionListener(this::xuLyDangNhap);
        btnExit.addActionListener(e -> System.exit(0));

        // Nhấn Enter ở ô mật khẩu để đăng nhập luôn
        getRootPane().setDefaultButton(btnLogin);
    }

    private void xuLyDangNhap(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        // Validate cơ bản
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập đầy đủ Tên đăng nhập và Mật khẩu!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Gọi DAO kiểm tra thông tin
        NhanVien nv = nhanVienDAO.checkDangNhap(username, password);

        if (nv != null) {
            // Lưu nhân viên vào phiên đăng nhập
            AuthSession.currentUser = nv;

            JOptionPane.showMessageDialog(this, 
                "Đăng nhập thành công!\nXin chào: " + nv.getTenNv() + " (" + nv.getVaiTro() + ")");

            // Đóng LoginForm
            this.dispose();

            // Mở màn hình chính (MainForm hoặc BanHangForm)
            SwingUtilities.invokeLater(() -> new MainForm().setVisible(true));
        } else {
            JOptionPane.showMessageDialog(this, 
                "Tên đăng nhập hoặc Mật khẩu không chính xác!", 
                "Lỗi đăng nhập", 
                JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtPassword.requestFocus();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}