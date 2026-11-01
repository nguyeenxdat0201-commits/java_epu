package dao;

import connectdatabase.DBConnect;
import model.Mon;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MonDAO {

    public List<Mon> getAllMon() {
        List<Mon> listmon = new ArrayList<>();

        String sql = "SELECT * FROM mon";
        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql);
                ResultSet rs = pStmt.executeQuery();
                ){
            while (rs.next()) {
                Mon mon = new Mon(
                        rs.getInt("id"),
                        rs.getString("ten_mon"),
                        rs.getInt("id_danh_muc"),
                        rs.getDouble("gia_tien"),
                        rs.getString("hinh_anh"),
                        rs.getString("trang_thai")
                );
                listmon.add(mon);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return listmon;
    }

    public boolean insertMon (Mon mon) {
        String sql = "INSERT INTO mon (ten_mon, id_danh_muc, gia_tien, hinh_anh, trang_thai) VALUES (?,?,?,?,?)";

        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql);
                ){
            pStmt.setString(1, mon.getTenMon());
            pStmt.setInt(2, mon.getIdDanhMuc());
            pStmt.setDouble(3, mon.getDonGiaCoBan());
            pStmt.setString(4, mon.getHinhAnh());
            pStmt.setString(5, mon.getTrangThai());

            return pStmt.executeUpdate() > 0;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public List<Mon> getMonByDanhMuc(int iddanhmuc) {
        List<Mon> listmon = new ArrayList<>();

        String sql = "SELECT * FROM mon WHERE id_danh_muc = ? AND trang_thai = 1";

        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql);

                ){
            pStmt.setInt(1, iddanhmuc);

            try (ResultSet rs = pStmt.executeQuery()){
                while (rs.next()){
                    Mon mon = new Mon(
                            rs.getInt("id"),
                            rs.getString("ten_mon"),
                            rs.getInt("id_danh_muc"),
                            rs.getDouble("gia_tien"),
                            rs.getString("hinh_anh"),
                            rs.getString("trang_thai")
                    );
                    listmon.add(mon);
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return listmon;
    }

    public boolean updateMon(Mon mon) {
        String sql = "UPDATE mon SET ten_mon = ?, id_danh_muc = ?, gia_tien = ?, hinh_anh = ?, " +
                "trang_thai = ? WHERE id = ?";
        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql);
                ){
            pStmt.setString(1, mon.getTenMon());
            pStmt.setInt(2, mon.getIdDanhMuc());
            pStmt.setDouble(3, mon.getDonGiaCoBan());
            pStmt.setString(4, mon.getHinhAnh());
            pStmt.setString(5, mon.getTrangThai());
            pStmt.setInt(6, mon.getId());

            return pStmt.executeUpdate() > 0;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean deleteMon(int id) {
         String sql = "DELETE FROM mon WHERE id = ?";

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
