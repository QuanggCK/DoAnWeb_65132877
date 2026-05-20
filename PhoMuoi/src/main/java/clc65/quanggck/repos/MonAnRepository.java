package clc65.quanggck.repos;

import clc65.quanggck.models.MonAn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MonAnRepository extends JpaRepository<MonAn, Integer> {

    List<MonAn> findByDanhMucId(Integer idDanhMuc);
    List<MonAn> findByTrangThaiTrue();
}