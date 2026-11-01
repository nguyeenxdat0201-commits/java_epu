package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KhuyenMai {
    private int id;
    private String maKM;
    private String tenChuongTrinh;
    private double phanTramGiam;
    private double soTienGiam;
    private Date ngayBatDau;
    private Date ngayKeThuc;
    private Boolean trangThai;
}
