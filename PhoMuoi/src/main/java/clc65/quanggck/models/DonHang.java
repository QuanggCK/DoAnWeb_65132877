package clc65.quanggck.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "don_hang")
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

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

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL)
    private List<CtDonHang> dsChiTietDonHang;

    // ----- CONSTRUCTORS -----
    public DonHang() {
    }

    public DonHang(Integer orderId, User user, LocalDateTime ngayDat, Long tongGia, String trangThai, String soDienThoaiNhan, String ghiChu, List<CtDonHang> dsChiTietDonHang) {
        this.orderId = orderId;
        this.user = user;
        this.ngayDat = ngayDat;
        this.tongGia = tongGia;
        this.trangThai = trangThai;
        this.soDienThoaiNhan = soDienThoaiNhan;
        this.ghiChu = ghiChu;
        this.dsChiTietDonHang = dsChiTietDonHang;
    }

    // ----- GETTERS AND SETTERS -----
    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public LocalDateTime getNgayDat() { return ngayDat; }
    public void setNgayDat(LocalDateTime ngayDat) { this.ngayDat = ngayDat; }

    public Long getTongGia() { return tongGia; }
    public void setTongGia(Long tongGia) { this.tongGia = tongGia; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getSoDienThoaiNhan() { return soDienThoaiNhan; }
    public void setSoDienThoaiNhan(String soDienThoaiNhan) { this.soDienThoaiNhan = soDienThoaiNhan; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public List<CtDonHang> getDsChiTietDonHang() { return dsChiTietDonHang; }
    public void setDsChiTietDonHang(List<CtDonHang> dsChiTietDonHang) { this.dsChiTietDonHang = dsChiTietDonHang; }
}