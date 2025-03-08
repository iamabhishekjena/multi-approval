package com.example.multi_approval.Repository;

import com.example.multi_approval.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Integer> {

    Optional<Users> findByLoginId(String loginId);

    boolean existsByEmailId(String emailId);
}
