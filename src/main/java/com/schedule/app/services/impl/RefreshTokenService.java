package com.schedule.app.services.impl;

import com.schedule.app.entities.RefreshToken;
import com.schedule.app.repository.IRefreshTokenRepository;
import com.schedule.app.repository.IUserRepository;
import com.schedule.app.services.ABaseServices;
import com.schedule.app.services.IRefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
@Service
public class RefreshTokenService extends ABaseServices implements IRefreshTokenService {
    @Autowired
    private IRefreshTokenRepository refreshTokenRepository;
    @Autowired
    private IUserRepository userRepository;
    @Override
    public RefreshToken findByToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken);
    }

    @Override
    public RefreshToken createRefreshToken(String email) {
        RefreshToken refreshToken=new RefreshToken();
        refreshToken.setUser(userRepository.findByEmail(email));
        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + 6000000));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
}
