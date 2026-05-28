package clc65.quanggck.controllers;
 
import clc65.quanggck.models.*;
import clc65.quanggck.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
 
@Controller
public class AdminController {
 
    private final MonAnService monAnService;
    private final DanhMucService danhMucService;
    private final DonHangService donHangService;
    private final UserService userService;
    private final QuangCaoService quangCaoService;
 
    // Constructor injection
    public AdminController(MonAnService monAnService,
                           DanhMucService danhMucService,
                           DonHangService donHangService,
                           UserService userService,
                           QuangCaoService quangCaoService) {
        this.monAnService   = monAnService;
        this.danhMucService = danhMucService;
        this.donHangService = donHangService;
        this.userService    = userService;
        this.quangCaoService = quangCaoService;
    }
 
    // =====================================================
    // VIEW – trả về trang HTML admin (Thymeleaf)
    // =====================================================
 
    /**
     * Trả về giao diện quản trị admin.
     * Template: src/main/resources/templates/admin/adminPage.html
     *
     * Bảo vệ: chỉ User có roleAdmin = true mới được vào.
     * - Chưa đăng nhập  → redirect /login
     * - Đăng nhập nhưng không phải admin → redirect / (trang chủ)
     */
    // Helper: kiểm tra quyền admin từ session
    private boolean isAdmin(jakarta.servlet.http.HttpSession session) {
        User u = (User) session.getAttribute("userLogin");
        return u != null && Boolean.TRUE.equals(u.getRoleAdmin());
    }
 
