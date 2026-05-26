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

    // Constructor thủ công
    public AdminController(MonAnService monAnService, DanhMucService danhMucService,
                           DonHangService donHangService, UserService userService,
                           QuangCaoService quangCaoService) {
        this.monAnService = monAnService;
        this.danhMucService = danhMucService;
        this.donHangService = donHangService;
        this.userService = userService;
        this.quangCaoService = quangCaoService;
    }

    // ----- QUẢN LÝ DANH MỤC -----
    @PostMapping("/danh-muc")
    public ResponseEntity<DanhMuc> saveDanhMuc(@RequestBody DanhMuc danhMuc) {
        return ResponseEntity.ok(danhMucService.saveDanhMuc(danhMuc));
    }

    @DeleteMapping("/danh-muc/{id}")
    public ResponseEntity<String> deleteDanhMuc(@PathVariable Integer id) {
        danhMucService.deleteDanhMuc(id);
        return ResponseEntity.ok("Xóa danh mục thành công!");
    }

    // ----- QUẢN LÝ MÓN ĂN -----
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

    // ----- QUẢN LÝ ĐƠN HÀNG -----
    @GetMapping("/don-hang")
    public ResponseEntity<List<DonHang>> getAllDonHang() {
        return ResponseEntity.ok(donHangService.getAllDonHang());
    }
    
 // ----- Thêm vào cụm QUẢN LÝ QUẢNG CÁO trong AdminController.java -----

 // ----- Thêm vào cụm QUẢN LÝ QUẢNG CÁO trong AdminController.java -----

 // API 1: Lấy chi tiết một quảng cáo theo ID để đổ dữ liệu cũ lên Form sửa
 // Đường dẫn gọi dữ liệu thực tế sẽ là: /api/admin/quang-cao/{id}
 @GetMapping("/quang-cao/{id}")
 public ResponseEntity<QuangCao> getQuangCaoById(@PathVariable Integer id) {
     return ResponseEntity.ok(quangCaoService.getQuangCaoById(id));
 }

 // API 2: Nhận dữ liệu cập nhật từ form gửi lên qua phương thức PUT
 // Đường dẫn thực tế: /api/admin/quang-cao/{id}
 @PutMapping("/quang-cao/{id}")
 public ResponseEntity<QuangCao> updateQuangCao(@PathVariable Integer id, @RequestBody QuangCao quangCaoMoi) {
     // Tìm quảng cáo cũ trong DB
     QuangCao qcOld = quangCaoService.getQuangCaoById(id);
     
     // Tiến hành cập nhật các trường thông tin mới
     qcOld.setTieuDe(quangCaoMoi.getTieuDe());
     qcOld.setNoiDung(quangCaoMoi.getNoiDung());
     qcOld.setHinhAnh(quangCaoMoi.getHinhAnh());
     qcOld.setTrangThai(quangCaoMoi.getTrangThai());
     
     // Lưu đè lại vào cơ sở dữ liệu
     return ResponseEntity.ok(quangCaoService.saveQuangCao(qcOld));
 }

    @PutMapping("/don-hang/{orderId}/trang-thai")
    public ResponseEntity<DonHang> updateTrangThaiDonHang(@PathVariable Integer orderId, @RequestParam String trangThaiMoi) {
        return ResponseEntity.ok(donHangService.updateTrangThai(orderId, trangThaiMoi));
    }

    // ----- QUẢN LÝ KHÁCH HÀNG -----
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ----- QUẢN LÝ QUẢNG CÁO -----
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