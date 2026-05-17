package clc65.quanggck.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "don_hang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId; //

    // Tạm thời để Integer, sau này làm liên kết khóa ngoại sẽ đổi thành đối tượng User sau
    @Column(name = "user_id")
    private Integer userId; //

    @Column(name = "ngay_dat")
    private LocalDateTime ngayDat; //

    @Column(name = "tong_gia")
    private Long tongGia; //

    @Column(name = "trang_thai", length = 20)
    private String trangThai; //

    @Column(name = "so_dien_thoai_nhan", length = 15)
    private String soDienThoaiNhan; //

    @Column(name = "ghi_chu", length = 255)
    private String ghiChu; //
}