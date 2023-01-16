package com.schedule.app.services.impl;

import com.schedule.app.entities.Token;
import com.schedule.app.services.ABaseServices;
import com.schedule.app.services.ITokenService;
import org.springframework.stereotype.Service;

/**
 * @author : Thành Lộc
 * @since : 10/12/2022, Wed
 **/
@Service
public class TokenService extends ABaseServices implements ITokenService {
    @Override
    public Token createToken(Token token) {
        return tokenRepository.saveAndFlush(token);
    }

    @Override
    public Token findByToken(String token) {
        return tokenRepository.findByToken(token);
    }
}
