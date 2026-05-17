package clc65.quanggck.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "don_hang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    // Nhiều đơn hàng thuộc về 1 người dùng (Khách hàng)
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user; // Thay thế cho Integer userId cũ

    @Column(name = "ngay_dat")
    private LocalDateTime ngayDat;

    @Column(name = "tong_gia")
    private Long tongGia;

    @Column(name = "trang_thai", length = 20)
    private String trangThai;

    @Column(name = "so_dien_thoai_nhan", length = 15)
    private String soDienThoaiNhan;

    @Column(name = "ghi_chu", length = 255)
    private String ghiChu;

    // 1 đơn hàng sẽ chứa một danh sách các món ăn chi tiết bên trong
    // KHÔNG dùng @JsonIgnore ở đây để khi gọi API đơn hàng, nó tự động trả về toàn bộ món đi kèm
    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL)
    private List<CtDonHang> dsChiTietDonHang; 
}