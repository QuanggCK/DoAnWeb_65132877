package clc65.quanggck.services;

import clc65.quanggck.models.DanhMuc;
import clc65.quanggck.repos.DanhMucRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DanhMucService {

    private final DanhMucRepository danhMucRepository;

    public DanhMucService(DanhMucRepository danhMucRepository) {
        this.danhMucRepository = danhMucRepository;
    }

    public List<DanhMuc> getAllDanhMuc() {
        return danhMucRepository.findAll();
    }

    public DanhMuc getDanhMucById(Integer id) {
        return danhMucRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục này!"));
    }

    public DanhMuc saveDanhMuc(DanhMuc danhMuc) {
        return danhMucRepository.save(danhMuc);
    }

    public void deleteDanhMuc(Integer id) {
        danhMucRepository.deleteById(id);
    }
}