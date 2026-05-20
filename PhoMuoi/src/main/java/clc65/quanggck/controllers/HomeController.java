package clc65.quanggck.controllers;

import clc65.quanggck.models.*;
import clc65.quanggck.services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/home")
@CrossOrigin("*") 
public class HomeController {

    private final MonAnService monAnService;
    private final DanhMucService danhMucService;
    private final QuangCaoService quangCaoService;
    private final UserService userService;
    private final DonHangService donHangService;


    public HomeController(MonAnService monAnService, DanhMucService danhMucService, 
                          QuangCaoService quangCaoService, UserService userService, 
                          DonHangService donHangService) {
        this.monAnService = monAnService;
        this.danhMucService = danhMucService;
        this.quangCaoService = quangCaoService;
        this.userService = userService;
        this.donHangService = donHangService;
    }

    // 1. Lấy danh sách danh mục món ăn
    @GetMapping("/danh-muc")
    public ResponseEntity<List<DanhMuc>> getAllDanhMuc() {
        return ResponseEntity.ok(danhMucService.getAllDanhMuc());
    }

    // 2. Lấy danh sách món ăn đang được mở bán
    @GetMapping("/mon-an")
    public ResponseEntity<List<MonAn>> getMonAnDangBan() {
        return ResponseEntity.ok(monAnService.getMonAnDangBan());
    }

    // 3. Lấy danh sách banner quảng cáo đang hoạt động
    @GetMapping("/quang-cao")
    public ResponseEntity<List<QuangCao>> getQuangCaoDangBat() {
        return ResponseEntity.ok(quangCaoService.getQuangCaoDangBat());
    }

    // 4. API Đăng ký tài khoản khách hàng
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User newUser = userService.register(user);
            return ResponseEntity.ok(newUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 5. API Đăng nhập
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String sdt, @RequestParam String matKhau) {
        try {
            User user = userService.login(sdt, matKhau);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 6. API Khách hàng gửi đơn đặt hàng (Check out)
    @PostMapping("/dat-hang")
    public ResponseEntity<?> createDonHang(@RequestBody DonHang donHang) {
        try {
            DonHang newOrder = donHangService.createDonHang(donHang);
            return ResponseEntity.ok(newOrder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Đặt hàng thất bại: " + e.getMessage());
        }
    }

    // 7. Xem lịch sử đơn hàng của 1 khách hàng
    @GetMapping("/lich-su-don-hang/{userId}")
    public ResponseEntity<List<DonHang>> getLichSuDonHang(@PathVariable Integer userId) {
        return ResponseEntity.ok(donHangService.getLichSuDonHang(userId));
    }
}