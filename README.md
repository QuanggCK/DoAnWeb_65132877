Link Demo: []
---
## 1. Mô tả ứng dụng
Ứng dụng **PhoMuoi (Phở Mười)** là nền tảng website đặt đồ ăn trực tuyến (Food Order) được thiết kế dành riêng cho các nhà hàng, quán ăn. Hệ thống cung cấp một quy trình mua hàng mượt mà cho thực khách: từ việc khám phá thực đơn, thêm món vào giỏ hàng cho đến bước thanh toán và theo dõi lịch sử giao dịch. Đồng thời, dự án tích hợp một trang Quản trị (Admin Dashboard) toàn diện, giúp chủ quán dễ dàng kiểm soát menu, xử lý đơn hàng và quản lý tệp khách hàng, từ đó tối ưu hóa quy trình kinh doanh.

![Trang Chủ Ứng Dụng](chèn_link_ảnh_trang_chủ_vào_đây)

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
│   │           ├── TaskManagerApplication.java  # File khởi chạy chính của ứng dụng Spring Boot
│   │           │
│   │           ├── config/                      # Cấu hình hệ thống & Bảo mật
│   │           │   └── SecurityConfig.java      # Cấu hình Spring Security (Phân quyền ADMIN/USER, Login/Logout)
│   │           │
│   │           ├── controllers/                         # Tầng tiếp nhận Request từ trình duyệt và điều hướng View
│   │           │   ├── AuthController.java              # Xử lý Đăng nhập, Đăng ký tài khoản
│   │           │   ├── ProjectController.java           # Quản lý định tuyến Dự án (Xem, thêm, sửa, xóa dự án)
│   │           │   └── TaskController.java              # Quản lý định tuyến Công việc & Bình luận (Xem, thêm, sửa, xóa, comment)
│   │           │   └── GlobalControllerAdvice.java      # Nạp dữ liệu tự động cho thông báo (Top 5 thông báo gần nhất)
│   │           │
│   │           ├── models/                              # Tầng chứa các Entity định nghĩa cấu trúc bảng Database
│   │           │   ├── User.java                        # Thông tin tài khoản, vai trò (Role)
│   │           │   ├── Project.java                     # Thông tin dự án
│   │           │   ├── Task.java                        # Thông tin chi tiết công việc, mức độ ưu tiên, hạn chót
│   │           │   ├── TaskStatus.java                  # Định nghĩa trạng thái công việc (Mới, Đang làm, Hoàn thành)
│   │           │   └── ProjectMember.java               # Thông tin các thành viên trong 1 dự án
│   │           │   └── Notifiaction.java                # Thông báo về các thay đổi cập nhật (Thêm, sửa dự án).
│   │           │   └── Comment.java                     # Thông tin nội dung thảo luận, thời gian tạo bình luận
│   │           │
│   │           ├── repos/                       # Tầng tương tác trực tiếp, truy vấn dữ liệu từ MySQL (JPA)
│   │           │   ├── UserRepository.java      # Tìm kiếm user, kiểm tra trùng lặp email/username
│   │           │   ├── ProjectRepository.java   # Truy vấn danh sách dự án
│   │           │   ├── TaskRepository.java      # Tìm kiếm, lọc danh sách công việc
│   │           │   ├── TaskStatusRepository.java# Truy vấn danh mục trạng thái
│   │           │   ├── CommentRepository.java   # Lấy danh sách bình luận theo Task ID
│   │           │   └── NotificationRepository.java# Truy vấn danh mục thông báo
│   │           │
│   │           └── services/                      # Tầng xử lý logic nghiệp vụ xử lý dữ liệu trung gian
│   │               ├── UserService.java           # Logic xử lý thông tin người dùng, lấy user đăng nhập hiện tại
│   │               ├── ProjectService.java        # Logic tính toán, xử lý thông tin dự án
│   │               ├── TaskService.java           # Logic phân công công việc, kiểm tra hạn chót
│   │               ├── TaskStatusService.java     # Cung cấp danh mục trạng thái tiến độ
│   │               ├── NotificationService.java   # Cung cấp danh sách top 5 thông báo thay đổi gần nhấtnhất
│   │               └── CommentService.java        # Logic kiểm tra, lưu trữ các bình luận hợp lệ
│   │
│   └── resources/
│       ├── application.properties               # File cấu hình cấu hình Port, chuỗi kết nối MySQL DB, mã hóa
│       │
│       ├── static/                              # Chứa tài nguyên tĩnh của hệ thống (Trình duyệt tải trực tiếp)
│       │     
│       └── templates/                           # Thư mục chứa toàn bộ giao diện HTML của hệ thống (Thymeleaf Engine)
│           ├── login.html                       # Giao diện form Đăng nhập tài khoản
│           │
│           ├── fragments/                       # Các thành phần giao diện dùng chung được tái sử dụng
│           │   ├── header.html                  # Thanh điều hướng phía trên cùng (Navbar, Thông tin User, Đăng xuất)
│           │   └── sidebar.html                 # Thanh trình đơn bên trái (Menu chuyển tab Dự án, Công việc, Tài khoản)
│           │
│           ├── project/                         # Thư mục chứa bộ giao diện quản trị Dự án
│           │   ├── list.html                    # Trang hiển thị danh sách toàn bộ dự án hiện có
│           │   ├── add.html                     # Trang chứa form tạo dự án mới (Chỉ Admin nhìn thấy)
│           │   └── edit.html                    # Trang chứa form cập nhật thông tin dự án
│           │
│           └── task/                            # Thư mục chứa bộ giao diện quản trị Công việc
│               ├── list.html                    # Trang hiển thị danh sách công việc (Có hiển thị huy hiệu ưu tiên, trạng thái)
│               ├── add.html                     # Trang chứa form tạo và phân công công việc mới
│               ├── edit.html                    # Trang chứa form cập nhật tiến độ, sửa đổi công việc
│               └── detail.html                  # Trang chi tiết công việc (Hiển thị đầy đủ mô tả, bảng thông tin và khung chat thảo luận)

```



