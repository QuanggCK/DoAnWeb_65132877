package clc65.quanggck.repos;

import clc65.quanggck.models.CtDonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CtDonHangRepository extends JpaRepository<CtDonHang, Integer> {
    // Thường dùng hàm save() mặc định khi duyệt vòng lặp để lưu chi tiết đơn hàng
}