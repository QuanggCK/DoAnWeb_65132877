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
@Table(name = "users")
@Data                // Tự động tạo tất cả Getter, Setter, toString, equals, hashCode
@NoArgsConstructor   // Tự động tạo Constructor không tham số (mặc định)
@AllArgsConstructor  // Tự động tạo Constructor có đầy đủ tất cả tham số
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId; //

    @Column(name = "ten_khach", length = 255)
    private String tenKhach; //

    @Column(name = "sdt", length = 10, unique = true)
    private String sdt; //

    @Column(name = "dia_chi", length = 255)
    private String diaChi; //

    @Column(name = "role_admin")
    private Boolean roleAdmin; //

    @Column(name = "mat_khau", length = 255)
    private String matKhau; //
}