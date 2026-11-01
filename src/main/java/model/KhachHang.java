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
public class KhachHang {
    private int id;
    private String teKh;
    private String sdt;
    private int diemTichLuy;
    private LocalDateTime ngayTao;
}
