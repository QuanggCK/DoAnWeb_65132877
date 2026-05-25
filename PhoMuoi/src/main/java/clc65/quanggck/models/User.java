package clc65.quanggck.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "users")
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

    // --- THÊM THUỘC TÍNH ANH VÀO SAU MAT_KHAU ---
    @Column(name = "anh", length = 255)
    private String anh;

    // 1 người dùng có thể đặt nhiều đơn hàng
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DonHang> dsDonHang;

    // ----- CONSTRUCTORS -----
    public User() {
    }

 // 2. Constructor đầy đủ các thuộc tính cơ bản (Giúp Hibernate map dữ liệu chuẩn 100%)
    public User(Integer userId, String tenKhach, String sdt, String diaChi, Boolean roleAdmin, String matKhau, String anh) {
        this.userId = userId;
        this.tenKhach = tenKhach;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.roleAdmin = roleAdmin;
        this.matKhau = matKhau;
        this.anh = anh;
    }

    // 3. Constructor bao gồm cả danh sách đơn hàng (Nếu cần dùng ở nơi khác trong dự án)
    public User(Integer userId, String tenKhach, String sdt, String diaChi, Boolean roleAdmin, String matKhau, String anh, List<DonHang> dsDonHang) {
        this.userId = userId;
        this.tenKhach = tenKhach;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.roleAdmin = roleAdmin;
        this.matKhau = matKhau;
        this.anh = anh;
        this.dsDonHang = dsDonHang;
    }

    // ----- GETTERS AND SETTERS -----
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getTenKhach() {
        return tenKhach;
    }

    public void setTenKhach(String tenKhach) {
        this.tenKhach = tenKhach;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public Boolean getRoleAdmin() {
        return roleAdmin;
    }

    public void setRoleAdmin(Boolean roleAdmin) {
        this.roleAdmin = roleAdmin;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    // --- THÊM GETTER VÀ SETTER CHO ANH Ở ĐÂY ---
    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public List<DonHang> getDsDonHang() {
        return dsDonHang;
    }

    public void setDsDonHang(List<DonHang> dsDonHang) {
        this.dsDonHang = dsDonHang;
    }
}