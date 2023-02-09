package com.schedule.app.repository;

import com.schedule.app.entities.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : Thành Lộc
 * @since : 02/08/2022
 **/
public interface IOtpRepository extends JpaRepository<Otp, Long> {
    Otp findOtpByUserId(Long userId);

}
