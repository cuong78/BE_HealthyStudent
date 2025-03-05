package com.healthy.backend.repository;

import com.healthy.backend.entity.Users;
import com.healthy.backend.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {

    @Query("SELECT u FROM Users u")
    List<Users> findAllUsers();

    Users findByEmail(String email);

    @Query("SELECT u.userId FROM Users u ORDER BY u.userId DESC LIMIT 1")
    String findLastUserId();

    Optional<Users> findByUserId(String userId);

    List<Users> findByFullNameContaining(String fullName);

    List<Users> findByRole(Role role);
}