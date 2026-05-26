package clc65.quanggck.repos;

import clc65.quanggck.models.MonAn;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import java.util.List;

@Repository
public interface MonAnRepository extends JpaRepository<MonAn, Integer> {

	List<MonAn> findByDanhMuc_IdAndTrangThaiTrue(Integer idDanhMuc);
    Page<MonAn> findByTrangThaiTrue(Pageable pageable);
    
}