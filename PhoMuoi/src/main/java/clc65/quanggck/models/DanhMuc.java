package clc65.quanggck.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "danh_muc")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DanhMuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_danh_muc")
    private Integer idDanhMuc;

    @Column(name = "ten_danh_muc", length = 255)
    private String tenDanhMuc;

    @Column(name = "mo_ta", length = 255)
    private String moTa;

    // 1 danh mục có nhiều món ăn
    @OneToMany(mappedBy = "danhMuc", cascade = CascadeType.ALL)
    @JsonIgnore // Ngăn không cho in ngược lại danh sách món ăn gây vòng lặp vô hạn
    private List<MonAn> dsMonAn;
}