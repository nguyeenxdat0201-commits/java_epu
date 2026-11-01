package dao;

import connectdatabase.DBConnect;
import model.HoaDon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class HoaDonDAO {


    public int taoHoaDon(HoaDon hd) {
        String sql = "INSERT INTO hoadon (id_ban, id_nhan_vien, ngay_tao, trang_thai, tong_tien) " +
                "VALUES (?, ?, NOW(), 'Chưa thanh toán', 0)";

        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ){
            pStmt.setInt(1, hd.getIdBan());
            pStmt.setInt(2, hd.getIdNhanVien());

            int taothanhcong = pStmt.executeUpdate();

            if (taothanhcong > 0) {
                try (ResultSet rs = pStmt.getGeneratedKeys()) {
                    if(rs.next()){
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public int getHoaDonChuaThanhToanByBan( int idban){
        String sql = "SELECT id FROM hoadon WHERE id_ban = ? AND trang_thai = 'Chưa thanh toán'";

        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql);
                ){
            pStmt.setInt(1, idban);
            try (
                    ResultSet rs = pStmt.executeQuery();
                    ){
                if (rs.next()){
                    return rs.getInt("id");
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return -1;
    }

    public boolean thanhToanHoaDon(
            int idHoaDon, Integer idKhachHang, Integer idKhuyenMai, double tienGiam,
            double tongTien, String ppThanhToan
    ) {
        String sql = "UPDATE hoadon SET id_khach_hang = ?, id_khuyen_mai = ?, " +
                "tien_giam = ?, tong_tien = ?, pp_thanh_toan = ?," +
                " trang_thai = 'Đã thanh toán' WHERE id = ?";

        try (
                Connection conn = DBConnect.getConnection();
                PreparedStatement pStmt = conn.prepareStatement(sql)
        ) {
            // setObject tự xử lý mượt mà cả giá trị số lẫn null
            pStmt.setObject(1, idKhachHang);
            pStmt.setObject(2, idKhuyenMai);
            pStmt.setDouble(3, tienGiam);
            pStmt.setDouble(4, tongTien);
            pStmt.setString(5, ppThanhToan);
            pStmt.setInt(6, idHoaDon);

            return pStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Lấy danh sách hóa đơn đã thanh toán theo khoảng ngày
    public List<HoaDon> getHoaDonTheoNgay(String tuNgay, String denNgay) {
        List<HoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM hoadon WHERE trang_thai = 'Đã thanh toán' AND DATE(ngay_tao) BETWEEN ? AND ?";

        try (
                Connection conn = DBConnect.getConnection();
                PreparedStatement pStmt = conn.prepareStatement(sql)
        ) {
            pStmt.setString(1, tuNgay);
            pStmt.setString(2, denNgay);
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    HoaDon hd = new HoaDon();
                    hd.setId(rs.getInt("id"));
                    hd.setIdBan(rs.getInt("id_ban"));
                    hd.setIdNhanVien(rs.getInt("id_nhan_vien"));
                    hd.setTongTien(rs.getDouble("tong_tien"));
                    hd.setPhuongThucTT(rs.getString("pp_thanh_toan"));
                    list.add(hd);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tính tổng doanh thu theo khoảng ngày
    public double getTongDoanhThu(String tuNgay, String denNgay) {
        String sql = "SELECT SUM(tong_tien) FROM hoadon WHERE trang_thai = 'Đã thanh toán' AND DATE(ngay_tao) BETWEEN ? AND ?";
        try (
                Connection conn = DBConnect.getConnection();
                PreparedStatement pStmt = conn.prepareStatement(sql)
        ) {
            pStmt.setString(1, tuNgay);
            pStmt.setString(2, denNgay);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
