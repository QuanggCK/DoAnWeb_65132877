package clc65.quanggck.controllers;

import clc65.quanggck.models.*;
import clc65.quanggck.services.*;
import jakarta.servlet.http.HttpSession; // Dùng để lưu trạng thái đăng nhập của User
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller 
@RequestMapping("/") 
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

    // 1. TRANG CHỦ / INDEX
    @GetMapping({"/", "/index"})
    public String trangChu(HttpSession session, Model model) {
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc());
        model.addAttribute("dsMonAn", monAnService.getMonAnDangBan());
        model.addAttribute("dsQuangCao", quangCaoService.getQuangCaoDangBat());
        
        // --- ĐỒNG BỘ LOGIC KIỂM TRA ĐĂNG NHẬP & GIỎ HÀNG CHO HEADER ---
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin != null) {
            // Đếm số lượng món trong giỏ hàng thực tế từ cơ sở dữ liệu của User này
            int cartSize = donHangService.getCartSizeByUserId(userLogin.getUserId()); 
            session.setAttribute("cartSize", cartSize);
        } else {
            // Nếu chưa đăng nhập thì mặc định giỏ hàng bằng 0
            session.setAttribute("cartSize", 0);
        }
        
        return "index"; 
    }

    // 2. GIAO DIỆN ĐĂNG KÝ
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc()); // Cho header
        model.addAttribute("user", new User());
        return "register"; // Mở file register.html
    }

    // XỬ LÝ ĐĂNG KÝ
    @PostMapping("/register")
    public String register(@ModelAttribute("user") User user, Model model) {
        try {
            userService.register(user);
            return "redirect:/login?success"; 
        } catch (Exception e) {
            model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc());
            model.addAttribute("error", e.getMessage());
            return "register"; 
        }
    }

    // 3. GIAO DIỆN ĐĂNG NHẬP
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc()); // Cho header
        return "login"; // Mở file login.html
    }

    // XỬ LÝ ĐĂNG NHẬP
    @PostMapping("/login")
    public String login(@RequestParam("sdt") String sdt, 
                        @RequestParam("mat_khau") String matKhau, 
                        HttpSession session, Model model) {
        try {
            // Gọi tầng service xử lý kiểm tra SĐT và mật khẩu
            User user = userService.login(sdt, matKhau);
            
            // ĐỒNG BỘ SESSION:
            session.setAttribute("user", user);       // Để Header nhận biết: ${session.user != null}
            session.setAttribute("userLogin", user);  // Giữ nguyên cho các tầng logic cũ của bạn
            
            return "redirect:/"; // Đăng nhập đúng điều hướng về trang chủ
        } catch (Exception e) {
            model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc());
            model.addAttribute("error", e.getMessage());
            return "login"; // Sai mật khẩu/SĐT quay lại trang login báo lỗi
        }
    }
    
 // 6. XEM TRANG CÁ NHÂN (TÀI KHOẢN)
    @GetMapping("/tai-khoan")
    public String showTaiKhoan(HttpSession session, Model model) {
        User userLogin = (User) session.getAttribute("userLogin");
        
        if (userLogin == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc());
        model.addAttribute("user", userLogin); 
        
        return "tai-khoan"; 
    }

    // ĐĂNG XUẤT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Xóa sạch toàn bộ các session liên quan đến đăng nhập và giỏ hàng
        session.removeAttribute("userLogin"); 
        session.removeAttribute("user");
        session.removeAttribute("cartSize");
        return "redirect:/";
    }

    // 4. XỬ LÝ ĐẶT HÀNG (CHECK OUT) FROM GIỎ HÀNG THYMELEAF
    @PostMapping("/dat-hang")
    public String createDonHang(@ModelAttribute DonHang donHang, HttpSession session, Model model) {
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin == null) {
            return "redirect:/login"; // Chưa đăng nhập bắt buộc đi đăng nhập
        }
        try {
            donHang.setUser(userLogin); // Gán user đang đăng nhập vào đơn hàng
            donHangService.createDonHang(donHang);
            return "redirect:/lich-su-don-hang"; // Đặt thành công chuyển đến trang lịch sử
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/gio-hang?error";
        }
    }

    // 5. XEM LỊCH SỬ ĐƠN HÀNG CỦA KHÁCH HÀNG ĐANG ĐĂNG NHẬP
    @GetMapping("/lich-su-don-hang")
    public String getLichSuDonHang(HttpSession session, Model model) {
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc()); // Cho header
        // Lấy lịch sử đơn theo ID người dùng đang đăng nhập trong Session
        List<DonHang> lichSu = donHangService.getLichSuDonHang(userLogin.getUserId());
        model.addAttribute("dsDonHang", lichSu);
        
        return "lich-su"; // Mở file lich-su.html
    }
}