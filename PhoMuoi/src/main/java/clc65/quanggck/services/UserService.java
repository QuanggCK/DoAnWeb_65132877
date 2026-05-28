package clc65.quanggck.services;

import clc65.quanggck.models.User;
import clc65.quanggck.repos.UserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User register(User user) {
        // findBySdt trả về Optional<User> → dùng isPresent() để kiểm tra
        if (userRepository.findBySdt(user.getSdt()).isPresent()) {
            throw new RuntimeException("Số điện thoại này đã được đăng ký!");
        }

        user.setRoleAdmin(false);
        user.setAnh("default-avatar.png");
        return userRepository.save(user);
    }

    public User login(String sdt, String matKhau) {
        // Lấy user từ DB, ném lỗi ngay nếu không tìm thấy SĐT
        User user = userRepository.findBySdt(sdt)
                .orElseThrow(() -> new RuntimeException("Số điện thoại hoặc mật khẩu không chính xác!"));

        // Kiểm tra mật khẩu
        if (!user.getMatKhau().equals(matKhau)) {
            throw new RuntimeException("Số điện thoại hoặc mật khẩu không chính xác!");
        }

        // DEBUG – xem console IntelliJ sau khi đăng nhập
        System.out.println(">>> [LOGIN] tenKhach  = " + user.getTenKhach());
        System.out.println(">>> [LOGIN] roleAdmin  = " + user.getRoleAdmin());

        return user;
    }
}