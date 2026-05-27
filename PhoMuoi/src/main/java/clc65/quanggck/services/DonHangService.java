package clc65.quanggck.services;

import clc65.quanggck.models.DonHang;
import clc65.quanggck.models.CtDonHang;
import clc65.quanggck.repos.DonHangRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime; 
import java.util.List;

import clc65.quanggck.models.MonAn;
import clc65.quanggck.models.User;
import java.util.ArrayList;
@Service
public class DonHangService {

    private final DonHangRepository donHangRepository;

    public DonHangService(DonHangRepository donHangRepository) {
        this.donHangRepository = donHangRepository;
    }

    public List<DonHang> getAllDonHang() {
        return donHangRepository.findAll();
    }

    public List<DonHang> getLichSuDonHang(Integer userId) {
        return donHangRepository.findByUser_UserIdOrderByOrderIdDesc(userId);
    }

    public DonHang updateTrangThai(Integer orderId, String trangThaiMoi) {
        DonHang donHang = donHangRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));
        donHang.setTrangThai(trangThaiMoi);
        return donHangRepository.save(donHang);
    }

    @Transactional
    public DonHang createDonHang(DonHang donHang) {
        donHang.setNgayDat(LocalDateTime.now()); 
        donHang.setTrangThai("Chờ xác nhận");

        if (donHang.getDsChiTietDonHang() != null) {
            for (CtDonHang ct : donHang.getDsChiTietDonHang()) {
                ct.setDonHang(donHang);
            }
        }

        return donHangRepository.save(donHang);
    }


    public int getCartSizeByUserId(Integer userId) {
        List<DonHang> danhSachDonHang = donHangRepository.findByUser_UserIdOrderByOrderIdDesc(userId);
        
        if (danhSachDonHang == null || danhSachDonHang.isEmpty()) {
            return 0;
        }

        int totalSize = 0;
        for (DonHang donHang : danhSachDonHang) {
            if ("Giỏ hàng".equalsIgnoreCase(donHang.getTrangThai()) || "Chờ xác nhận".equalsIgnoreCase(donHang.getTrangThai())) {
                
                if (donHang.getDsChiTietDonHang() != null) {
                    for (CtDonHang ct : donHang.getDsChiTietDonHang()) {
                        if (ct != null && ct.getQuantity() != null) {
                            totalSize += ct.getQuantity(); 
                        }
                    }
                }
            }
        }
        return totalSize;
    }
    
    public DonHang getGioHangHienTaiByUserId(Integer userId) {
        return donHangRepository
                .findByUser_UserIdAndTrangThai(userId, "Giỏ hàng")
                .orElse(null);
    }
    @Transactional
    public void themVaoGio(User user, MonAn monAn, Integer soLuong) {

        // Tìm đơn hàng "Giỏ hàng" hiện tại của user
        DonHang gioHang = donHangRepository
                .findByUser_UserIdAndTrangThai(user.getUserId(), "Giỏ hàng")
                .orElse(null);

        // Nếu chưa có giỏ hàng → tạo mới
        if (gioHang == null) {
            gioHang = new DonHang();
            gioHang.setUser(user);
            gioHang.setTrangThai("Giỏ hàng");
            gioHang.setNgayDat(LocalDateTime.now());
            gioHang.setTongGia(0L);
            gioHang.setDsChiTietDonHang(new ArrayList<>());
            gioHang = donHangRepository.save(gioHang);
        }

        // Kiểm tra món đã có trong giỏ chưa
        List<CtDonHang> dsChiTiet = gioHang.getDsChiTietDonHang();
        if (dsChiTiet == null) dsChiTiet = new ArrayList<>();

        CtDonHang ctHienCo = null;
        for (CtDonHang ct : dsChiTiet) {
            if (ct.getMonAn().getId().equals(monAn.getId())) {
                ctHienCo = ct;
                break;
            }
        }

        if (ctHienCo != null) {
            // Món đã có → tăng số lượng
            ctHienCo.setQuantity(ctHienCo.getQuantity() + soLuong);
        } else {
            // Món chưa có → thêm mới
            CtDonHang ctMoi = new CtDonHang();
            ctMoi.setDonHang(gioHang);
            ctMoi.setMonAn(monAn);
            ctMoi.setQuantity(soLuong);
            ctMoi.setGia(monAn.getGiaTien().longValue());
            dsChiTiet.add(ctMoi);
            gioHang.setDsChiTietDonHang(dsChiTiet);
        }

        // Tính lại tổng giá
        long tongGia = 0;
        for (CtDonHang ct : gioHang.getDsChiTietDonHang()) {
            tongGia += ct.getGia() * ct.getQuantity();
        }
        gioHang.setTongGia(tongGia);

        donHangRepository.save(gioHang);
    }
    
}