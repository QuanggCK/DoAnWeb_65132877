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
        User userInSession = (User) session.getAttribute("userLogin");
        if (userInSession != null) {
            // Admin đã đăng nhập → về trang admin
            if (Boolean.TRUE.equals(userInSession.getRoleAdmin())) {
                return "redirect:/admin";
            }
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

            // Lưu tập trung vào 'userLogin' để toàn bộ hệ thống cùng nhận diện chung
            session.setAttribute("userLogin", user);

            // Nếu tài khoản có quyền Admin → chuyển thẳng sang trang quản trị
            if (Boolean.TRUE.equals(user.getRoleAdmin())) {
                return "redirect:/admin";
            }

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
            @RequestParam(value = "anhProfile", required = false) MultipartFile anhProfile, // Thêm required = false để tránh lỗi 400 khi không chọn ảnh
            HttpSession session,
            RedirectAttributes redirectAttributes) { 

        // Đồng bộ lấy dữ liệu từ 'userLogin'
        User currentUser = (User) session.getAttribute("userLogin");
        if (currentUser == null) return "redirect:/login";

        try {
            User userDb = userRepository.findById(currentUser.getUserId()).orElse(null);
            if (userDb != null) {
                userDb.setTenKhach(tenKhach);
                userDb.setSdt(sdt);
                userDb.setDiaChi(diaChi);

                // Xử lý lưu ảnh nếu người dùng có upload ảnh mới
                if (anhProfile != null && !anhProfile.isEmpty()) {
                    
                    // Lưu ảnh vào đúng thư mục mà WebConfig đang serve: src/main/resources/static/images/profiles/
                    String projectRoot = System.getProperty("user.dir");
                    String folderPath = projectRoot + "/src/main/resources/static/images/profiles/";
                    
                    File folder = new File(folderPath);
                    if (!folder.exists()) {
                        folder.mkdirs();
                    }

                    String fileName = System.currentTimeMillis() + "_" + anhProfile.getOriginalFilename();
                    File destFile = new File(folder, fileName);
                    
                    // Dùng getAbsoluteFile() để tránh bị ném vào thư mục Temp của Tomcat
                    anhProfile.transferTo(destFile.getAbsoluteFile());
                    
                    // Lưu tên file vào database
                    userDb.setAnh(fileName);
                }
                // Nếu không upload ảnh mới → giữ nguyên ảnh cũ (không làm gì cả)

                userRepository.save(userDb);
                
                // Cập nhật lại session duy nhất
                session.setAttribute("userLogin", userDb);
                redirectAttributes.addFlashAttribute("messageSuccess", "Cập nhật thành công!");
            }

        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("messageError", "Lỗi khi tải ảnh: " + e.getMessage());
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
    
 // THÊM HÀM NÀY VÀO DƯỚI ĐÁY FILE HOMECONTROLLER.JAVA
    @GetMapping("/quang-cao")
    public String trangQuangCao(Model model, HttpSession session) {
        model.addAttribute("dsQuangCao", quangCaoService.getQuangCaoDangBat());
        return "qc"; 
    }
    
    @GetMapping("/gio-hang")
    public String xemGioHang(HttpSession session, Model model) {
        // 1. Kiểm tra đăng nhập
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin == null) {
            return "redirect:/login";
        }

        model.addAttribute("dsDanhMuc", danhMucService.getAllDanhMuc()); 

        // 2. LẤY GIỎ HÀNG TỪ DATABASE THEO TRẠNG THÁI "Giỏ hàng"
        DonHang donHang = donHangService.getGioHangHienTaiByUserId(userLogin.getUserId());
        
        // ================= TRÌNH KIỂM TRA LOG CONSOLE =================
        System.out.println("======= [DIAGNOSTIC] KIỂM TRA GIỎ HÀNG =======");
        if (donHang == null) {
            System.out.println("-> KẾT QUẢ: Không tìm thấy đơn hàng trạng thái 'Giỏ hàng' nào trong DB.");
        } else {
            System.out.println("-> KẾT QUẢ: Tìm thấy giỏ hàng trong Database thành công!");
            int size = (donHang.getDsChiTietDonHang() != null) ? donHang.getDsChiTietDonHang().size() : 0;
            System.out.println("-> SỐ LƯỢNG DÒNG SẢN PHẨM: " + size);
            System.out.println("-> TỔNG TIỀN ĐANG TÍNH: " + donHang.getTongGia() + " đ");
        }
        System.out.println("=================================================");

        // 3. Nếu trong DB chưa có giỏ hàng, khởi tạo object tạm tránh lỗi giao diện hiển thị
        if (donHang == null) {
            donHang = new DonHang();
            donHang.setDsChiTietDonHang(new java.util.ArrayList<>());
            donHang.setTongGia(0L);
        }

        // Tự động điền số điện thoại nhận từ tài khoản
        if (donHang.getSoDienThoaiNhan() == null || donHang.getSoDienThoaiNhan().isEmpty()) {
            donHang.setSoDienThoaiNhan(userLogin.getSdt()); 
        }

        // 4. Truyền biến sang Thymeleaf HTML (bắt buộc tên biến là "donHang")
        model.addAttribute("donHang", donHang);
        
        return "gio-hang"; 
    }
    @PostMapping("/dat-hang")
    public String createDonHang(
            @RequestParam(value = "tenNguoiNhan", required = false) String tenNguoiNhan,
            @RequestParam(value = "soDienThoaiNhan", required = false) String soDienThoaiNhan,
            @RequestParam(value = "diaChiNhan", required = false) String diaChiNhan,
            @RequestParam(value = "phuongThucThanhToan", required = false) String phuongThucThanhToan,
            @RequestParam(value = "ghiChu", required = false) String ghiChu,
            HttpSession session, RedirectAttributes redirectAttributes) {
        
        // 1. Kiểm tra đăng nhập
        User userLogin = (User) session.getAttribute("userLogin");
        if (userLogin == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Bạn cần đăng nhập để tiến hành đặt hàng!");
            return "redirect:/login"; 
        }
        
        // 2. Lấy giỏ hàng từ Database lên
        DonHang gioHangHienTai = donHangService.getGioHangHienTaiByUserId(userLogin.getUserId());
        if (gioHangHienTai == null || gioHangHienTai.getDsChiTietDonHang() == null || gioHangHienTai.getDsChiTietDonHang().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Giỏ hàng của bạn đang trống! Không thể đặt hàng.");
            return "redirect:/";
        }
        
        try {
            // 3. Cập nhật thông tin nhận hàng & ghi chú từ form Checkout
            if (tenNguoiNhan != null) gioHangHienTai.setTenNguoiNhan(tenNguoiNhan);
            if (soDienThoaiNhan != null) gioHangHienTai.setSoDienThoaiNhan(soDienThoaiNhan);
            if (diaChiNhan != null) gioHangHienTai.setDiaChiNhan(diaChiNhan);
            if (phuongThucThanhToan != null) gioHangHienTai.setPhuongThucThanhToan(phuongThucThanhToan);
            if (ghiChu != null) gioHangHienTai.setGhiChu(ghiChu);
            
            // 4. CHUYỂN TRẠNG THÁI TỪ "Giỏ hàng" -> "Chờ xác nhận" và lưu
            donHangService.createDonHang(gioHangHienTai);
            
            // 5. Cập nhật lại số lượng badge hiển thị trên Header về 0
            session.removeAttribute("gioHangSession"); 
            session.setAttribute("cartSize", 0);
            
            // Gửi thông báo thành công
            redirectAttributes.addFlashAttribute("messageSuccess", "Chúc mừng! Bạn đã đặt hàng thành công.");
           
            return "redirect:/"; 
            
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
        
        model.addAttribute("activePage", "lich-su");

        List<DonHang> lichSu = donHangService.getLichSuDonHang(userLogin.getUserId());
        model.addAttribute("dsDonHang", lichSu);
        
        return "lich-su-don-hang"; 
    }
}