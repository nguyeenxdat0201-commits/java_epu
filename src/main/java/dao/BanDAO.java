package dao;

import connectdatabase.DBConnect;
import model.Ban;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Predicate;

public class BanDAO {

//    public static void main(String[] args) {
//        BanDAO banDAO = new BanDAO();
//
//        for (Ban ban : banDAO.getAllBan()){
//            System.out.println(ban.getTenBan());
//            System.out.println(ban.getTrangThai());
//        }
//    }

    public List<Ban> getAllBan() {
        List<Ban> listban = new ArrayList<>();

        String sql = "SELECT * FROM ban";

        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql);
                ResultSet rs = pStmt.executeQuery();
                ){
            while (rs.next()){
                Ban ban = new Ban(
                        rs.getInt("id"),
                        rs.getString("ten_ban"),
                        rs.getString("khu_vuc"),
                        rs.getString("trang_thai")
                );
                listban.add(ban);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return listban;
    }

    public boolean insertBan(Ban ban) {
        String sql = "INSERT INTO ban (ten_ban, khu_vuc, trang_thai) VALUES (?,?,?)";

        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql);
                ){
            pStmt.setString(1, ban.getTenBan());
            pStmt.setString(2, ban.getKhuVuc());
            pStmt.setString(3, ban.getTrangThai());

            return pStmt.executeUpdate() > 0;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean updateBan(Ban ban) {
        String sql = "UPDATE ban SET ten_ban = ?, khu_vuc = ?, trang_thai= ? WHERE id = ?";

        try (
                Connection connection = DBConnect.getConnection();
                PreparedStatement pStmt = connection.prepareStatement(sql);
                ){
            pStmt.setString(1, ban.getTenBan());
            pStmt.setString(2, ban.getKhuVuc());
            pStmt.setString(3, ban.getTrangThai());
            pStmt.setInt(4, ban.getId());

            return pStmt.executeUpdate() > 0;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean deleteBan(int id) {
        String sql = "DELETE FROM ban WHERE id = ?";

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
