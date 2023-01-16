package com.schedule.app.models.dtos.auths;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author : Thành Lộc
 * @since : 10/12/2022, Wed
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class LoginDTO implements Serializable {
    @NotBlank(message = "Email là bắt buộc")
    @Email(message = "Email không hợp lệ",regexp = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}")
    private String email;

    @NotBlank(message = "Mật khẩu là bắt buộc")
    private String password;

}
