package clc65.quanggck.services;

import clc65.quanggck.models.QuangCao;
import clc65.quanggck.repos.QuangCaoRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuangCaoService {

    private final QuangCaoRepository quangCaoRepository;

    public QuangCaoService(QuangCaoRepository quangCaoRepository) {
        this.quangCaoRepository = quangCaoRepository;
    }

    public List<QuangCao> getAllQuangCao() {
        return quangCaoRepository.findAll();
    }

    // TỰ ĐỘNG LỌC KHUYẾN MÃI THEO THỜI GIAN HIỆN TẠI
    public List<QuangCao> getQuangCaoDangBat() {
        List<QuangCao> dsTatCa = quangCaoRepository.findByTrangThaiTrue();
        LocalDateTime bayGio = LocalDateTime.now();

        return dsTatCa.stream().filter(qc -> {
            // Nếu không cài đặt ngày bắt đầu hoặc ngày kết thúc, mặc định hiển thị theo trang_thai
            if (qc.getNgayKm() == null || qc.getNgayEndkm() == null) {
                return true;
            }
            // Điều kiện: Thời gian hiện tại phải nằm từ ngày bắt đầu đến trước ngày kết thúc
            return !bayGio.isBefore(qc.getNgayKm()) && !bayGio.isAfter(qc.getNgayEndkm());
        }).collect(Collectors.toList());
    }

    public QuangCao saveQuangCao(QuangCao quangCao) {
        return quangCaoRepository.save(quangCao);
    }

    public void deleteQuangCao(Integer id) {
        quangCaoRepository.deleteById(id);
    }
}