    @GetMapping("/admin")
    public String adminPage(jakarta.servlet.http.HttpSession session) {
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin == null)                               return "redirect:/login";
        if (!Boolean.TRUE.equals(userLogin.getRoleAdmin())) return "redirect:/";
        return "admin/adminPage";
    }
 
    @GetMapping("/admin/food")
    public String adminFoodPage(jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        return "admin/adminFood";
    }
 
    @GetMapping("/admin/danh-muc")
    public String adminDanhMucPage(jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        return "admin/adminDanhMuc";
    }
 
    @GetMapping("/admin/don-hang")
    public String adminDonHangPage(jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        return "admin/adminDonHang";
    }
 
    @GetMapping("/admin/doanh-thu")
    public String adminDoanhThuPage(jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        return "admin/adminDoanhThu";
    }
 
    @GetMapping("/admin/users")
    public String adminUsersPage(jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        return "admin/adminUsers";
    }
 
    @GetMapping("/admin/quang-cao")
    public String adminQuangCaoPage(jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        return "admin/adminQuangCao";
    }
 
    // =====================================================
    // REST API – prefix /api/admin
    // =====================================================
 
    // ----- QUẢN LÝ DANH MỤC -----
 
    @PostMapping("/api/admin/danh-muc")
    @ResponseBody
    public ResponseEntity<DanhMuc> saveDanhMuc(@RequestBody DanhMuc danhMuc) {
        return ResponseEntity.ok(danhMucService.saveDanhMuc(danhMuc));
    }
 
    @DeleteMapping("/api/admin/danh-muc/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteDanhMuc(@PathVariable Integer id) {
        danhMucService.deleteDanhMuc(id);
        return ResponseEntity.ok("Xóa danh mục thành công!");
    }
 
    // ----- QUẢN LÝ MÓN ĂN -----
 
 // ----- QUẢN LÝ MÓN ĂN -----
 
    // 1. Lấy danh sách TẤT CẢ món ăn
    @GetMapping("/api/admin/mon-an")
    @ResponseBody
    public ResponseEntity<List<MonAn>> getAllMonAn() {
        return ResponseEntity.ok(monAnService.getAllMonAn());
    }
 
    // 2. Lấy chi tiết MỘT món ăn theo ID (Dùng để đổ dữ liệu cũ ra Form khi bấm nút Sửa)
    @GetMapping("/api/admin/mon-an/{id}")
    @ResponseBody
    public ResponseEntity<MonAn> getMonAnById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(monAnService.getMonAnById(id));
    }
 
    // 3. THÊM MỚI một món ăn
    @PostMapping("/api/admin/mon-an")
    @ResponseBody
    public ResponseEntity<MonAn> saveMonAn(@RequestBody MonAn monAn) {
        return ResponseEntity.ok(monAnService.saveMonAn(monAn));
    }
 
    // 4. CẬP NHẬT (SỬA) món ăn đã có
 // 4. CẬP NHẬT (SỬA) món ăn đã có
    @PutMapping("/api/admin/mon-an/{id}")
    @ResponseBody
    public ResponseEntity<MonAn> updateMonAn(@PathVariable("id") Integer id, @RequestBody MonAn monAnMoi) {
        // Lấy dữ liệu món ăn cũ từ Database lên
        MonAn monAnCu = monAnService.getMonAnById(id);
        
        if (monAnCu != null) {
            // Cập nhật các trường thông tin mới (Đã khớp 100% với MonAn.java của bạn)
            monAnCu.setTenMon(monAnMoi.getTenMon());
            monAnCu.setGiaTien(monAnMoi.getGiaTien()); 
            monAnCu.setHinhAnh(monAnMoi.getHinhAnh()); 
            monAnCu.setNguyenLieu(monAnMoi.getNguyenLieu()); 
            monAnCu.setTrangThai(monAnMoi.getTrangThai());
            
            // Nếu có cập nhật danh mục:
            if(monAnMoi.getDanhMuc() != null) {
                monAnCu.setDanhMuc(monAnMoi.getDanhMuc());
            }
 
            // Lưu lại những thay đổi vào Database
            return ResponseEntity.ok(monAnService.saveMonAn(monAnCu));
        }
        
        return ResponseEntity.notFound().build(); // Trả về lỗi 404 nếu không tìm thấy món ăn
    }
 
    // 5. XÓA món ăn
    @DeleteMapping("/api/admin/mon-an/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteMonAn(@PathVariable("id") Integer id) {
        monAnService.deleteMonAn(id);
        return ResponseEntity.ok("Xóa món ăn thành công!");
    }
 
    // ----- QUẢN LÝ ĐƠN HÀNG -----
 
    @GetMapping("/api/admin/don-hang")
    @ResponseBody
    public ResponseEntity<List<DonHang>> getAllDonHang() {
        return ResponseEntity.ok(donHangService.getAllDonHang());
    }
 
    @PutMapping("/api/admin/don-hang/{orderId}/trang-thai")
    @ResponseBody
    public ResponseEntity<DonHang> updateTrangThaiDonHang(
            @PathVariable Integer orderId,
            @RequestParam String trangThaiMoi) {
        return ResponseEntity.ok(donHangService.updateTrangThai(orderId, trangThaiMoi));
    }
 
    // ----- QUẢN LÝ KHÁCH HÀNG -----
 
    @GetMapping("/api/admin/users")
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
 
    // ----- QUẢN LÝ QUẢNG CÁO -----
 
    @GetMapping("/api/admin/quang-cao")
    @ResponseBody
    public ResponseEntity<List<QuangCao>> getAllQuangCao() {
        return ResponseEntity.ok(quangCaoService.getAllQuangCao());
    }
 
    @GetMapping("/api/admin/quang-cao/{id}")
    @ResponseBody
    public ResponseEntity<QuangCao> getQuangCaoById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(quangCaoService.getQuangCaoById(id));
    }
 
    @PostMapping("/api/admin/quang-cao")
    @ResponseBody
    public ResponseEntity<QuangCao> saveQuangCao(@RequestBody QuangCao quangCao) {
        return ResponseEntity.ok(quangCaoService.saveQuangCao(quangCao));
    }
 
    @PutMapping("/api/admin/quang-cao/{id}")
    @ResponseBody
    public ResponseEntity<QuangCao> updateQuangCao(
            @PathVariable("id") Integer id,
            @RequestBody QuangCao quangCaoMoi) {
 
        QuangCao qcOld = quangCaoService.getQuangCaoById(id);
        qcOld.setTieuDe(quangCaoMoi.getTieuDe());
        qcOld.setNoiDung(quangCaoMoi.getNoiDung());
        qcOld.setHinhAnh(quangCaoMoi.getHinhAnh());
        qcOld.setTrangThai(quangCaoMoi.getTrangThai());
 
        return ResponseEntity.ok(quangCaoService.saveQuangCao(qcOld));
    }
 
    @DeleteMapping("/api/admin/quang-cao/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteQuangCao(@PathVariable Integer id) {
        quangCaoService.deleteQuangCao(id);
        return ResponseEntity.ok("Xóa quảng cáo thành công!");
    }
}