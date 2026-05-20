package clc65.quanggck.services;

import clc65.quanggck.models.User;
import clc65.quanggck.repos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Lấy tất cả người dùng (Dành cho Admin)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Đăng ký tài khoản mới
    public User register(User user) {
        // Kiểm tra xem số điện thoại đã tồn tại chưa
        if (userRepository.findBySdt(user.getSdt()) != null) {
            throw new RuntimeException("Số điện thoại này đã được đăng ký!");
        }
        // Mặc định tài khoản mới tạo không phải là admin
        user.setRoleAdmin(false); 
        return userRepository.save(user);
    }

    // Đăng nhập đơn giản
    public User login(String sdt, String matKhau) {
        User user = userRepository.findBySdt(sdt);
        if (user != null && user.getMatKhau().equals(matKhau)) {
            return user; // Đăng nhập thành công
        }
        throw new RuntimeException("Số điện thoại hoặc mật khẩu không chính xác!");
    }
}