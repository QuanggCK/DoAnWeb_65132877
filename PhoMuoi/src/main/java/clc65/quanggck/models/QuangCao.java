package clc65.quanggck.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "quang_cao")
public class QuangCao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tieu_de", nullable = false, length = 200)
    private String tieuDe;

    @Column(name = "noi_dung", columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "hinh_anh", length = 255)
    private String hinhAnh;

    @Column(name = "trang_thai")
    private Boolean trangThai = true; // true: đang hiển thị banner, false: ẩn đi

    // --- THUỘC TÍNH MỚI THÊM ---
    @Column(name = "ngay_km")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") // Hỗ trợ nhận dữ liệu từ thẻ <input type="datetime-local">
    private LocalDateTime ngayKm;

    @Column(name = "ngay_endkm")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime ngayEndkm;

    // --- GETTERS / SETTERS ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public Boolean getTrangThai() { return trangThai; }
    public void setTrangThai(Boolean trangThai) { this.trangThai = trangThai; }

    // Getter/Setter cho thuộc tính mới
    public LocalDateTime getNgayKm() { return ngayKm; }
    public void setNgayKm(LocalDateTime ngayKm) { this.ngayKm = ngayKm; }

    public LocalDateTime getNgayEndkm() { return ngayEndkm; }
    public void setNgayEndkm(LocalDateTime ngayEndkm) { this.ngayEndkm = ngayEndkm; }
}