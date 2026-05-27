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
    @PostMapping("/gio-hang/them")
    public String themVaoGioHang(
            @RequestParam("monAnId") Integer monAnId,
            @RequestParam("soLuong") Integer soLuong,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User userLogin = (User) session.getAttribute("userLogin");

        if (userLogin == null) {
            redirectAttributes.addFlashAttribute("messageError", "Vui lòng đăng nhập để thêm vào giỏ hàng!");
            return "redirect:/login";
        }

        try {
            MonAn monAn = monAnService.getMonAnById(monAnId);
            donHangService.themVaoGio(userLogin, monAn, soLuong);

            int cartSize = donHangService.getCartSizeByUserId(userLogin.getUserId());
            session.setAttribute("cartSize", cartSize);

            redirectAttributes.addFlashAttribute("messageSuccess", 
                "Đã thêm \"" + monAn.getTenMon() + "\" vào giỏ hàng!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageError", "Lỗi: " + e.getMessage());
        }

        return "redirect:/";
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
    
 // ----- 1. HÀM GET: HIỂN THỊ GIAO DIỆN FORM ĐIỀU CHỈNH -----
    @GetMapping("/admin/quang-cao/edit/{id}")
    public String trangChinhSuaQuangCao(@PathVariable("id") Integer id, HttpSession session, Model model) {
        // Kiểm tra quyền quản trị Admin
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin == null || !Boolean.TRUE.equals(userLogin.getRoleAdmin())) {
            return "redirect:/login"; // Khách thường hoặc chưa đăng nhập thì đá về login
        }
        
        // Truyền ID quảng cáo sang Model để giao diện Thymeleaf nhận diện
        model.addAttribute("idQuangCao", id); 
        return "qc-adjust"; // Trả về tệp templates/qc-adjust.html
    }

    // ----- 2. HÀM POST: XỬ LÝ NHẬN DỮ LIỆU FORM & UPLOAD FILE ẢNH -----
    @PostMapping("/admin/quang-cao/edit/{id}")
    public String updateQuangCao(
            @PathVariable("id") Integer id,
            @RequestParam("tieuDe") String tieuDe,
            @RequestParam("noiDung") String noiDung,
            @RequestParam(value = "trangThai", required = false) Boolean trangThai,
            @RequestParam("anhQuangCao") MultipartFile anhQuangCao,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Kiểm tra lại quyền Admin để đảm bảo an toàn bảo mật dữ liệu
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin == null || !Boolean.TRUE.equals(userLogin.getRoleAdmin())) {
            return "redirect:/login";
        }

        try {
            // Tìm kiếm đối tượng quảng cáo gốc từ Database lên
            QuangCao qcDb = quangCaoService.getQuangCaoById(id);
            if (qcDb != null) {
                qcDb.setTieuDe(tieuDe);
                qcDb.setNoiDung(noiDung);
                // Xử lý nút gạt Switch dạng Boolean (Nếu không tích chọn giá trị truyền về sẽ là null)
                qcDb.setTrangThai(trangThai != null);

                // Kiểm tra xem Admin có tải tệp ảnh mới lên không
                if (anhQuangCao != null && !anhQuangCao.isEmpty()) {
                    // Thư mục lưu trữ theo đúng yêu cầu của bạn
                    String folderPath = "src/main/resources/static/images/announcements/";
                    
                    File folder = new File(folderPath);
                    if (!folder.exists()) {
                        folder.mkdirs(); // Tự động tạo thư mục nếu chưa tồn tại
                    }

                    // Thiết lập tên file theo thời gian thực để tránh trùng tên ảnh cũ
                    String fileName = System.currentTimeMillis() + "_" + anhQuangCao.getOriginalFilename();
                    File destFile = new File(folder, fileName);
                    anhQuangCao.transferTo(destFile); // Ghi file trực tiếp vào ổ đĩa
                    
                    qcDb.setHinhAnh(fileName); // Cập nhật tên file ảnh mới vào DB
                }

                // Tiến hành lưu cập nhật
                quangCaoService.saveQuangCao(qcDb);
                redirectAttributes.addFlashAttribute("messageSuccess", "Cập nhật quảng cáo thành công!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("messageError", "Gặp lỗi trong quá trình upload ảnh!");
        }

        // Sau khi xử lý xong, chuyển hướng về trang danh sách khuyến mãi
        return "redirect:/quang-cao"; 
    }

    @GetMapping("/gio-hang")
    public String xemGioHang(HttpSession session, Model model) {
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin == null) {
            return "redirect:/login";
        }

        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc()); 

        DonHang donHang = (DonHang) session.getAttribute("gioHangSession");
        if (donHang == null) {
            donHang = new DonHang();
            donHang.setDsChiTietDonHang(new java.util.ArrayList<>());
            donHang.setTongGia(0L);
        }

        // TỰ ĐỘNG ĐIỀN: Nếu đơn hàng chưa có SĐT nhận, lấy luôn SĐT của user đăng nhập
        if (donHang.getSoDienThoaiNhan() == null || donHang.getSoDienThoaiNhan().isEmpty()) {
            // Hãy kiểm tra trong file User.java của bạn hàm lấy SĐT tên là gì nhé (VD: getSoDienThoai(), getSdt(), getPhone()...)
            donHang.setSoDienThoaiNhan(userLogin.getSdt()); 
        }

        model.addAttribute("donHang", donHang);
        return "gio-hang"; 
    }
    // 4. XỬ LÝ ĐẶT HÀNG
 // 4. XỬ LÝ ĐẶT HÀNG
 // 4. XỬ LÝ ĐẶT HÀNG (LẤY DỮ LIỆU TỪ GIỎ HÀNG TRONG SESSION)
    @PostMapping("/dat-hang")
    public String createDonHang(@RequestParam(value = "soDienThoaiNhan", required = false) String soDienThoaiNhan,
                                @RequestParam(value = "ghiChu", required = false) String ghiChu,
                                HttpSession session, 
                                RedirectAttributes redirectAttributes) {
        
        // 1. Kiểm tra đăng nhập
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn cần đăng nhập để tiến hành đặt hàng!");
            return "redirect:/login"; 
        }
        
        // 2. Lấy giỏ hàng tích lũy từ Session ra
        DonHang gioHangHienTai = (DonHang) session.getAttribute("gioHangSession");
        if (gioHangHienTai == null || gioHangHienTai.getDsChiTietDonHang() == null || gioHangHienTai.getDsChiTietDonHang().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Giỏ hàng của bạn đang trống! Không thể đặt hàng.");
            return "redirect:/";
        }
        
        try {
            // 3. Gán các thông tin cần thiết từ khách hàng vào Đơn hàng
            gioHangHienTai.setUser(userLogin);
            gioHangHienTai.setNgayDat(java.time.LocalDateTime.now());
            gioHangHienTai.setTrangThai("Chờ xử lý");
            
            // Nếu trên giao diện giỏ hàng có ô nhập SĐT và Ghi chú thì hứng thông tin điền vào đơn
            if (soDienThoaiNhan != null) gioHangHienTai.setSoDienThoaiNhan(soDienThoaiNhan);
            if (ghiChu != null) gioHangHienTai.setGhiChu(ghiChu);
            
            // 4. Lưu đơn hàng cùng toàn bộ danh sách CtDonHang vào Database thông qua Service của bạn
            donHangService.createDonHang(gioHangHienTai);
            
            // 5. ĐẶT HÀNG THÀNH CÔNG -> Giải phóng (Xóa) giỏ hàng tạm thời để tránh trùng lặp
            session.removeAttribute("gioHangSession");
            session.setAttribute("cartSize", 0); // Đèn giỏ hàng trên Header tắt (trở về 0)
            
            // 6. Gửi thông báo thành công sang trang Lịch sử đơn hàng
            redirectAttributes.addFlashAttribute("successMessage", "Chúc mừng! Bạn đã đặt hàng thành công.");
            return "redirect:/lich-su-don-hang"; 
            
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi đặt hàng: " + e.getMessage());
            return "redirect:/gio-hang";
        }
    }
   
    
    @GetMapping("/lich-su-don-hang")
    public String getLichSuDonHang(HttpSession session, Model model) {
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc()); 
        
        // THÊM DÒNG NÀY ĐỂ KÍCH HOẠT HIỆU ỨNG SÁNG ĐÈN Ở SIDE-BAR:
        model.addAttribute("activePage", "lich-su");

        List<DonHang> lichSu = donHangService.getLichSuDonHang(userLogin.getUserId());
        model.addAttribute("dsDonHang", lichSu);
        
        return "lich-su"; 
    }
}