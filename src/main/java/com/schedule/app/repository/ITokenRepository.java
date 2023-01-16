package com.schedule.app.repository;

import com.schedule.app.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : Thành Lộc
 * @since : 10/12/2022, Wed
 **/
public interface ITokenRepository extends JpaRepository<Token, Long> {
    Token findByToken(String token);
}
