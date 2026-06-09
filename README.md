Link Demo: [(https://youtu.be/t1Z4eovDqH4)]
---
## 1. Mô tả ứng dụng
Ứng dụng **PhoMuoi (Phở Mười)** là nền tảng website đặt đồ ăn trực tuyến (Food Order) được thiết kế dành riêng cho các nhà hàng, quán ăn. Hệ thống cung cấp một quy trình mua hàng mượt mà cho thực khách: từ việc khám phá thực đơn, thêm món vào giỏ hàng cho đến bước thanh toán và theo dõi lịch sử giao dịch. Đồng thời, dự án tích hợp một trang Quản trị (Admin Dashboard) toàn diện, giúp chủ quán dễ dàng kiểm soát menu, xử lý đơn hàng và quản lý tệp khách hàng, từ đó tối ưu hóa quy trình kinh doanh.

---

## 2. Công nghệ sử dụng
Dự án được xây dựng trên mô hình Full-Stack phổ biến với các công nghệ hiện đại:
* **Backend:** Java, Spring Boot, Spring MVC (Xử lý logic, API và điều hướng).
* **Database:** MySQL / Spring Data JPA (Quản lý và tương tác với cơ sở dữ liệu quan hệ).
* **Frontend:** HTML5, CSS3, Thymeleaf (Engine kết xuất giao diện server-side), Bootstrap 5 (Giao diện đáp ứng - Responsive), FontAwesome & Bootstrap Icons.
* **Cơ chế lưu trữ:** Quản lý trạng thái Giỏ hàng và Đăng nhập thông qua `Session`. Upload file trực tiếp lên Local Storage.

---

## 3. Các chức năng chính
Hệ thống phân chia rõ ràng hai vai trò người dùng (Authentication & Authorization):

### 🧑‍💻 Dành cho Khách Hàng (USER)
* **Khám phá thực đơn:** Xem danh sách món ăn theo danh mục, tìm kiếm món ăn, xem banner quảng cáo/khuyến mãi.
* **Giỏ hàng (Cart):** Thêm/sửa/xóa món ăn vào giỏ hàng một cách linh hoạt (Lưu trữ bằng Session tối ưu).
* **Đặt hàng (Checkout):** Cung cấp form thanh toán chi tiết với Tên người nhận, Số điện thoại, Địa chỉ giao hàng và hỗ trợ nhiều Phương thức thanh toán (COD, Momo, Chuyển khoản).
* **Quản lý cá nhân:** Theo dõi chi tiết Lịch sử đơn hàng, cập nhật thông tin tài khoản và upload thay đổi Ảnh đại diện (Avatar).
### ⚙️ Dành cho Quản trị viên (ADMIN)
* **Quản lý Thực đơn:** Thêm mới, chỉnh sửa, xóa Món ăn và Danh mục. Hỗ trợ upload và preview hình ảnh món ăn trực tiếp.
* **Quản lý Đơn hàng:** Xem danh sách tất cả đơn hàng, thay đổi trạng thái đơn (Chờ xác nhận, Đang giao, Đã hoàn thành, Đã hủy) để khách hàng tiện theo dõi.
* **Quản lý Marketing:** Đăng tải và chỉnh sửa các chiến dịch Quảng cáo, Banner khuyến mãi.
* **Quản lý Hệ thống:** Theo dõi danh sách tài khoản người dùng đăng ký trên hệ thống.

---

## 4. Hình ảnh giao diện hệ thống

* **Header:**
  <img width="1886" height="73" alt="hd" src="https://github.com/user-attachments/assets/62a4162b-f12f-4341-a7f4-6d5fa00f4e7d" />
  
* **Giao diện đăng nhập:**
<img width="760" height="437" alt="login" src="https://github.com/user-attachments/assets/072a03d4-87d8-4d19-8e5a-9ca2d5fc5099" />

* **Giao diện đăng ký:**
<img width="723" height="387" alt="regis" src="https://github.com/user-attachments/assets/63bad552-3c62-4544-b72a-8da04237e8fe" />

* **Giao diện món ăn:**
<img width="668" height="329" alt="food" src="https://github.com/user-attachments/assets/4bca0352-dcc9-40fa-be84-656f4e88a4a7" />

* **Giao diện trang chủ:**
<img width="722" height="361" alt="hpage" src="https://github.com/user-attachments/assets/c07e80a1-9b2c-4db7-be26-ac1f50b9d30e" />

* **Giao diện quản lý đơn hàng của Admin:**
<img width="725" height="387" alt="donhang" src="https://github.com/user-attachments/assets/33ce4563-dba3-41af-8dde-c88132787c1c" />

* **Giao diện quản lý món của Admin:**
<img width="724" height="395" alt="àood" src="https://github.com/user-attachments/assets/4afd368a-a146-4af8-ab5d-c5b6f69f907c" />

* **Giao diện quản lý của Admin:**
<img width="728" height="393" alt="dboard" src="https://github.com/user-attachments/assets/7eb70db0-63bf-4e5c-b670-946871f785d8" />


---
## 5. Sơ đồ kiến trúc hệ thống

Dự án được thiết kế theo mô hình kiến trúc phân tầng tiêu chuẩn, kết hợp giữa mô hình **MVC (Model-View-Controller)** ở tầng giao diện và kiến trúc dịch vụ (**Service-Repository**) ở tầng xử lý nghiệp vụ nhằm đảm bảo tính độc lập, dễ bảo trì và mở rộng dữ liệu.

```text
[ TRÌNH DUYỆT CỦA KHÁCH HÀNG / ADMIN ]
                      |   ^
   (1) HTTP Request   |   | (8) HTTP Response
  (Click Đặt hàng,    |   | (Trả về file HTML đã 
   Xem menu...)       |   |  được render giao diện)
                      v   |
+-------------------------------------------------------+
|          TẦNG GIAO DIỆN (PRESENTATION LAYER)          |
|  - HTML5, CSS3, Bootstrap 5, JavaScript               |
|  - Thymeleaf Templates (gio-hang.html, header.html)   |
+-------------------------------------------------------+
                      |   ^
    (2) Truyền Data   |   | (7) Gắn Data vào Model
    (Form, Biến URL)  |   | (Lệnh model.addAttribute)
                      v   |
+-------------------------------------------------------+
|             TẦNG ĐIỀU HƯỚNG (CONTROLLER)              |
|  - Kiểm tra Session (Chặn nếu chưa đăng nhập)         |
|  - HomeController, AdminController, UserController    |
+-------------------------------------------------------+
                      |   ^
   (3) Gọi hàm xử lý  |   | (6) Trả về Đối tượng
   (Create, Get,...)  |   | (Đơn Hàng, User, List Món)
                      v   |
+-------------------------------------------------------+
|            TẦNG NGHIỆP VỤ (SERVICE LAYER)             |
|  - Xử lý logic: Tính tổng tiền, Đổi trạng thái đơn... |
|  - MonAnService, DonHangService, UserService          |
+-------------------------------------------------------+
                      |   ^
    (4) Lệnh thao tác |   | (5) Trả về Dữ liệu thô
      (Save, Find...) |   | (Từ Database)
                      v   |
+-------------------------------------------------------+
|        TẦNG TRUY CẬP DỮ LIỆU (REPOSITORY LAYER)       |
|  - Spring Data JPA Interfaces                         |
|  - MonAnRepository, DonHangRepository, UserRepository |
+-------------------------------------------------------+
                      |   ^
   (4a) Sinh tự động  |   | (4b) Kết quả truy xuất
     lệnh SQL Query   |   | (JDBC ResultSet)
                      v   |
+-------------------------------------------------------+
|                CƠ SỞ DỮ LIỆU (MYSQL)                  |
|  - Lưu trữ: user, don_hang, ct_donhang, mon_an...     |
+-------------------------------------------------------+
```
---

## 6. Cấu trúc chi tiết thư mục dự án
```text
src/
├── main/
│   ├── java/
│   │   └── clc65/
│   │       └── quanggck/
│   │           ├── PhoMuoiApplication.java      # File khởi chạy chính của ứng dụng Spring Boot
│   │           │
│   │           ├── config/                      # Cấu hình hệ thống và bảo mật
│   │           │   ├── WebConfig.java           # Cấu hình hiển thị tài nguyên tĩnh và ảnh upload
│   │           │   └── SecurityConfig.java      # Cấu hình Spring Security, đăng nhập và phân quyền
│   │           │
│   │           ├── controllers/                 # Tiếp nhận request từ người dùng và điều hướng giao diện
│   │           │   ├── AdminController.java     # Xử lý đăng nhập, đăng ký và các chức năng quản trị
│   │           │   ├── HomeController.java      # Xử lý trang chủ, danh sách món ăn, quảng cáo và đơn hàng
│   │           │   ├── ChatBotController.java   # Xử lý chatbot theo kịch bản.
│   │           │   └── RController.java         # Cung cấp dữ liệu động (AJAX/API) cho giao diện
│   │           │
│   │           ├── models/                      # Các Entity ánh xạ với bảng trong cơ sở dữ liệu
│   │           │   ├── User.java                # Thông tin tài khoản người dùng và quyền hạn
│   │           │   ├── QuangCao.java            # Thông tin các banner/quảng cáo hiển thị trên website
│   │           │   ├── MonAn.java               # Thông tin món ăn, giá bán, mô tả và hình ảnh
│   │           │   ├── DonHang.java             # Thông tin đơn hàng của khách hàng
│   │           │   ├── DanhMuc.java             # Danh mục phân loại món ăn
│   │           │   └── CtDonHang.java           # Chi tiết các món ăn trong từng đơn hàng
│   │           │
│   │           ├── repos/                       # Tầng truy cập dữ liệu sử dụng Spring Data JPA
│   │           │   ├── UserRepository.java      # Truy vấn và quản lý thông tin người dùng
│   │           │   ├── CtDonHangRepository.java # Truy vấn chi tiết đơn hàng
│   │           │   ├── DonHangRepository.java   # Truy vấn và quản lý đơn hàng
│   │           │   ├── MonAnRepository.java     # Truy vấn danh sách món ăn
│   │           │   ├── QuangCaoRepository.java  # Truy vấn dữ liệu quảng cáo/banner
│   │           │   └── DanhMucRepository.java   # Truy vấn danh mục món ăn
│   │           │
│   │           └── services/                    # Tầng xử lý nghiệp vụ của hệ thống
│   │               ├── UserService.java         # Xử lý nghiệp vụ người dùng và xác thực tài khoản
│   │               ├── DanhMucService.java      # Xử lý nghiệp vụ danh mục món ăn
│   │               ├── DonHangService.java      # Xử lý tạo, cập nhật và quản lý đơn hàng
│   │               ├── MonAnService.java        # Xử lý nghiệp vụ liên quan đến món ăn
│   │               └── QuangCaoService.java     # Quản lý nội dung quảng cáo/banner
│   │
│   └── resources/
│       ├── application.properties               # Cấu hình kết nối MySQL, port, upload file,...
│       │
│       ├── static/                              # Chứa CSS, JavaScript, hình ảnh và tài nguyên tĩnh
│       │
│       └── templates/                           # Giao diện HTML sử dụng Thymeleaf
│           ├── fragments/                       # Các thành phần dùng chung (header, footer, navbar,...)
│           └── admin/                           # Giao diện dành cho quản trị viên
```



