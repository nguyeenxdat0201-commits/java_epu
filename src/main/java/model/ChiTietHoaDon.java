package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChiTietHoaDon {
    private int idHoaDon;
    private int idMon;
    private String size;
    private double phuPhiSize;
    private int soLuong;
    private double donGia;
    private String ghiChu;


}
