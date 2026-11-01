package model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mon {
    private int id;
    private String tenMon;
    private int idDanhMuc;
    private double donGiaCoBan;
    private String hinhAnh;
    private String trangThai;
}
