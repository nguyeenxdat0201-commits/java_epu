package dao;

import connectdatabase.DBConnect;
import model.ChiTietHoaDon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {

    // Thêm món vào chi tiết hóa đơn
    public boolean insertChiTiet(ChiTietHoaDon ct) {
        String sql = "INSERT INTO chitiethoadon (id_hoa_don, id_mon, so_luong, don_gia, size, phu_phi_size, ghi_chu) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection conn = DBConnect.getConnection();
                PreparedStatement pStmt = conn.prepareStatement(sql)
        ) {
            pStmt.setInt(1, ct.getIdHoaDon());
            pStmt.setInt(2, ct.getIdMon());
            pStmt.setInt(3, ct.getSoLuong());
            pStmt.setDouble(4, ct.getDonGia());
            pStmt.setString(5, ct.getSize());
            pStmt.setDouble(6, ct.getPhuPhiSize());
            pStmt.setString(7, ct.getGhiChu());

            return pStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 2. BỔ SUNG: Lấy danh sách món trong chi tiết hóa đơn theo id_hoa_don
    public List<ChiTietHoaDon> getChiTietByHoaDonId(int idHoaDon) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT * FROM chitiethoadon WHERE id_hoa_don = ?";

        try (
                Connection conn = DBConnect.getConnection();
                PreparedStatement pStmt = conn.prepareStatement(sql)
        ) {
            pStmt.setInt(1, idHoaDon);
            try (ResultSet rs = pStmt.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDon ct = new ChiTietHoaDon();
                    ct.setIdHoaDon(rs.getInt("id"));
                    ct.setIdHoaDon(rs.getInt("id_hoa_don"));
                    ct.setIdMon(rs.getInt("id_mon"));
                    ct.setSoLuong(rs.getInt("so_luong"));
                    ct.setDonGia(rs.getDouble("don_gia"));
                    ct.setSize(rs.getString("size"));
                    ct.setPhuPhiSize(rs.getDouble("phu_phi_size"));
                    ct.setGhiChu(rs.getString("ghi_chu"));
                    list.add(ct);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
