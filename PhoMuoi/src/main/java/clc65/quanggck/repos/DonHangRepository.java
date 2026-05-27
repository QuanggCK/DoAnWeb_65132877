package clc65.quanggck.repos;

import clc65.quanggck.models.DonHang;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DonHangRepository extends JpaRepository<DonHang, Integer> {
    
	List<DonHang> findByUser_UserIdOrderByOrderIdDesc(Integer userId);
	Optional<DonHang> findByUser_UserIdAndTrangThai(Integer userId, String trangThai);
}