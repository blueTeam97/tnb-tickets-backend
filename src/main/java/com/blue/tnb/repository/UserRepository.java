package com.blue.tnb.repository;

import com.blue.tnb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    @Query(value="Select id from User u WHERE u.email= :userEmail",nativeQuery = true)
    Long getUserIdByEmail(@Param("userEmail") String userEmail);
}
