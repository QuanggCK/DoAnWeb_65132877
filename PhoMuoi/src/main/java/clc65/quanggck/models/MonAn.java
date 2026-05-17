package clc65.quanggck.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mon_an")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonAn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId; //

    // Tạm thời lưu dưới dạng Integer. Sau này khi làm quan hệ giữa các bảng, 
    // chúng ta sẽ đổi thành @ManyToOne nối với class DanhMuc sau.
    @Column(name = "id_danh_muc")
    private Integer idDanhMuc; //

    @Column(name = "ten_mon", length = 255)
    private String tenMon; //

    @Column(name = "price")
    private Long price; //

    @Column(name = "ingredients", columnDefinition = "TEXT")
    private String ingredients; //

    @Column(name = "image_mon", length = 255)
    private String imageMon; //

    @Column(name = "trang_thai")
    private Boolean trangThai; //
}