package model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DanhMuc {
    private int id;
    private String tenDanhMuc;

    @Override
    public String toString() {
        return "ID: " + this.id + " | " + "Món: " + this.tenDanhMuc;
    }
}
