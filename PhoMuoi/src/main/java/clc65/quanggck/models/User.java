package clc65.quanggck.models;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "ten_khach", length = 255)
    private String tenKhach;

    @Column(name = "sdt", length = 10, unique = true)
    private String sdt;

    @Column(name = "dia_chi", length = 255)
    private String diaChi;

    @Column(name = "role_admin")
    private Boolean roleAdmin;

    @Column(name = "mat_khau", length = 255)
    private String matKhau;

    // 1 người dùng có thể đặt nhiều đơn hàng
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DonHang> dsDonHang;
}