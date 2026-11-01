package dao;

import connectdatabase.DBConnect;
import model.KhuyenMai;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KhuyenMaiDAO {

    // 1. Lấy toàn bộ danh sách khuyến mãi
    public List<KhuyenMai> getAll() {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = "SELECT * FROM khuyenmai";
        
        try (
            Connection conn = DBConnect.getConnection();
            PreparedStatement pStmt = conn.prepareStatement(sql);
            ResultSet rs = pStmt.executeQuery()
        ) {
            while (rs.next()) {
                KhuyenMai km = mapResultSetToKhuyenMai(rs);
                list.add(km);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Tìm mã khuyến mãi theo Code (Sử dụng khi khách nhập mã giảm giá ở màn Bán Hàng)
    public KhuyenMai findByMa(String maKM) {
        String sql = "SELECT * FROM khuyenmai WHERE ma_khuyen_mai = ? AND trang_thai = 1";
        
        try (
            Connection conn = DBConnect.getConnection();
            PreparedStatement pStmt = conn.prepareStatement(sql)
        ) {
            pStmt.setString(1, maKM);
            try (ResultSet rs = pStmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToKhuyenMai(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. Thêm mới chương trình khuyến mãi
    public boolean insert(KhuyenMai km) {
        String sql = "INSERT INTO khuyenmai (ten_khuyen_mai, ma_khuyen_mai, phan_tram_giam, so_tien_giam, trang_thai) VALUES (?, ?, ?, ?, ?)";
        
        try (
            Connection conn = DBConnect.getConnection();
            PreparedStatement pStmt = conn.prepareStatement(sql)
        ) {
            pStmt.setString(1, km.getTenChuongTrinh());
            pStmt.setString(2, km.getMaKM());
            pStmt.setObject(3, km.getPhanTramGiam()); // Dùng setObject để hỗ trợ giá trị null nếu chỉ giảm tiền
            pStmt.setObject(4, km.getSoTienGiam());   // Dùng setObject để hỗ trợ giá trị null nếu giảm theo %
            pStmt.setBoolean(5, km.getTrangThai());      // 1: Hoạt động, 0: Hết hạn/Tắt
            
            return pStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Cập nhật thông tin khuyến mãi
    public boolean update(KhuyenMai km) {
        String sql = "UPDATE khuyenmai SET ten_khuyen_mai = ?, ma_khuyen_mai = ?, phan_tram_giam = ?, so_tien_giam = ?, trang_thai = ? WHERE id = ?";
        
        try (
            Connection conn = DBConnect.getConnection();
            PreparedStatement pStmt = conn.prepareStatement(sql)
        ) {
            pStmt.setString(1, km.getTenChuongTrinh());
            pStmt.setString(2, km.getMaKM());
            pStmt.setObject(3, km.getPhanTramGiam());
            pStmt.setObject(4, km.getSoTienGiam());
            pStmt.setBoolean(5, km.getTrangThai());
            pStmt.setInt(6, km.getId());
            
            return pStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 5. Xóa khuyến mãi theo ID
    public boolean delete(int id) {
        String sql = "DELETE FROM khuyenmai WHERE id = ?";
        
        try (
            Connection conn = DBConnect.getConnection();
            PreparedStatement pStmt = conn.prepareStatement(sql)
        ) {
            pStmt.setInt(1, id);
            return pStmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper map dữ liệu ResultSet sang Model KhuyenMai
    private KhuyenMai mapResultSetToKhuyenMai(ResultSet rs) throws SQLException {
        KhuyenMai km = new KhuyenMai();
        km.setId(rs.getInt("id"));
        km.setTenChuongTrinh(rs.getString("ten_khuyen_mai"));
        km.setMaKM(rs.getString("ma_khuyen_mai"));

        // 1. Nếu dùng kiểu double thường: gán 0 thay vì null
        double phanTram = rs.getDouble("phan_tram_giam");
        km.setPhanTramGiam(rs.wasNull() ? 0.0 : phanTram);

        double soTien = rs.getDouble("so_tien_giam");
        km.setSoTienGiam(rs.wasNull() ? 0.0 : soTien);

        // 2. Nếu trong KhuyenMai.java trangThai là boolean (true/false) thì dùng rs.getBoolean:
        km.setTrangThai(rs.getBoolean("trang_thai"));

        // NẾU trangThai trong KhuyenMai.java là int thì giữ nguyên:
        // km.setTrangThai(rs.getInt("trang_thai"));

        return km;
    }
}