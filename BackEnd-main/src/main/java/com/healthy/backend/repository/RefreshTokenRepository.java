package com.healthy.backend.repository;

import com.healthy.backend.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByHashedToken(String hashedToken);

    List<RefreshToken> findByUserId(String userId);

    @Modifying
    @Query("DELETE FROM RefreshToken r WHERE r.expiresAt <= :now")
    void deleteExpiredTokens(@Param("now") LocalDateTime now);

    @Transactional
    @Modifying
    void deleteByUserId(String userId);

} 