package model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NhanVien {
    private int id;
    private String tenNv;
    private String sdt;
    private String tenDangNhap;
    private String matKhau;
    private String vaiTro;
    private boolean trangThai;
}
