package clc65.quanggck.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "mon_an")
public class MonAn {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten_mon", nullable = false, length = 150)
    private String tenMon;

    @Column(name = "gia_tien", nullable = false)
    private Double giaTien;

    @Column(name = "hinh_anh", length = 255)
    private String hinhAnh;

    @Column(name = "nguyen_lieu", length = 255)
    private String nguyenLieu;

    @Column(name = "trang_thai")
    private Boolean trangThai = true; // true: đang bán, false: ngừng bán

    // NHIỀU món ăn thuộc VỀ MỘT danh mục (ManyToOne)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "danh_muc_id", nullable = false)
    @JsonIgnoreProperties("dsMonAn")
    private DanhMuc danhMuc;

    // --- GETTERS / SETTERS ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTenMon() { return tenMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }

    public Double getGiaTien() { return giaTien; }
    public void setGiaTien(Double giaTien) { this.giaTien = giaTien; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    // GETTER / SETTER CHO NGUYÊN LIỆU
    public String getNguyenLieu() { return nguyenLieu; }
    public void setNguyenLieu(String nguyenLieu) { this.nguyenLieu = nguyenLieu; }

    public Boolean getTrangThai() { return trangThai; }
    public void setTrangThai(Boolean trangThai) { this.trangThai = trangThai; }

    public DanhMuc getDanhMuc() { return danhMuc; }
    public void setDanhMuc(DanhMuc danhMuc) { this.danhMuc = danhMuc; }
}