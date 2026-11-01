package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HoaDon {
    private int id;
    private int idBan;
    private int idNhanVien;
    private int idKhachHang;
    private int idKhuyenMai;
    private LocalDateTime ngaytao;
    private double tienGiam;
    private double tongTien;
    private String phuongThucTT;
    private String trangThai;
}
