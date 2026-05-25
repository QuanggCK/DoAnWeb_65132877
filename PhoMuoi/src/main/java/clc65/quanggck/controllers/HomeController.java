package clc65.quanggck.controllers;

import clc65.quanggck.models.*;
import clc65.quanggck.services.*;
import jakarta.servlet.http.HttpSession; // Dùng để lưu trạng thái đăng nhập của User
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import clc65.quanggck.repos.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller 
@RequestMapping("/") 
public class HomeController {

    private final MonAnService monAnService;
    private final DanhMucService danhMucService;
    private final QuangCaoService quangCaoService;
    private final UserService userService;
    private final DonHangService donHangService;
    
    // Đã sửa: Chuyển sang thuộc tính 'final' để ép buộc Constructor Injection ổn định
    private final UserRepository userRepository;

    // Đã sửa: Bổ sung thêm 'UserRepository userRepository' vào Constructor để Spring Boot tự động Inject chuẩn 100%
    public HomeController(MonAnService monAnService, DanhMucService danhMucService, 
                          QuangCaoService quangCaoService, UserService userService, 
                          DonHangService donHangService, UserRepository userRepository) {
        this.monAnService = monAnService;
        this.danhMucService = danhMucService;
        this.quangCaoService = quangCaoService;
        this.userService = userService;
        this.donHangService = donHangService;
        this.userRepository = userRepository; // Khởi tạo thành công biến userRepository
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
    
    // 6. XEM TRANG CÁ NHÂN (TÀI KHẢN)
    @GetMapping("/tai-khoan")
    public String showTaiKhoan(HttpSession session, Model model) {
        User userInSession = (User) session.getAttribute("user");
        
        if (userInSession == null) {
            return "redirect:/login";
        }
        
        // Đã bổ sung: Load danh sách danh mục để thanh Menu Header không bị trắng trơn dữ liệu
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc()); 
        
        // Lấy dữ liệu mới tinh từ database ra hiển thị
        User freshUser = userRepository.findById(userInSession.getUserId()).orElse(null);
        model.addAttribute("user", freshUser); 
        
        return "tai-khoan";
    }

    // GIAO DIỆN CẬP NHẬT THÔNG TIN TÀI KHOẢN
    @GetMapping("/tai-khoan/update")
    public String showUpdateForm(HttpSession session, Model model) {
        User userInSession = (User) session.getAttribute("user");
        if (userInSession == null) {
            return "redirect:/login";
        }
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc()); // Cho header
        User freshUser = userRepository.findById(userInSession.getUserId()).orElse(null);
        model.addAttribute("user", freshUser);
        return "update-mk"; // Mở file update-mk.html
    }

    // XỬ LÝ CẬP NHẬT THÔNG TIN (Hàm sửa lỗi kết hợp upload ảnh vật lý)
    @PostMapping("/tai-khoan/update")
    public String updateTaiKhoan(
            @RequestParam("tenKhach") String tenKhach,
            @RequestParam("sdt") String sdt,
            @RequestParam("diaChi") String diaChi,
            @RequestParam("anhProfile") MultipartFile anhProfile,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            return "redirect:/login"; 
        }

        try {
            // Tìm đối tượng thực tế bám chặt dữ liệu database gốc
            User userDb = userRepository.findById(currentUser.getUserId()).orElse(null);
            if (userDb != null) {
                userDb.setTenKhach(tenKhach);
                userDb.setSdt(sdt);
                userDb.setDiaChi(diaChi);
                
                // Xử lý lưu ảnh nếu người dùng chọn file mới
                if (anhProfile != null && !anhProfile.isEmpty()) {
                    String folderPath = "src/main/resources/static/images/profiles/";
                    String fileName = System.currentTimeMillis() + "_" + anhProfile.getOriginalFilename();

                    File destFile = new File(folderPath + fileName);
                    if (!destFile.getParentFile().exists()) {
                        destFile.getParentFile().mkdirs(); 
                    }

                    anhProfile.transferTo(destFile);
                    userDb.setAnh(fileName); // Lưu tên file ảnh đại diện vào trường 'anh'
                }

                // Thực hiện lưu thành công thông qua userRepository viết thường đã được tiêm
                userRepository.save(userDb);

                // Đồng bộ cập nhật lại toàn bộ các phiên Session hiện hành
                session.setAttribute("user", userDb);
                session.setAttribute("userLogin", userDb);

                redirectAttributes.addFlashAttribute("messageSuccess", "Cập nhật thông tin thành công!");
            }

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("messageError", "Có lỗi xảy ra trong quá trình tải ảnh!");
        }

        return "redirect:/tai-khoan";
    }

    // ĐĂNG XUẤT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
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
            return "redirect:/login"; 
        }
        try {
            donHang.setUser(userLogin); 
            donHangService.createDonHang(donHang);
            return "redirect:/lich-su-don-hang"; 
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/gio-hang?error";
        }
    }

    @GetMapping("/lich-su-don-hang")
    public String getLichSuDonHang(HttpSession session, Model model) {
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc()); 

        List<DonHang> lichSu = donHangService.getLichSuDonHang(userLogin.getUserId());
        model.addAttribute("dsDonHang", lichSu);
        
        return "lich-su"; 
    }
}