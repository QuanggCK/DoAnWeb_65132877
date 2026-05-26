package clc65.quanggck.controllers;

import clc65.quanggck.models.*;
import clc65.quanggck.services.*;
import jakarta.servlet.http.HttpSession; 
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import clc65.quanggck.repos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
    public String trangChu(HttpSession session, Model model,
                            @RequestParam(name = "page", defaultValue = "0") int page) {
        
        Page<MonAn> pageMonAn = monAnService.getMonAnDangBan(PageRequest.of(page, 10));
        
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc());
        model.addAttribute("dsMonAn", pageMonAn.getContent());
        model.addAttribute("totalPages", pageMonAn.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("dsQuangCao", quangCaoService.getQuangCaoDangBat());

        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin != null) {
            session.setAttribute("cartSize", donHangService.getCartSizeByUserId(userLogin.getUserId()));
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
        // SỬA: Đồng bộ kiểm tra bằng 'userLogin' thay vì 'user'
        User userInSession = (User) session.getAttribute("userLogin");
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
            
            // SỬA: Lưu tập trung vào 'userLogin' để toàn bộ hệ thống cùng nhận diện chung
            session.setAttribute("userLogin", user);  
            
            return "redirect:/"; 
        } catch (Exception e) {
            model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc());
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", new User()); 
            return "login"; 
        }
    }
    
    // 6. XEM TRANG CÁ NHÂN (TÀI KHẢN)
    @GetMapping("/tai-khoan")
    public String showTaiKhoan(HttpSession session, Model model) {
        // SỬA: Đồng bộ kiểm tra session 'userLogin'
        User userInSession = (User) session.getAttribute("userLogin");
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
        // SỬA: Đồng bộ kiểm tra session 'userLogin'
        User userInSession = (User) session.getAttribute("userLogin");
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
            RedirectAttributes redirectAttributes) { // SỬA: Đã xóa tham số 'HttpServletRequest request' không dùng đến

        // SỬA: Đồng bộ lấy dữ liệu từ 'userLogin'
        User currentUser = (User) session.getAttribute("userLogin");
        if (currentUser == null) return "redirect:/login";

        try {
            User userDb = userRepository.findById(currentUser.getUserId()).orElse(null);
            if (userDb != null) {
                userDb.setTenKhach(tenKhach);
                userDb.setSdt(sdt);
                userDb.setDiaChi(diaChi);

                if (anhProfile != null && !anhProfile.isEmpty()) {
                    // Mẹo lưu trữ: Nếu chưa cấu hình Resource Mapping, hãy lưu tạm vào thư mục static của Project để hiển thị được ảnh ngay
                    String folderPath = "src/main/resources/static/images/profiles/";
                    
                    File folder = new File(folderPath);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }

                    String fileName = System.currentTimeMillis() + "_" + anhProfile.getOriginalFilename();
                    File destFile = new File(folder, fileName);
                    anhProfile.transferTo(destFile);
                    
                    userDb.setAnh(fileName);
                }

                userRepository.save(userDb);
                
                // Cập nhật lại session duy nhất
                session.setAttribute("userLogin", userDb);
                redirectAttributes.addFlashAttribute("messageSuccess", "Cập nhật thành công!");
            }

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("messageError", "Lỗi khi tải ảnh!");
        }

        return "redirect:/tai-khoan";
    }
    
 // CHI TIẾT MÓN ĂN
    @GetMapping("/mon-an/{id}")
    public String chiTietMonAn(@PathVariable(name = "id") Integer id, Model model) {
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc());
        model.addAttribute("mon", monAnService.getMonAnById(id));
        return "detail-food";
    }
    
    @GetMapping("/danh-muc/{id}")
    public String monAnTheoDanhMuc(@PathVariable(name = "id") Integer id, HttpSession session, Model model) {
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc());
        List<MonAn> dsMonAn = monAnService.getMonAnByDanhMuc(id);
        model.addAttribute("dsMonAn", dsMonAn);
        model.addAttribute("activeDanhMucId", id); 
        model.addAttribute("dsQuangCao", quangCaoService.getQuangCaoDangBat());
        model.addAttribute("totalPages", 1);
        model.addAttribute("currentPage", 0);
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin != null) {
            session.setAttribute("cartSize", donHangService.getCartSizeByUserId(userLogin.getUserId()));
        } else {
            session.setAttribute("cartSize", 0);
        }
        return "index"; 
    }

    // ĐĂNG XUẤT
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("userLogin"); 
        session.removeAttribute("cartSize");
        return "redirect:/login"; 
    }
    
    @GetMapping("/quang-cao")
    public String hiểnThịTrangQuangCao(Model model) {
        model.addAttribute("dsQuangCao", quangCaoService.getQuangCaoDangBat());
        return "qc"; 
    }

 // ----- Thêm/Sửa hàm xử lý cập nhật Quảng cáo trong HomeController.java -----

    @PostMapping("/admin/quang-cao/edit/{id}")
    public String updateQuangCao(
            @PathVariable("id") Integer id,
            @RequestParam("tieuDe") String tieuDe,
            @RequestParam("noiDung") String noiDung,
            @RequestParam(value = "trangThai", required = false) Boolean trangThai,
            @RequestParam("anhQuangCao") MultipartFile anhQuangCao,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // 1. Kiểm tra quyền Admin
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin == null || !Boolean.TRUE.equals(userLogin.getRoleAdmin())) {
            return "redirect:/login";
        }

        try {
            // 2. Tìm quảng cáo gốc trong Database
            QuangCao qcDb = quangCaoService.getQuangCaoById(id);
            if (qcDb != null) {
                qcDb.setTieuDe(tieuDe);
                qcDb.setNoiDung(noiDung);
                // Xử lý Checkbox switch (nếu không tích chọn thì giá trị nhận về là null -> gán false)
                qcDb.setTrangThai(trangThai != null);

                // 3. Xử lý tải ảnh và lưu vào thư mục static/images/announcements/
                if (anhQuangCao != null && !anhQuangCao.isEmpty()) {
                    String folderPath = "src/main/resources/static/images/announcements/";
                    
                    File folder = new File(folderPath);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }

                    // Đổi tên file theo thời gian thực để tránh trùng lặp ảnh trùng tên
                    String fileName = System.currentTimeMillis() + "_" + anhQuangCao.getOriginalFilename();
                    File destFile = new File(folder, fileName);
                    anhQuangCao.transferTo(destFile);
                    
                    // Lưu tên file ảnh mới vào DB
                    qcDb.setHinhAnh(fileName);
                }

                // 4. Lưu dữ liệu thay đổi vào Database
                quangCaoService.saveQuangCao(qcDb);
                redirectAttributes.addFlashAttribute("messageSuccess", "Cập nhật quảng cáo thành công!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("messageError", "Gặp lỗi trong quá trình upload ảnh!");
        }

        // Quay trở lại trang danh sách quảng cáo/khuyến mãi (hoặc trang hiện tại tùy ý bạn)
        return "redirect:/quang-cao"; 
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