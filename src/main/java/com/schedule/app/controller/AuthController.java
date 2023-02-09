package com.schedule.app.controller;

import com.schedule.app.annotations.swagger.RequiredHeaderToken;
import com.schedule.app.entities.*;
import com.schedule.app.handler.ScheduleServiceException;
import com.schedule.app.models.dtos.auths.LoginDTO;
import com.schedule.app.models.dtos.auths.ValidateOtpInpoutDTO;
import com.schedule.app.models.requests.RefreshTokenRequest;
import com.schedule.app.models.responses.JwtResponse;
import com.schedule.app.models.wrapper.ObjectResponseWrapper;
import com.schedule.app.security.UserPrincipal;
import com.schedule.app.services.IRefreshTokenService;
import com.schedule.app.utils.Constants;
import com.schedule.app.utils.Extensions;
import com.schedule.app.utils.SendMail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author : Thành Lộc
 * @since : 10/12/2022, Wed
 **/

@ExtensionMethod(Extensions.class)
@RestController
@RequestMapping(Constants.AUTH_SERVICE_URL)
@Tag(name = "Authentication", description = "Authentication API")
@Validated
@Transactional
public class AuthController extends BaseAPI {

    @Autowired
    private IRefreshTokenService refreshTokenService;


    @PostMapping("/login")
    public ObjectResponseWrapper login(@Valid @RequestBody LoginDTO user) {
//        UserLogin userNet = userLoginService.findByUserName(user.getEmail());
        User userNet = userService.findByEmail(user.getEmail());
        System.out.println(userNet);
        UserPrincipal userPrincipal = userService.getUserPrincipal(userNet);
        if (!new BCryptPasswordEncoder().matches(user.getPassword().trim(), userPrincipal.getPassword())) {
            throw new ScheduleServiceException("Sai mật khẩu");
        }
        Token token = new Token();
        token.setToken(jwtUtil.generateToken(userPrincipal));
        token.setTokenExpDate(jwtUtil.generateExpirationDate());
        JwtResponse jwt = new JwtResponse();
        jwt.setAccessToken(jwtUtil.generateToken(userPrincipal));
        jwt.setTokenExpDate(jwtUtil.generateExpirationDate());
        jwt.setRefreshToken(refreshTokenService.createRefreshToken(userNet.getEmail()).getRefreshToken());
        return ObjectResponseWrapper.builder()
                .status(1)
                .data(jwt)
                .build();
    }

    @GetMapping("/refreshToken")
    @Operation(summary = "Refresh token")
    public ObjectResponseWrapper refreshToken(@Valid @RequestBody RefreshTokenRequest tokenRequest) {
        RefreshToken refreshToken = refreshTokenService.findByToken(tokenRequest.getRefreshToken());
        if (refreshToken == null) {
            throw new ScheduleServiceException("Token không tồn tại.");
        } else if (refreshToken.getExpiryDate().before(new Date())) {
            throw new ScheduleServiceException("Token đã hết hạn.");
        }

        User userNet = refreshToken.getUser();
        System.out.println(userNet);
        UserPrincipal userPrincipal = userService.getUserPrincipal(userNet);
        JwtResponse jwt = new JwtResponse();
        jwt.setAccessToken(jwtUtil.generateToken(userPrincipal));
        jwt.setTokenExpDate(jwtUtil.generateExpirationDate());

            jwt.setRefreshToken(refreshToken.getRefreshToken());

        return ObjectResponseWrapper.builder()
                .status(1)
                .data(jwt)
                .build();
    }

    @GetMapping("/user")
    @RequiredHeaderToken
    @Operation(summary = "Lấy thông tin user")
    public ObjectResponseWrapper getUserOwner(@RequestHeader(Constants.HEADER_TOKEN_NAME) String accessToken) {
        String token = "";
        if (accessToken != null && accessToken.length() > 6) {
            token = accessToken.substring(6);
        }
        UserPrincipal userPrincipal = jwtUtil.getUserFromToken(token);
        User user = userService.findById(userPrincipal.getUserId());

        if (user == null) {
            throw new ScheduleServiceException("Không tìm thấy user này.");
        }
        user.setUserLogin(null);

        return ObjectResponseWrapper.builder()
                .status(1)
                .data(user)
                .build();
    }


    @GetMapping("/forgot-password")
    @Operation(summary = "Quên mật khẩu")
    public ObjectResponseWrapper forgotPassword(@RequestParam(required = true) String email) throws MessagingException, UnsupportedEncodingException {

        try {
            User user = userService.findByEmail(email);

            if (user == null) {
                throw new ScheduleServiceException("Không tìm thấy user này.");
            }
            int otp = new Random().nextInt(1000000 - 100000) + 100000;
            SendMail.sendMailOTP(email, otp);
            otpRepository.save(new Otp(user.getId(), (long) otp, Calendar.getInstance().getTime()));
            return ObjectResponseWrapper.builder()
                    .status(1)
                    .message("Vui lòng kiểm tra OTP trong email.")
                    .build();
        }catch (Exception e){
            return ObjectResponseWrapper.builder()
                    .status(1)
                    .message("Vui lòng thử lại.")
                    .build();
        }

    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Cập nhật mật khẩu mới")
    public ObjectResponseWrapper verifyOTP(@RequestBody(required = true) ValidateOtpInpoutDTO validateOtpInpoutDTO) {
        User user = userService.findByEmail(validateOtpInpoutDTO.getEmail());
        if (user == null) {
            throw new ScheduleServiceException("Không tìm thấy user này.");
        }

        Otp otp = otpRepository.findOtpByUserId(user.getId());
        if (otp == null) {
            throw new ScheduleServiceException("OTP không hợp lệ.");
        }
        if (otp.getOtp() != validateOtpInpoutDTO.getOtp()) {
            throw new ScheduleServiceException("OTP không hợp lệ.");
        }
        if (Calendar.getInstance().getTime().getTime() - otp.getCreatedAt().getTime() > 2 * 60 * 1000) {
            throw new ScheduleServiceException("OTP hết hạn.");
        }

        UserLogin userLogin = userLoginService.findUserLoginByUserId(user.getId());
        userLogin.setPassword(new BCryptPasswordEncoder().encode(validateOtpInpoutDTO.getPassword().trim()));
        userLoginService.save(userLogin);
        otpRepository.delete(otp);
        return ObjectResponseWrapper.builder()
                .status(1)
                .message("Success")
                .build();
    }

}
