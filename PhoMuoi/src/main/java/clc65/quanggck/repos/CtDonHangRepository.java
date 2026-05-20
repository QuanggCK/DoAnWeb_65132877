package clc65.quanggck.repos;

import clc65.quanggck.models.CtDonHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CtDonHangRepository extends JpaRepository<CtDonHang, Integer> {
    List<CtDonHang> findByDonHang_OrderId(Integer orderId);
}