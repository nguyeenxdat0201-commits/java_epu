package gui;

import dao.DanhMucDAO;
import dao.MonDAO;
import model.DanhMuc;
import model.Mon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MonForm extends JPanel {

    private MonDAO monDAO = new MonDAO();
    private DanhMucDAO danhMucDAO = new DanhMucDAO();

    private JTextField txtId, txtTenMon, txtGia;
    private JComboBox<DanhMuc> cbDanhMuc;
    private JTable tblMon;
    private DefaultTableModel modelMon;
    private JButton btnThem, btnSua, btnXoa, btnLamMoi;

    public MonForm() {
//        setTitle("Quản Lý Thực Đơn (Món)");
//        setSize(800, 500);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);

        initComponents();
        loadDanhMucToComboBox();
        loadDataToTable();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // --- FORM NHẬP THÔNG TIN (BÊN TRÁI) ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Thông Tin Món"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Mã Món (Auto/Readonly)
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Mã Món:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        txtId = new JTextField(15);
        txtId.setEditable(false);
        panelForm.add(txtId, gbc);

        // Tên Món
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel("Tên Món:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        txtTenMon = new JTextField(15);
        panelForm.add(txtTenMon, gbc);

        // Đơn Giá
        gbc.gridx = 0; gbc.gridy = 2;
        panelForm.add(new JLabel("Đơn Giá:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2;
        txtGia = new JTextField(15);
        panelForm.add(txtGia, gbc);

        // Danh Mục
        gbc.gridx = 0; gbc.gridy = 3;
        panelForm.add(new JLabel("Danh Mục:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3;
        cbDanhMuc = new JComboBox<>();
        panelForm.add(cbDanhMuc, gbc);

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

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        panelForm.add(panelButtons, gbc);

        add(panelForm, BorderLayout.WEST);

        // --- BẢNG HIỂN THỊ DANH SÁCH (BÊN PHẢI) ---
        modelMon = new DefaultTableModel(new Object[]{"Mã món","Tên Món", "Đơn Giá", "Danh Mục"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblMon = new JTable(modelMon);
        add(new JScrollPane(tblMon), BorderLayout.CENTER);

        // --- SỰ KIỆN ---
        tblMon.addMouseListener(new MouseAdapter() {
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

    private void loadDanhMucToComboBox() {
        cbDanhMuc.removeAllItems();
        List<DanhMuc> list = danhMucDAO.getAllDanhMuc();
        for (DanhMuc dm : list) {
            cbDanhMuc.addItem(dm);
        }
    }

    private void loadDataToTable() {
        modelMon.setRowCount(0);
        List<Mon> list = monDAO.getAllMon();

        for (Mon m : list) {
            modelMon.addRow(new Object[]{m.getId(), m.getTenMon(),String.format("%.0f",m.getDonGiaCoBan()) , m.getIdDanhMuc()});
        }
    }

    private void hienThiChiTiet() {
        int row = tblMon.getSelectedRow();
        if (row != -1) {
            txtId.setText(modelMon.getValueAt(row, 0).toString());
            txtTenMon.setText(modelMon.getValueAt(row, 1).toString());
            txtGia.setText(modelMon.getValueAt(row, 2).toString());
        }
    }

    private void lamMoiForm() {
        txtId.setText("");
        txtTenMon.setText("");
        txtGia.setText("");
        tblMon.clearSelection();
    }

    private void xuLyThem() {
        try {
            String ten = txtTenMon.getText().trim();
            double gia = Double.parseDouble(txtGia.getText().trim());
            DanhMuc dm = (DanhMuc) cbDanhMuc.getSelectedItem();

            if (ten.isEmpty() || dm == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            Mon m = new Mon();
            m.setTenMon(ten);
            m.setDonGiaCoBan(gia);
            m.setIdDanhMuc(dm.getId());

            if (monDAO.insertMon(m)) {
                JOptionPane.showMessageDialog(this, "Thêm món thành công!");
                loadDataToTable();
                lamMoiForm();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá phải là số hợp lệ!");
        }
    }

    private void xuLySua() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần sửa từ bảng!");
            return;
        }

        try {
            int id = Integer.parseInt(txtId.getText());
            String ten = txtTenMon.getText().trim();
            double gia = Double.parseDouble(txtGia.getText().trim());
            DanhMuc dm = (DanhMuc) cbDanhMuc.getSelectedItem();

            Mon m = new Mon();
            m.setId(id);
            m.setTenMon(ten);
            m.setDonGiaCoBan(gia);
            m.setIdDanhMuc(dm.getId());

            if (monDAO.updateMon(m)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadDataToTable();
                lamMoiForm();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá không hợp lệ!");
        }
    }

    private void xuLyXoa() {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa món này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            if (monDAO.deleteMon(id)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadDataToTable();
                lamMoiForm();
            }
        }
    }
}