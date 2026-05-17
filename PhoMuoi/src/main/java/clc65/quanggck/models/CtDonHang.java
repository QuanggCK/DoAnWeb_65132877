package clc65.quanggck.models;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ct_donhang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Integer detailId;

    // Nhiều chi tiết thuộc về cùng 1 Đơn hàng
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @JsonIgnore // Tránh lặp: DonHang -> CtDonHang -> DonHang
    private DonHang donHang; // Thay thế cho Integer orderId cũ

    // Nhiều chi tiết có thể chứa cùng 1 Món ăn giống nhau ở các đơn khác nhau
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private MonAn monAn; // Thay thế cho Integer productId cũ

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "gia")
    private Long gia;
}