package com.example.auth.repository;
import com.example.auth.entity.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
public interface ResetTokenRepository extends JpaRepository<ResetToken,Long> {Optional<ResetToken> findByTokenHash(String hash);@Transactional void deleteByUserId(Long userId);}
