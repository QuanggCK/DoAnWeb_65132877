package clc65.quanggck.controllers;

import clc65.quanggck.models.*;
import clc65.quanggck.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin("*")
public class AdminController {

    private final MonAnService monAnService;
    private final DanhMucService danhMucService;
    private final DonHangService donHangService;
    private final UserService userService;
    private final QuangCaoService quangCaoService;


    public AdminController(MonAnService monAnService, DanhMucService danhMucService,
                           DonHangService donHangService, UserService userService,
                           QuangCaoService quangCaoService) {
        this.monAnService = monAnService;
        this.danhMucService = danhMucService;
        this.donHangService = donHangService;
        this.userService = userService;
        this.quangCaoService = quangCaoService;
    }


    @PostMapping("/danh-muc")
    public ResponseEntity<DanhMuc> saveDanhMuc(@RequestBody DanhMuc danhMuc) {
        return ResponseEntity.ok(danhMucService.saveDanhMuc(danhMuc));
    }

    @DeleteMapping("/danh-muc/{id}")
    public ResponseEntity<String> deleteDanhMuc(@PathVariable Integer id) {
        danhMucService.deleteDanhMuc(id);
        return ResponseEntity.ok("Xóa danh mục thành công!");
    }


    @GetMapping("/mon-an")
    public ResponseEntity<List<MonAn>> getAllMonAn() {
        return ResponseEntity.ok(monAnService.getAllMonAn());
    }

    @PostMapping("/mon-an")
    public ResponseEntity<MonAn> saveMonAn(@RequestBody MonAn monAn) {
        return ResponseEntity.ok(monAnService.saveMonAn(monAn));
    }

    @DeleteMapping("/mon-an/{id}")
    public ResponseEntity<String> deleteMonAn(@PathVariable Integer id) {
        monAnService.deleteMonAn(id);
        return ResponseEntity.ok("Xóa món ăn thành công!");
    }


    @GetMapping("/don-hang")
    public ResponseEntity<List<DonHang>> getAllDonHang() {
        return ResponseEntity.ok(donHangService.getAllDonHang());
    }

    @PutMapping("/don-hang/{orderId}/trang-thai")
    public ResponseEntity<DonHang> updateTrangThaiDonHang(@PathVariable Integer orderId, @RequestParam String trangThaiMoi) {
        return ResponseEntity.ok(donHangService.updateTrangThai(orderId, trangThaiMoi));
    }


    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }


    @GetMapping("/quang-cao")
    public ResponseEntity<List<QuangCao>> getAllQuangCao() {
        return ResponseEntity.ok(quangCaoService.getAllQuangCao());
    }

    @PostMapping("/quang-cao")
    public ResponseEntity<QuangCao> saveQuangCao(@RequestBody QuangCao quangCao) {
        return ResponseEntity.ok(quangCaoService.saveQuangCao(quangCao));
    }

    @DeleteMapping("/quang-cao/{id}")
    public ResponseEntity<String> deleteQuangCao(@PathVariable Integer id) {
        quangCaoService.deleteQuangCao(id);
        return ResponseEntity.ok("Xóa quảng cáo thành công!");
    }
}