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

        if (userRepository.findBySdt(user.getSdt()) != null) {
            throw new RuntimeException("Số điện thoại này đã được đăng ký!");
        }

        user.setRoleAdmin(false); 
        return userRepository.save(user);
    }

    public User login(String sdt, String matKhau) {
        User user = userRepository.findBySdt(sdt).orElse(null);
        if (user != null && user.getMatKhau().equals(matKhau)) {
            return user; 
        }
        throw new RuntimeException("Số điện thoại hoặc mật khẩu không chính xác!");
    }
}