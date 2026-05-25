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
import jakarta.servlet.http.HttpServletRequest;

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
    private final UserRepository userRepository;

    public HomeController(MonAnService monAnService, DanhMucService danhMucService, 
                          QuangCaoService quangCaoService, UserService userService, 
                          DonHangService donHangService, UserRepository userRepository) {
        this.monAnService = monAnService;
        this.danhMucService = danhMucService;
        this.quangCaoService = quangCaoService;
        this.userService = userService;
        this.donHangService = donHangService;
        this.userRepository = userRepository; 
    }

    // 1. TRANG CHỦ / INDEX
    @GetMapping({"/", "/index"})
    public String trangChu(HttpSession session, Model model) {
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc());
        model.addAttribute("dsMonAn", monAnService.getMonAnDangBan());
        model.addAttribute("dsQuangCao", quangCaoService.getQuangCaoDangBat());
        
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin != null) {
            int cartSize = donHangService.getCartSizeByUserId(userLogin.getUserId()); 
            session.setAttribute("cartSize", cartSize);
        } else {
            session.setAttribute("cartSize", 0);
        }
        
        return "index"; 
    }

    // 2. GIAO DIỆN ĐĂNG KÝ
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc()); 
        model.addAttribute("user", new User());
        return "register"; 
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
    public String showLoginForm(Model model, HttpSession session) {
        User userInSession = (User) session.getAttribute("user");
        
        if (userInSession != null) {
            return "redirect:/tai-khoan";
        }
        
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc()); 
        model.addAttribute("user", new User()); 
        
        return "login"; 
    }

    // XỬ LÝ ĐĂNG NHẬP
    @PostMapping("/login")
    public String login(@RequestParam("sdt") String sdt, 
                        @RequestParam("mat_khau") String matKhau, 
                        HttpSession session, Model model) {
        try {
            User user = userService.login(sdt, matKhau);
            
            session.setAttribute("user", user);       
            session.setAttribute("userLogin", user);  
            
            return "redirect:/"; 
        } catch (Exception e) {
            model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc());
            model.addAttribute("error", e.getMessage());
            
            // ĐÃ SỬA: Thêm object user rỗng vào đây để phòng trường hợp đăng nhập thất bại, 
            // Thymeleaf khi render lại trang login kèm thông báo lỗi không bị crash.
            model.addAttribute("user", new User()); 
            
            return "login"; 
        }
    }
    
    // 6. XEM TRANG CÁ NHÂN (TÀI KHẢN)
    @GetMapping("/tai-khoan")
    public String showTaiKhoan(HttpSession session, Model model) {
        User userInSession = (User) session.getAttribute("user");
        
        if (userInSession == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc()); 
        
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
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc()); 
        User freshUser = userRepository.findById(userInSession.getUserId()).orElse(null);
        model.addAttribute("user", freshUser);
        return "update-mk"; 
    }

    // XỬ LÝ CẬP NHẬT THÔNG TIN
    @PostMapping("/tai-khoan/update")
    public String updateTaiKhoan(
            @RequestParam("tenKhach") String tenKhach,
            @RequestParam("sdt") String sdt,
            @RequestParam("diaChi") String diaChi,
            @RequestParam("anhProfile") MultipartFile anhProfile,
            HttpSession session,
            HttpServletRequest request,           // ← THÊM CÁI NÀY
            RedirectAttributes redirectAttributes) {

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        try {
            User userDb = userRepository.findById(currentUser.getUserId()).orElse(null);
            if (userDb != null) {
                userDb.setTenKhach(tenKhach);
                userDb.setSdt(sdt);
                userDb.setDiaChi(diaChi);

                if (anhProfile != null && !anhProfile.isEmpty()) {
                    String uploadDir = System.getProperty("user.home") + "/phomuoi-uploads/profiles/";
                    
                    File folder = new File(uploadDir);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }

                    String fileName = System.currentTimeMillis() + "_" + anhProfile.getOriginalFilename();
                    File destFile = new File(folder, fileName);
                    anhProfile.transferTo(destFile);
                    
                    userDb.setAnh(fileName);
                }

                userRepository.save(userDb);
                session.setAttribute("user", userDb);
                session.setAttribute("userLogin", userDb);
                redirectAttributes.addFlashAttribute("messageSuccess", "Cập nhật thành công!");
            }

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("messageError", "Lỗi khi tải ảnh!");
        }

        return "redirect:/tai-khoan";
    }

    // ĐĂNG XUẤT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userLogin"); 
        session.removeAttribute("user");
        session.removeAttribute("cartSize");
        return "redirect:/login"; 
    }

    // 4. XỬ LÝ ĐẶT HÀNG
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