package com.schedule.app.repository;

import com.schedule.app.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    RefreshToken findByRefreshToken(String token);
}
