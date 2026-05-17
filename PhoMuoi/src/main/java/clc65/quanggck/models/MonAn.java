package clc65.quanggck.models;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "mon_an")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonAn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    // Nhiều món ăn thuộc về 1 danh mục
    @ManyToOne
    @JoinColumn(name = "id_danh_muc", referencedColumnName = "id_danh_muc")
    private DanhMuc danhMuc; // Thay thế cho Integer idDanhMuc cũ

    @Column(name = "ten_mon", length = 255)
    private String tenMon;

    @Column(name = "price")
    private Long price;

    @Column(name = "ingredients", columnDefinition = "TEXT")
    private String ingredients;

    @Column(name = "image_mon", length = 255)
    private String imageMon;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    // 1 món ăn có thể xuất hiện trong nhiều Chi tiết đơn hàng
    @OneToMany(mappedBy = "monAn", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CtDonHang> dsChiTietDonHang;
}