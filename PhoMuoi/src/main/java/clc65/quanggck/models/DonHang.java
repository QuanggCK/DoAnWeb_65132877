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

    // ----- BỔ SUNG 3 TRƯỜNG MỚI THEO CSDL -----
    @Column(name = "ten_nguoi_nhan", length = 100)
    private String tenNguoiNhan;

    @Column(name = "dia_chi_nhan", length = 255)
    private String diaChiNhan;

    @Column(name = "phuong_thuc_thanh_toan", length = 50)
    private String phuongThucThanhToan;
    // ------------------------------------------

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL)
    private List<CtDonHang> dsChiTietDonHang;

    // ----- CONSTRUCTORS -----
    public DonHang() {
    }

    public DonHang(Integer orderId, User user, LocalDateTime ngayDat, Long tongGia, String trangThai, 
                   String soDienThoaiNhan, String ghiChu, String tenNguoiNhan, String diaChiNhan, 
                   String phuongThucThanhToan, List<CtDonHang> dsChiTietDonHang) {
        this.orderId = orderId;
        this.user = user;
        this.ngayDat = ngayDat;
        this.tongGia = tongGia;
        this.trangThai = trangThai;
        this.soDienThoaiNhan = soDienThoaiNhan;
        this.ghiChu = ghiChu;
        this.tenNguoiNhan = tenNguoiNhan;
        this.diaChiNhan = diaChiNhan;
        this.phuongThucThanhToan = phuongThucThanhToan;
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

    // --- GETTERS/SETTERS CHO 3 TRƯỜNG MỚI ---
    public String getTenNguoiNhan() { return tenNguoiNhan; }
    public void setTenNguoiNhan(String tenNguoiNhan) { this.tenNguoiNhan = tenNguoiNhan; }

    public String getDiaChiNhan() { return diaChiNhan; }
    public void setDiaChiNhan(String diaChiNhan) { this.diaChiNhan = diaChiNhan; }

    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(String phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }
    // ----------------------------------------

    public List<CtDonHang> getDsChiTietDonHang() { return dsChiTietDonHang; }
    public void setDsChiTietDonHang(List<CtDonHang> dsChiTietDonHang) { this.dsChiTietDonHang = dsChiTietDonHang; }
}