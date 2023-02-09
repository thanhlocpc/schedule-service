package com.schedule.app.models.dtos.auths;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author : Thành Lộc
 * @since : 02/08/2022
 **/
@Data
public class ValidateOtpInpoutDTO {
    @NotBlank(message = "Email là bắt buộc")
    private String email;

    @NotBlank(message = "OTP là bắt buộc")
    private int otp;

    @NotBlank(message = "Mật khẩu là bắt buộc")
    private String password;
}
