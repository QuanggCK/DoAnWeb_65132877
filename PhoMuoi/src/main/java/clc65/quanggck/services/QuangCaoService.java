package clc65.quanggck.services;

import clc65.quanggck.models.QuangCao;
import clc65.quanggck.repos.QuangCaoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuangCaoService {

    private final QuangCaoRepository quangCaoRepository;

    // Khởi tạo constructor thủ công để nhúng Repository
    public QuangCaoService(QuangCaoRepository quangCaoRepository) {
        this.quangCaoRepository = quangCaoRepository;
    }

    public List<QuangCao> getAllQuangCao() {
        return quangCaoRepository.findAll();
    }

    public List<QuangCao> getQuangCaoDangBat() {
        return quangCaoRepository.findByTrangThaiTrue();
    }

    public QuangCao saveQuangCao(QuangCao quangCao) {
        return quangCaoRepository.save(quangCao);
    }

    public void deleteQuangCao(Integer id) {
        quangCaoRepository.deleteById(id);
    }
}