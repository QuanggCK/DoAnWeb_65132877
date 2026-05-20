package clc65.quanggck.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@Entity
@Table(name = "danh_muc")
public class DanhMuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten_danh_muc", nullable = false, length = 100)
    private String tenDanhMuc;

    // Một danh mục có NHIỀU món ăn (OneToMany)
    // JsonIgnoreProperties giúp tránh bị vòng lặp vô hạn khi xuất dữ liệu chuỗi JSON sang ReactJS
    @OneToMany(mappedBy = "danhMuc", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("danhMuc")
    private List<MonAn> dsMonAn;

    // --- GETTERS / SETTERS ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTenDanhMuc() { return tenDanhMuc; }
    public void setTenDanhMuc(String tenDanhMuc) { this.tenDanhMuc = tenDanhMuc; }

    public List<MonAn> getDsMonAn() { return dsMonAn; }
    public void setDsMonAn(List<MonAn> dsMonAn) { this.dsMonAn = dsMonAn; }
}