package clc65.quanggck.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ct_donhang")
public class CtDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Integer detailId;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @JsonIgnore
    private DonHang donHang;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private MonAn monAn;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "gia")
    private Long gia;

    // ----- CONSTRUCTORS -----
    public CtDonHang() {
    }

    public CtDonHang(Integer detailId, DonHang donHang, MonAn monAn, Integer quantity, Long gia) {
        this.detailId = detailId;
        this.donHang = donHang;
        this.monAn = monAn;
        this.quantity = quantity;
        this.gia = gia;
    }

    // ----- GETTERS AND SETTERS -----
    public Integer getDetailId() { return detailId; }
    public void setDetailId(Integer detailId) { this.detailId = detailId; }

    public DonHang getDonHang() { return donHang; }
    public void setDonHang(DonHang donHang) { this.donHang = donHang; }

    public MonAn getMonAn() { return monAn; }
    public void setMonAn(MonAn monAn) { this.monAn = monAn; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Long getGia() { return gia; }
    public void setGia(Long gia) { this.gia = gia; }
}