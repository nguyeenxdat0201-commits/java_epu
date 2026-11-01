package dao;

import connectdatabase.DBConnect;
import model.DanhMuc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DanhMucDAO {
    public static void main(String[] args) {
        DanhMucDAO dao = new DanhMucDAO();

        // 1. Test lấy danh sách
        System.out.println("=== DANH SÁCH DANH MỤC ===");
        for (DanhMuc dm : dao.getAllDanhMuc()) {
            System.out.println(dm.getId() + " - " + dm.getTenDanhMuc());
        }
    }

    public List<DanhMuc> getAllDanhMuc() {
        List<DanhMuc> list = new ArrayList<>();

        String sql = "SELECT * FROM danhmuc";

        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql);
                ResultSet rs = pStmt.executeQuery();
        ){
            while (rs.next()) {
                DanhMuc dm = new DanhMuc(
                        rs.getInt("id"),
                        rs.getString("ten_danh_muc")
                );
                list.add(dm);
            };

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return list;
    }

    public boolean themDanhMuc(DanhMuc danhMuc){
        String sql = "INSERT INTO danhmuc (ten_danh_muc) VALUES (?)";

        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql);
                ){
            pStmt.setString(1, danhMuc.getTenDanhMuc());

            return pStmt.executeUpdate() > 0;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean updateDanhMuc(DanhMuc danhMuc) {
        String sql = "UPDATE danhmuc SET ten_danh_muc =? WHERE id = ?";
        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql);
                ){
            pStmt.setString(1, danhMuc.getTenDanhMuc());
            pStmt.setInt(2, danhMuc.getId());

            return pStmt.executeUpdate() > 0;

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean deleteDanhMuc(int id) {
        String sql = "DELETE FROM danhmuc WHERE id = ?";
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
