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
@Table(name = "quang_cao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuangCao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_id")
    private Integer adId; //

    @Column(name = "title", length = 255)
    private String title; //

    @Column(name = "image", length = 255)
    private String image; //


    @Column(name = "content", length = 255)
    private String content; //

    @Column(name = "trang_thai")
    private Boolean trangThai; //
}