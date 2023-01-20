package com.schedule.app.services;

import com.schedule.app.entities.RefreshToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface IRefreshTokenService {
    RefreshToken findByToken(String refreshToken);
    RefreshToken  createRefreshToken(String email);
}
