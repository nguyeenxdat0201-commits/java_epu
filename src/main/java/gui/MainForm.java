package gui;

import model.NhanVien;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainForm extends JFrame {

    private JLabel lblWelcome;
    private JButton btnBanHang, btnQuanLyMon, btnQuanLyNhanVien, btnBaoCao, btnDangXuat;
    private JPanel panelContent;

    public MainForm() {
        // Kiểm tra an toàn: Nếu chưa đăng nhập thì đẩy về LoginForm
        if (!AuthSession.isLoggedIn()) {
            JOptionPane.showMessageDialog(null, "Vui lòng đăng nhập trước!");
            new LoginForm().setVisible(true);
            this.dispose();
            return;
        }

        setTitle("Hệ Thống Quản Lý Quán Cà Phê");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        phanQuyenNguoiDung();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // --- 1. THANH TIÊU ĐỀ TRÊN (HEADER) ---
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(139, 69, 19)); // Màu nâu cà phê
        panelHeader.setPreferredSize(new Dimension(1000, 60));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblAppTitle = new JLabel("COFFEE SHOP MANAGEMENT");
        lblAppTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblAppTitle.setForeground(Color.WHITE);

        NhanVien nv = AuthSession.currentUser;
        lblWelcome = new JLabel("Xin chào: " + nv.getTenNv() + " (" + nv.getVaiTro() + ")");
        lblWelcome.setFont(new Font("Arial", Font.ITALIC, 14));
        lblWelcome.setForeground(Color.WHITE);

        panelHeader.add(lblAppTitle, BorderLayout.WEST);
        panelHeader.add(lblWelcome, BorderLayout.EAST);
        add(panelHeader, BorderLayout.NORTH);

        // --- 2. THANH MENU TRÁI (SIDEBAR) ---
        JPanel panelSidebar = new JPanel();
        panelSidebar.setLayout(new BoxLayout(panelSidebar, BoxLayout.Y_AXIS));
        panelSidebar.setBackground(new Color(240, 235, 225));
        panelSidebar.setPreferredSize(new Dimension(200, 0));
        panelSidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        btnBanHang = createMenuButton("Bán Hàng (POS)");
        btnQuanLyMon = createMenuButton("Quản Lý Món");
        btnQuanLyNhanVien = createMenuButton("Quản Lý Nhân Viên");
        btnBaoCao = createMenuButton("Báo Cáo Doanh Thu");
        btnDangXuat = createMenuButton("Đăng Xuất");

        // Đổi màu nút đăng xuất để nổi bật
        btnDangXuat.setBackground(new Color(178, 34, 34));
        btnDangXuat.setForeground(Color.RED);

        panelSidebar.add(btnBanHang);
        panelSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        panelSidebar.add(btnQuanLyMon);
        panelSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        panelSidebar.add(btnQuanLyNhanVien);
        panelSidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        panelSidebar.add(btnBaoCao);
        panelSidebar.add(Box.createVerticalGlue()); // Đẩy nút Đăng xuất xuống đáy
        panelSidebar.add(btnDangXuat);

        add(panelSidebar, BorderLayout.WEST);

        // --- 3. KHU VỰC HIỂN THỊ CHÍNH (CENTER CONTENT) ---
        panelContent = new JPanel(new BorderLayout());
        panelContent.setBackground(Color.WHITE);
        
        JLabel lblPlaceholder = new JLabel("Vui lòng chọn chức năng từ menu bên trái", SwingConstants.CENTER);
        lblPlaceholder.setFont(new Font("Arial", Font.PLAIN, 16));
        lblPlaceholder.setForeground(Color.GRAY);
        panelContent.add(lblPlaceholder, BorderLayout.CENTER);

        add(panelContent, BorderLayout.CENTER);

        // --- SỰ KIỆN NÚT BẤM ---
        btnBanHang.addActionListener(e -> showForm(new BanHangForm()));
        btnQuanLyMon.addActionListener(e -> showForm(new MonForm()));
        btnQuanLyNhanVien.addActionListener(e -> showForm(new NhanVienForm()));
        btnBaoCao.addActionListener(e -> showForm(new BaoCaoForm()));
        btnDangXuat.addActionListener(this::xuLyDangXuat);
    }

    // Hàm tạo nút menu thống nhất giao diện
    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setFocusPainted(false);
        return btn;
    }

    // Phân quyền hiển thị dựa trên Chức vụ
    private void phanQuyenNguoiDung() {
        NhanVien nv = AuthSession.currentUser;
        if (nv != null && "Nhân viên".equalsIgnoreCase(nv.getVaiTro())) {
            // Nhân viên thu ngân chỉ thấy nút Bán Hàng, ẩn các chức năng Quản lý
            btnQuanLyMon.setVisible(false);
            btnQuanLyNhanVien.setVisible(false);
            btnBaoCao.setVisible(false);
        }
    }

    private void xuLyDangXuat(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(
            this, 
            "Bạn có chắc chắn muốn đăng xuất?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            AuthSession.logout(); // Xóa phiên đăng nhập
            this.dispose();       // Đóng MainForm
            new LoginForm().setVisible(true); // Quay lại LoginForm
        }
    }

    private void showForm(JPanel form) {
        panelContent.removeAll();
        panelContent.add(form, BorderLayout.CENTER);
        panelContent.revalidate();
        panelContent.repaint();
    }
}