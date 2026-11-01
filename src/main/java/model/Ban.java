package model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Ban {
    private int id;
    private String tenBan;
    private String khuVuc;
    private String trangThai;

    @Override
    public String toString() {
        return "Bàn: " + this.tenBan + " | " + "Khu vực: " + this.khuVuc + " | " + "Trạng Thái: " + this.trangThai;
    }
}
