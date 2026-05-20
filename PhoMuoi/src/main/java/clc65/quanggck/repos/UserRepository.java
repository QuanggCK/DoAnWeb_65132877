package clc65.quanggck.repos;

import clc65.quanggck.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Tự động sinh ra câu lệnh SQL: SELECT * FROM users WHERE sdt = ?
    User findBySdt(String sdt);
}