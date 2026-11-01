package gui;

import model.NhanVien;

public class AuthSession {
    // Biến static lưu nhân viên đang đăng nhập trên toàn hệ thống
    public static NhanVien currentUser = null;

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void logout() {
        currentUser = null;
    }
}