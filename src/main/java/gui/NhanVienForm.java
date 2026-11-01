package gui;

import dao.NhanVienDAO;
import model.NhanVien;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class NhanVienForm extends JPanel {

    private NhanVienDAO nhanVienDAO = new NhanVienDAO();

    private JTextField txtId, txtHoTen, txtTenDangNhap;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cbChucVu;
    private JTable tblNhanVien;
    private DefaultTableModel modelNhanVien;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    public NhanVienForm() {
//        setTitle("Quản Lý Nhân Viên & Tài Khoản");
//        setSize(850, 520);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);

        initComponents();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // --- FORM NHẬP THÔNG TIN (BÊN TRÁI) ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Thông Tin Nhân Viên"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã Nhân Viên
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Mã NV:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        txtId = new JTextField(15);
        txtId.setEditable(false);
        panelForm.add(txtId, gbc);

        // Họ và Tên
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtHoTen = new JTextField(15);
        panelForm.add(txtHoTen, gbc);

        // Tên Đăng Nhập
        gbc.gridx = 0; gbc.gridy = 2;
        panelForm.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtTenDangNhap = new JTextField(15);
        panelForm.add(txtTenDangNhap, gbc);

        // Mật Khẩu
        gbc.gridx = 0; gbc.gridy = 3;
        panelForm.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        txtMatKhau = new JPasswordField(15);
        panelForm.add(txtMatKhau, gbc);

        // Chức Vụ
        gbc.gridx = 0; gbc.gridy = 4;
        panelForm.add(new JLabel("Chức vụ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4;
        cbChucVu = new JComboBox<>(new String[]{"Nhân viên", "Quản lý"});
        panelForm.add(cbChucVu, gbc);

        // Panel Nút Bấm
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");

        panelButtons.add(btnThem);
        panelButtons.add(btnSua);
        panelButtons.add(btnXoa);
        panelButtons.add(btnLamMoi);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        panelForm.add(panelButtons, gbc);

        add(panelForm, BorderLayout.WEST);

        // --- BẢNG DANH SÁCH (BÊN PHẢI) ---
        modelNhanVien = new DefaultTableModel(new Object[]{"Mã NV", "Họ Tên", "Tên Đăng Nhập", "Chức Vụ"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblNhanVien = new JTable(modelNhanVien);
        add(new JScrollPane(tblNhanVien), BorderLayout.CENTER);

        // --- SỰ KIỆN ---
        tblNhanVien.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                hienThiChiTiet();
            }
        });

        btnThem.addActionListener(e -> xuLyThem());
        btnSua.addActionListener(e -> xuLySua());
        btnXoa.addActionListener(e -> xuLyXoa());
        btnLamMoi.addActionListener(e -> lamMoiForm());
    }

    private void loadDataToTable() {
        modelNhanVien.setRowCount(0);
        List<NhanVien> list = nhanVienDAO.getAllNhanVien(); // Đảm bảo NhanVienDAO có hàm getAll()
        for (NhanVien nv : list) {
            modelNhanVien.addRow(new Object[]{nv.getId(), nv.getTenNv(), nv.getTenDangNhap(), nv.getVaiTro()});
        }
    }

    private void hienThiChiTiet() {
        int row = tblNhanVien.getSelectedRow();
        if (row != -1) {
            txtId.setText(modelNhanVien.getValueAt(row, 0).toString());
            txtHoTen.setText(modelNhanVien.getValueAt(row, 1).toString());
            txtTenDangNhap.setText(modelNhanVien.getValueAt(row, 2).toString());
            cbChucVu.setSelectedItem(modelNhanVien.getValueAt(row, 3).toString());
            txtMatKhau.setText(""); // Bỏ trống mật khẩu vì lý do bảo mật
        }
    }

    private void lamMoiForm() {
        txtId.setText("");
        txtHoTen.setText("");
        txtTenDangNhap.setText("");
        txtMatKhau.setText("");
        cbChucVu.setSelectedIndex(0);
        tblNhanVien.clearSelection();
    }

    private void xuLyThem() {
        String hoTen = txtHoTen.getText().trim();
        String tenDN = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();
        String chucVu = (String) cbChucVu.getSelectedItem();

        if (hoTen.isEmpty() || tenDN.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }

        NhanVien nv = new NhanVien();
        nv.setTenNv(hoTen);
        nv.setTenDangNhap(tenDN);
        nv.setMatKhau(matKhau);
        nv.setVaiTro(chucVu);

        if (nhanVienDAO.insertNhanVien(nv)) { // Đảm bảo NhanVienDAO có hàm insert()
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
            loadDataToTable();
            lamMoiForm();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại (Tên đăng nhập có thể đã tồn tại)!");
        }
    }

    private void xuLySua() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!");
            return;
        }

        int id = Integer.parseInt(txtId.getText());
        String hoTen = txtHoTen.getText().trim();
        String tenDN = txtTenDangNhap.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword()).trim();
        String chucVu = (String) cbChucVu.getSelectedItem();

        NhanVien nv = new NhanVien();
        nv.setId(id);
        nv.setTenNv(hoTen);
        nv.setTenDangNhap(tenDN);
        nv.setMatKhau(matKhau); // Nếu để trống thì giữ nguyên mật khẩu cũ trong DAO
        nv.setVaiTro(chucVu);

        if (nhanVienDAO.updateNhanVien(nv)) { // Đảm bảo NhanVienDAO có hàm update()
            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            loadDataToTable();
            lamMoiForm();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
        }
    }

    private void xuLyXoa() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            if (nhanVienDAO.deleteNhanVien(id)) { // Đảm bảo NhanVienDAO có hàm delete()
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadDataToTable();
                lamMoiForm();
            } else {
                JOptionPane.showMessageDialog(this, "Không thể xóa nhân viên này (đã có ràng buộc với hóa đơn)!");
            }
        }
    }
}