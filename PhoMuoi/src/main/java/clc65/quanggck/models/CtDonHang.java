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
@Table(name = "ct_donhang")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CtDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Integer detailId;

    // Tạm thời để Integer. Sau này khi làm quan hệ giữa các bảng (Relationships), 
    // chúng ta sẽ đổi thành @ManyToOne nối với class DonHang.
    @Column(name = "order_id")
    private Integer orderId;

    // Tương tự, sau này sẽ nối với class MonAn.
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "quantity")
    private Integer quantity;

    // Giá của món ăn tại ĐÚNG THỜI ĐIỂM khách đặt hàng (để tránh trường hợp 
    // sau này quán tăng giá phở thì lịch sử đơn hàng cũ bị nhảy giá theo).
    @Column(name = "gia")
    private Long gia; 
}