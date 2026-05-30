package clc65.quanggck.controllers;
 
import clc65.quanggck.models.*;
import clc65.quanggck.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
 
import java.io.File;
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

    @GetMapping("/admin/quang-cao/adjust")
    public String adminQuangCaoAdjustPage(jakarta.servlet.http.HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        return "admin/qc-adjust";
    }
 
    // =====================================================
    // REST API – prefix /api/admin
    // =====================================================
 
    // ----- QUẢN LÝ DANH MỤC -----
 
 // ----- QUẢN LÝ DANH MỤC -----
    
    // 1. Lấy danh sách tất cả danh mục
    @GetMapping("/api/admin/danh-muc")
    @ResponseBody
    public ResponseEntity<List<DanhMuc>> getAllDanhMuc() {
        return ResponseEntity.ok(danhMucService.getAllDanhMuc());
    }

    // 2. Lấy chi tiết 1 danh mục theo ID
    @GetMapping("/api/admin/danh-muc/{id}")
    @ResponseBody
    public ResponseEntity<DanhMuc> getDanhMucById(@PathVariable Integer id) {
        return ResponseEntity.ok(danhMucService.getDanhMucById(id));
    }

    // 3. Thêm danh mục mới
    @PostMapping("/api/admin/danh-muc")
    @ResponseBody
    public ResponseEntity<DanhMuc> saveDanhMuc(@RequestBody DanhMuc danhMuc) {
        return ResponseEntity.ok(danhMucService.saveDanhMuc(danhMuc));
    }

    // 4. Cập nhật danh mục đã có
    @PutMapping("/api/admin/danh-muc/{id}")
    @ResponseBody
    public ResponseEntity<DanhMuc> updateDanhMuc(@PathVariable Integer id, @RequestBody DanhMuc danhMuc) {
        // Gắn ID vào object để Spring Data JPA hiểu đây là lệnh Update chứ không phải Thêm mới
        danhMuc.setId(id);
        return ResponseEntity.ok(danhMucService.saveDanhMuc(danhMuc));
    }
 
    // 5. Xóa danh mục
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
    public ResponseEntity<?> updateTrangThaiDonHang(
            @PathVariable("orderId") Integer orderId,
            @RequestParam("trangThaiMoi") String trangThaiMoi) {
        if (orderId == null) {
            return ResponseEntity.badRequest().body("Mã đơn hàng không hợp lệ!");
        }
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

    // ----- REST UPLOAD API -----
 // ----- REST UPLOAD API -----
 // ----- REST UPLOAD API -----
    @PostMapping("/api/admin/upload")
    @ResponseBody
    public ResponseEntity<java.util.Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type) {
        java.util.Map<String, String> response = new java.util.HashMap<>();
        try {
            if (file == null || file.isEmpty()) {
                response.put("error", "File is empty");
                return ResponseEntity.badRequest().body(response);
            }
            String fileName = file.getOriginalFilename();
            
            // Lấy đường dẫn tuyệt đối tới thư mục static/images gốc của dự án
            String rootPath = System.getProperty("user.dir");
            String folderPath = rootPath + "/src/main/resources/static/images/";

            // [XỬ LÝ ĐƯỜNG DẪN Ổ CỨNG] Phân loại thư mục lưu trữ theo đúng yêu cầu của bạn
            if ("food".equalsIgnoreCase(type)) {
                folderPath += "food-img/";
            } else if ("profile".equalsIgnoreCase(type)) {
                folderPath += "profiles/";
            } else if ("ad".equalsIgnoreCase(type)) {
                // Ảnh quảng cáo ở thư mục images thôi, giữ nguyên folderPath gốc
            }

            // Tạo thư mục nếu chưa tồn tại
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            // Lưu file vào ổ đĩa
            File destFile = new File(folder, fileName);
            file.transferTo(destFile.getAbsoluteFile()); // getAbsoluteFile() để tránh lỗi thư mục tạm trên Spring Boot 3

            response.put("fileName", fileName);

            // [XỬ LÝ ĐƯỜNG DẪN WEB] Trả về filePath hiển thị tương ứng cho giao diện hiển thị
            String webPath = "/images/" + fileName;
            if ("food".equalsIgnoreCase(type)) {
                webPath = "/images/food-img/" + fileName;
            } else if ("profile".equalsIgnoreCase(type)) {
                webPath = "/images/profiles/" + fileName;
            } else if ("ad".equalsIgnoreCase(type)) {
                webPath = "/images/" + fileName;
            }
            
            response.put("filePath", webPath);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) { 
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
} 