package com.schedule.app.controller;

import com.schedule.app.annotations.swagger.RequiredHeaderToken;
import com.schedule.app.entities.*;
import com.schedule.app.handler.TutorServiceException;
import com.schedule.app.models.dtos.auths.LoginDTO;
import com.schedule.app.models.wrapper.ObjectResponseWrapper;
import com.schedule.app.security.UserPrincipal;
import com.schedule.app.utils.Constants;
import com.schedule.app.utils.Extensions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.experimental.ExtensionMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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




    @PostMapping("/login")
    public ObjectResponseWrapper login(@Valid @RequestBody LoginDTO user) {
//        UserLogin userNet = userLoginService.findByUserName(user.getEmail());
        User userNet = userService.findByEmail(user.getEmail());

        if (userNet == null || userNet.getUserLogin() == null) {
            throw new TutorServiceException("Không tìm thấy tài khoản");
        }

        UserLogin userLogin = userNet.getUserLogin();

        if (!userLogin.getActive()) {
            throw new TutorServiceException("Tài khoản chưa được kích hoạt");
        }

        UserPrincipal userPrincipal = new UserPrincipal();
        if (null != userNet) {
            userPrincipal.setUserId(userNet.getId());
            userPrincipal.setUsername(userNet.getEmail());
            userPrincipal.setPassword(userLogin.getPassword());

            // lấy role
            Set<String> authorities = new HashSet<>();
            if (null != userNet.getRoles()) {
                userNet.getRoles().forEach(r -> {
                    authorities.add(r.getId());
                    r.getPermissions().forEach(p -> authorities.add(p.getId()));
                });
            }
            userPrincipal.setAuthorities(authorities);
        }
        if (!new BCryptPasswordEncoder().matches(user.getPassword().trim(), userPrincipal.getPassword())) {
            throw new TutorServiceException("Sai mật khẩu");
        }
        Token token = new Token();
        token.setToken(jwtUtil.generateToken(userPrincipal));
        token.setTokenExpDate(jwtUtil.generateExpirationDate());
        return ObjectResponseWrapper.builder()
                .status(1)
                .data(token.getToken())
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
            throw new TutorServiceException("Không tìm thấy user này.");
        }
        user.setUserLogin(null);

        return ObjectResponseWrapper.builder()
                .status(1)
                .data(user)
                .build();
    }



}
