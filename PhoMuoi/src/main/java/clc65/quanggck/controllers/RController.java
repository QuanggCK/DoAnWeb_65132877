package clc65.quanggck.controllers;

import clc65.quanggck.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/check")
@CrossOrigin("*")
public class RController {

    private final UserService userService;
    private final MonAnService monAnService;
    private final DonHangService donHangService;

    public RController(UserService userService, MonAnService monAnService, DonHangService donHangService) {
        this.userService = userService;
        this.monAnService = monAnService;
        this.donHangService = donHangService;
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> checkSystemStatus() {
        Map<String, Object> statusReport = new HashMap<>();
        
        try { 
            int totalUsers = userService.getAllUsers().size();
            int totalDishes = monAnService.getAllMonAn().size();
            int totalOrders = donHangService.getAllDonHang().size();

            statusReport.put("he_thong", "HOẠT ĐỘNG TỐT (OK)");
            statusReport.put("ket_noi_database", "THÀNH CÔNG");
            statusReport.put("so_luong_khach_hang", totalUsers);
            statusReport.put("so_luong_mon_an", totalDishes);
            statusReport.put("so_luong_don_hang", totalOrders);
            
            return ResponseEntity.ok(statusReport);
        } catch (Exception e) { 
            statusReport.put("he_thong", "LỖI KẾT NỐI");
            statusReport.put("chi_tiet_loi", e.getMessage());
            return ResponseEntity.status(500).body(statusReport);
        }
    } 
} 