package clc65.quanggck.repos;

import clc65.quanggck.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {   
    Optional<User> findBySdt(String sdt);
    boolean existsBySdt(String sdt);
}