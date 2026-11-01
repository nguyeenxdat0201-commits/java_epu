package gui;

import dao.*;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class BanHangForm extends JPanel {

    // DAOs
    private final BanDAO banDAO = new BanDAO();
    private final MonDAO monDAO = new MonDAO();
    private final HoaDonDAO hoaDonDAO = new HoaDonDAO();
    private final ChiTietHoaDonDAO chiTietDAO = new ChiTietHoaDonDAO();

    // Biến trạng thái đơn hàng hiện tại
    private Integer currentHoaDonId = null;

    // GUI Components
    private JComboBox<Ban> cbBan;
    private JTable tblMon, tblGioHang;
    private DefaultTableModel modelMon, modelGioHang;
    private JSpinner spSoLuong;
    private JTextField txtTienGiam, txtTongTien;
    private JComboBox<String> cbPTTT;
    private JButton btnTaoDon, btnThemMon, btnThanhToan;
    private JComboBox<String> cbSize;
    private JTextField txtGhiChu;

    public BanHangForm() {
        initComponents();
        loadBanToComboBox();
        loadMonToTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // ================= 1. BÊN TRÁI: DANH SÁCH MÓN & CHỌN BÀN =================
        JPanel panelLeft = new JPanel(new BorderLayout(5, 5));
        panelLeft.setBorder(BorderFactory.createTitledBorder("Thực Đơn & Chọn Bàn"));
        panelLeft.setPreferredSize(new Dimension(520, 0));

        // Panel top chọn bàn + Tạo đơn
        JPanel panelTopLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panelTopLeft.add(new JLabel("Chọn bàn:"));
        cbBan = new JComboBox<>();
        panelTopLeft.add(cbBan);

        btnTaoDon = new JButton("Tạo Đơn Mới");
        btnTaoDon.setBackground(new Color(139, 69, 19));
        btnTaoDon.setForeground(Color.BLUE);
        panelTopLeft.add(btnTaoDon);

        panelLeft.add(panelTopLeft, BorderLayout.NORTH);

        // Bảng món ăn
        modelMon = new DefaultTableModel(new Object[]{"ID", "Tên Món", "Đơn Giá (VNĐ)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblMon = new JTable(modelMon);
        panelLeft.add(new JScrollPane(tblMon), BorderLayout.CENTER);

        // Panel bottom thêm món: Size, Số lượng, Ghi chú, Nút thêm
        JPanel panelBotLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        cbSize = new JComboBox<>(new String[]{"Nhỏ (+0đ)", "Vừa (+5.000đ)", "Lớn (+10.000đ)"});
        spSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        txtGhiChu = new JTextField(8);

        panelBotLeft.add(new JLabel("Size:"));
        panelBotLeft.add(cbSize);
        panelBotLeft.add(new JLabel("SL:"));
        panelBotLeft.add(spSoLuong);
        panelBotLeft.add(new JLabel("Ghi chú:"));
        panelBotLeft.add(txtGhiChu);

        btnThemMon = new JButton("+ Thêm Vào Đơn");
        panelBotLeft.add(btnThemMon);

        panelLeft.add(panelBotLeft, BorderLayout.SOUTH);

        // ================= 2. BÊN PHẢI: CHI TIẾT HÓA ĐƠN & THANH TOÁN =================
        JPanel panelRight = new JPanel(new BorderLayout(5, 5));
        panelRight.setBorder(BorderFactory.createTitledBorder("Chi Tiết Hóa Đơn"));

        // Bảng giỏ hàng (7 cột chuẩn)
        modelGioHang = new DefaultTableModel(
                new Object[]{"Món", "Size", "SL", "Đơn Giá", "Phụ phí size", "Thành Tiền", "Ghi chú"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblGioHang = new JTable(modelGioHang);
        panelRight.add(new JScrollPane(tblGioHang), BorderLayout.CENTER);

        // Panel tính tiền & Thanh toán
        JPanel panelPayment = new JPanel(new GridBagLayout());
        panelPayment.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dòng 1: Tiền giảm
        gbc.gridx = 0; gbc.gridy = 0;
        panelPayment.add(new JLabel("Tiền giảm giá (VNĐ):"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        txtTienGiam = new JTextField("0");
        panelPayment.add(txtTienGiam, gbc);

        // Dòng 2: Tổng tiền thanh toán
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelPayment.add(new JLabel("Tổng tiền trả:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        txtTongTien = new JTextField("0");
        txtTongTien.setEditable(false);
        txtTongTien.setFont(new Font("Arial", Font.BOLD, 14));
        txtTongTien.setForeground(Color.RED);
        panelPayment.add(txtTongTien, gbc);

        // Dòng 3: Phương thức thanh toán
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        panelPayment.add(new JLabel("Hình thức:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        cbPTTT = new JComboBox<>(new String[]{"Tiền mặt", "Chuyển khoản"});
        panelPayment.add(cbPTTT, gbc);

        // Dòng 4: Nút thanh toán
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        btnThanhToan = new JButton("XÁC NHẬN THANH TOÁN");
        btnThanhToan.setFont(new Font("Arial", Font.BOLD, 14));
        btnThanhToan.setBackground(new Color(34, 139, 34));
        btnThanhToan.setForeground(Color.magenta);
        panelPayment.add(btnThanhToan, gbc);

        panelRight.add(panelPayment, BorderLayout.SOUTH);

        add(panelLeft, BorderLayout.WEST);
        add(panelRight, BorderLayout.CENTER);

        // Gán sự kiện
        btnTaoDon.addActionListener(this::xuLyTaoDon);
        btnThemMon.addActionListener(this::xuLyThemMon);
        btnThanhToan.addActionListener(this::xuLyThanhToan);
    }

    // --- CÁC HÀM XỬ LÝ DỮ LIỆU ---

    private void loadBanToComboBox() {
        cbBan.removeAllItems();
        List<Ban> list = banDAO.getAllBan();
        for (Ban b : list) {
            cbBan.addItem(b);
        }

        // Tự động kiểm tra nạp lại hóa đơn chưa thanh toán khi đổi Bàn
        cbBan.addActionListener(e -> {
            Ban banChon = (Ban) cbBan.getSelectedItem();
            if (banChon != null) {
                int idHD = hoaDonDAO.getHoaDonChuaThanhToanByBan(banChon.getId());
                if (idHD > 0) {
                    this.currentHoaDonId = idHD;
                    loadChiTietHoaDonLenBang(idHD);
                } else {
                    this.currentHoaDonId = null;
                    modelGioHang.setRowCount(0);
                    txtTongTien.setText("0");
                }
            }
        });
    }

    private void loadMonToTable() {
        modelMon.setRowCount(0);
        List<Mon> list = monDAO.getAllMon();
        for (Mon m : list) {
            modelMon.addRow(new Object[]{m.getId(), m.getTenMon(), m.getDonGiaCoBan()});
        }
    }

    // 1. Tạo hóa đơn mới
    private void xuLyTaoDon(ActionEvent e) {
        Ban banChon = (Ban) cbBan.getSelectedItem();
        if (banChon == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn bàn!");
            return;
        }

        int idHDCu = hoaDonDAO.getHoaDonChuaThanhToanByBan(banChon.getId());
        if (idHDCu > 0) {
            this.currentHoaDonId = idHDCu;
            loadChiTietHoaDonLenBang(idHDCu);
            JOptionPane.showMessageDialog(this, "Bàn này đang có Hóa đơn #" + currentHoaDonId + " chưa thanh toán!");
            return;
        }

        HoaDon hd = new HoaDon();
        hd.setIdBan(banChon.getId());
        hd.setIdNhanVien(AuthSession.currentUser.getId());

        int idHoaDonMoi = hoaDonDAO.taoHoaDon(hd);

        if (idHoaDonMoi > 0) {
            this.currentHoaDonId = idHoaDonMoi;
            modelGioHang.setRowCount(0);
            txtTongTien.setText("0");
            JOptionPane.showMessageDialog(this, "Đã tạo thành công Hóa đơn #" + currentHoaDonId + " cho " + banChon.getTenBan());
        } else {
            JOptionPane.showMessageDialog(this, "Tạo hóa đơn thất bại!");
        }
    }

    // 2. Thêm món vào hóa đơn
    private void xuLyThemMon(ActionEvent e) {
        if (currentHoaDonId == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn Bàn và bấm 'Tạo Đơn Mới' trước!");
            return;
        }

        int selectedRow = tblMon.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 món từ bảng Thực Đơn!");
            return;
        }

        try {
            int idMon = Integer.parseInt(modelMon.getValueAt(selectedRow, 0).toString());
            String tenMon = modelMon.getValueAt(selectedRow, 1).toString();
            double donGia = Double.parseDouble(modelMon.getValueAt(selectedRow, 2).toString());
            int soLuong = (int) spSoLuong.getValue();

            // Tính Size và Phụ phí Size
            String sizeSelected = (String) cbSize.getSelectedItem();
            String size = "Nhỏ";
            double phuPhiSize = 0;
            if (sizeSelected != null && sizeSelected.startsWith("Vừa")) {
                size = "Vừa";
                phuPhiSize = 5000;
            } else if (sizeSelected != null && sizeSelected.startsWith("Lớn")) {
                size = "Lớn";
                phuPhiSize = 10000;
            }

            String ghiChu = txtGhiChu.getText().trim();
            double thanhTien = (donGia + phuPhiSize) * soLuong;

            ChiTietHoaDon ct = new ChiTietHoaDon();
            ct.setIdHoaDon(currentHoaDonId);
            ct.setIdMon(idMon);
            ct.setSoLuong(soLuong);
            ct.setDonGia(donGia);
            ct.setSize(size);
            ct.setPhuPhiSize(phuPhiSize);
            ct.setGhiChu(ghiChu);

            boolean ok = chiTietDAO.insertChiTiet(ct);
            if (ok) {
                modelGioHang.addRow(new Object[]{
                        tenMon, size, soLuong, donGia, phuPhiSize, thanhTien, ghiChu
                });
                tinhTongTien();
                txtGhiChu.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Thêm món thất bại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi thêm món: " + ex.getMessage());
        }
    }

    // 3. Tính lại tổng tiền trên giao diện
    private void tinhTongTien() {
        double tong = 0;
        for (int i = 0; i < modelGioHang.getRowCount(); i++) {
            tong += Double.parseDouble(modelGioHang.getValueAt(i, 5).toString()); // Cột 5 là Thành Tiền
        }

        double tienGiam = 0;
        try {
            tienGiam = Double.parseDouble(txtTienGiam.getText().trim());
        } catch (NumberFormatException ignored) {}

        double tongThanhToan = Math.max(0, tong - tienGiam);
        txtTongTien.setText(String.format("%.0f", tongThanhToan));
    }

    // 4. Thanh toán hóa đơn
    private void xuLyThanhToan(ActionEvent e) {
        if (currentHoaDonId == null) {
            JOptionPane.showMessageDialog(this, "Chưa có hóa đơn nào được chọn!");
            return;
        }

        if (modelGioHang.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống, không thể thanh toán!");
            return;
        }

        double tienGiam = 0;
        try {
            tienGiam = Double.parseDouble(txtTienGiam.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Tiền giảm giá không hợp lệ!");
            return;
        }

        double tongTien = Double.parseDouble(txtTongTien.getText().trim());
        String pttt = (String) cbPTTT.getSelectedItem();

        Integer idKhachHang = null;
        Integer idKhuyenMai = null;

        boolean success = hoaDonDAO.thanhToanHoaDon(
                currentHoaDonId,
                idKhachHang,
                idKhuyenMai,
                tienGiam,
                tongTien,
                pttt
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "Thanh toán thành công Hóa đơn #" + currentHoaDonId);

            currentHoaDonId = null;
            modelGioHang.setRowCount(0);
            txtTienGiam.setText("0");
            txtTongTien.setText("0");
        } else {
            JOptionPane.showMessageDialog(this, "Thanh toán thất bại!");
        }
    }

    // 5. Nạp chi tiết hóa đơn lên bảng
    private void loadChiTietHoaDonLenBang(int idHoaDon) {
        modelGioHang.setRowCount(0);
        List<ChiTietHoaDon> listCT = chiTietDAO.getChiTietByHoaDonId(idHoaDon);

        for (ChiTietHoaDon ct : listCT) {
            String tenMon = "Món #" + ct.getIdMon();
            for (Mon m : monDAO.getAllMon()) {
                if (m.getId() == ct.getIdMon()) {
                    tenMon = m.getTenMon();
                    break;
                }
            }

            double donGia = ct.getDonGia();
            double phuPhi = ct.getPhuPhiSize();
            double thanhTien = (donGia + phuPhi) * ct.getSoLuong();
            String size = ct.getSize() != null ? ct.getSize() : "S";
            String ghiChu = ct.getGhiChu() != null ? ct.getGhiChu() : "";

            modelGioHang.addRow(new Object[]{
                    tenMon,
                    size,
                    ct.getSoLuong(),
                    donGia,
                    phuPhi,
                    thanhTien,
                    ghiChu
            });
        }
        tinhTongTien();
    }
}