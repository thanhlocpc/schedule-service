package com.schedule.app.services;

import com.schedule.app.entities.Token;

/**
 * @author : Thành Lộc
 * @since : 10/12/2022, Wed
 **/
public interface ITokenService {
    Token createToken(Token token);

    Token findByToken(String token);
}
