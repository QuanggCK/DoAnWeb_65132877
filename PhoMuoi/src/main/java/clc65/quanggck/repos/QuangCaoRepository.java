package clc65.quanggck.repos;

import clc65.quanggck.models.QuangCao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface QuangCaoRepository extends JpaRepository<QuangCao, Integer> {
    List<QuangCao> findByTrangThaiTrue();
}