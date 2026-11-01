package dao;

import connectdatabase.DBConnect;
import model.NhanVien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    public static void main(String[] args) {
        DBConnect.checkConnection();

        NhanVienDAO nhanVienDAO = new NhanVienDAO();

        NhanVien nhanVien = nhanVienDAO.checkDangNhap("Thungan02", "123456");
        System.out.println(nhanVien.getTenNv());
        System.out.println(nhanVien.getSdt());

        for (NhanVien nv : nhanVienDAO.getAllNhanVien()){
            System.out.println(nv.getId());
            System.out.println(nv.getTenNv());
            System.out.println(nv.getSdt());
            System.out.println(nv.getVaiTro());
            System.out.println(nv.isTrangThai());

        }

    }

    public NhanVien checkDangNhap (String username, String password) {
        String sql = "SELECT * FROM nhanvien WHERE ten_dang_nhap = ? " +
                "AND mat_khau = ? AND trang_thai = 1";
        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql)
        ){
            pStmt.setString(1, username);
            pStmt.setString(2, password);

            try (ResultSet rs = pStmt.executeQuery()){
                if (rs.next()){
                    return new NhanVien(
                            rs.getInt("id"),
                            rs.getString("ten_nv"),
                            rs.getString("so_dien_thoai"),
                            rs.getString("ten_dang_nhap"),
                            rs.getString("mat_khau"),
                            rs.getString("vai_tro"),
                            rs.getBoolean("trang_thai")
                    );
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        String sql = "SELECT * FROM nhanvien";
        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql);
                ResultSet rs = pStmt.executeQuery()
        ){
            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getInt("id"),
                        rs.getString("ten_nv"),
                        rs.getString("so_dien_thoai"),
                        rs.getString("ten_dang_nhap"),
                        rs.getString("mat_khau"),
                        rs.getString("vai_tro"),
                        rs.getBoolean("trang_thai")
                );
                list.add(nv);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return list;
    }

    public boolean insertNhanVien(NhanVien nv) {
        String sql = "INSERT INTO nhanvien " +
                "(ten_nv, so_dien_thoai, ten_dang_nhap, mat_khau, vai_tro, trang_thai) VALUES (?,?,?,?,?,?)";
        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql)
        ){
            pStmt.setString(1, nv.getTenNv());
            pStmt.setString(2, nv.getSdt());
            pStmt.setString(3, nv.getTenDangNhap());
            pStmt.setString(4, nv.getMatKhau());
            pStmt.setString(5, nv.getVaiTro());
            pStmt.setBoolean(6, nv.isTrangThai());

            return pStmt.executeUpdate() > 0;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean updateNhanVien(NhanVien nv){
        String sql = "UPDATE nhanvien SET " +
                "ten_nv = ?, so_dien_thoai = ?, mat_khau = ?, vai_tro = ?, trang_thai = ?" +
                "WHERE id = ?";
        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql);
        ){
            pStmt.setString(1 , nv.getTenNv());
            pStmt.setString(2, nv.getSdt());
            pStmt.setString(3, nv.getMatKhau());
            pStmt.setString(4, nv.getVaiTro());
            pStmt.setBoolean(5, nv.isTrangThai());
            pStmt.setInt(6, nv.getId());

            return pStmt.executeUpdate() > 0;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean deleteNhanVien(int id) {
        String sql = "DELETE FROM nhanvien WHERE id = ?";

        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql);
        ){
            pStmt.setInt(1, id);

            return pStmt.executeUpdate() > 0;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }


}
