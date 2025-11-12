package com.zoonosys.repositories;

import com.zoonosys.models.PasswordResetToken;
import com.zoonosys.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiryDate <= :currentTime OR t.used = true")
    int deleteExpiredAndUsedTokens(@Param("currentTime") LocalDateTime currentTime);

    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.user = :user AND t.used = false")
    void deleteAllByUserAndUsedIsFalse(@Param("user")User user);
}
