package clc65.quanggck.services;

import clc65.quanggck.models.MonAn;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import clc65.quanggck.repos.MonAnRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MonAnService {

    private final MonAnRepository monAnRepository;

    public MonAnService(MonAnRepository monAnRepository) {
        this.monAnRepository = monAnRepository;
    }
    public List<MonAn> getAllMonAn() {
        return monAnRepository.findAll();
    }



    public List<MonAn> getMonAnByDanhMuc(Integer idDanhMuc) {
    	return monAnRepository.findByDanhMuc_IdAndTrangThaiTrue(idDanhMuc);
    }

    public MonAn getMonAnById(Integer id) {
        return monAnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy món ăn này!"));
    }

    public MonAn saveMonAn(MonAn monAn) {
        return monAnRepository.save(monAn);
    }
    public Page<MonAn> getMonAnDangBan(Pageable pageable) {
        return monAnRepository.findByTrangThaiTrue(pageable);
    }

    public void deleteMonAn(Integer id) {
        monAnRepository.deleteById(id);
    }
}