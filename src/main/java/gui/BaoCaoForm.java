package gui;

import dao.HoaDonDAO;
import model.HoaDon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class BaoCaoForm extends JPanel {

    private HoaDonDAO hoaDonDAO = new HoaDonDAO();

    private JTextField txtTuNgay, txtDenNgay;
    private JLabel lblTongDon, lblTongDoanhThu;
    private JTable tblHoaDon;
    private DefaultTableModel modelHoaDon;
    private JButton btnThongKe;

    public BaoCaoForm() {
//        setTitle("Báo Cáo Doanh Thu");
//        setSize(850, 550);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);

        initComponents();
        
        // Mặc định lọc ngày hiện tại
        String today = LocalDate.now().toString(); // Định dạng YYYY-MM-DD
        txtTuNgay.setText(today);
        txtDenNgay.setText(today);
        
        xuLyThongKe();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // --- PANEL BỘ LỌC THỜI GIAN (TOP) ---
        JPanel panelFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelFilter.setBorder(BorderFactory.createTitledBorder("Bộ Lọc Báo Cáo"));

        panelFilter.add(new JLabel("Từ ngày (YYYY-MM-DD):"));
        txtTuNgay = new JTextField(10);
        panelFilter.add(txtTuNgay);

        panelFilter.add(new JLabel("Đến ngày (YYYY-MM-DD):"));
        txtDenNgay = new JTextField(10);
        panelFilter.add(txtDenNgay);

        btnThongKe = new JButton("Xem Báo Cáo");
        btnThongKe.setBackground(new Color(139, 69, 19));
        btnThongKe.setForeground(Color.MAGENTA);
        panelFilter.add(btnThongKe);

        add(panelFilter, BorderLayout.NORTH);

        // --- BẢNG DANH SÁCH HÓA ĐƠN (CENTER) ---
        modelHoaDon = new DefaultTableModel(new Object[]{"Mã HĐ", "Mã Bàn", "Mã NV", "PT Thanh Toán", "Tổng Tiền (VNĐ)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblHoaDon = new JTable(modelHoaDon);
        add(new JScrollPane(tblHoaDon), BorderLayout.CENTER);

        // --- PANEL TỔNG KẾT (BOTTOM) ---
        JPanel panelSummary = new JPanel(new GridLayout(1, 2, 10, 10));
        panelSummary.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        lblTongDon = new JLabel("Tổng số đơn: 0", SwingConstants.LEFT);
        lblTongDon.setFont(new Font("Arial", Font.BOLD, 14));

        lblTongDoanhThu = new JLabel("Tổng doanh thu: 0 VNĐ", SwingConstants.RIGHT);
        lblTongDoanhThu.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongDoanhThu.setForeground(new Color(178, 34, 34));

        panelSummary.add(lblTongDon);
        panelSummary.add(lblTongDoanhThu);

        add(panelSummary, BorderLayout.SOUTH);

        // Sự kiện
        btnThongKe.addActionListener(e -> xuLyThongKe());
    }

    private void xuLyThongKe() {
        String tuNgay = txtTuNgay.getText().trim();
        String denNgay = txtDenNgay.getText().trim();

        if (tuNgay.isEmpty() || denNgay.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ khoảng ngày!");
            return;
        }

        List<HoaDon> list = hoaDonDAO.getHoaDonTheoNgay(tuNgay, denNgay);
        modelHoaDon.setRowCount(0);

        for (HoaDon hd : list) {
            modelHoaDon.addRow(new Object[]{
                hd.getId(),
                hd.getIdBan(),
                hd.getIdNhanVien(),
                hd.getPhuongThucTT(),
                String.format("%.0f", hd.getTongTien())
            });
        }

        double tongTien = hoaDonDAO.getTongDoanhThu(tuNgay, denNgay);
        lblTongDon.setText("Tổng số đơn: " + list.size());
        lblTongDoanhThu.setText(String.format("Tổng doanh thu: %,.0f VNĐ", tongTien));
    }
}