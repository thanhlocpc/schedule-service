package com.schedule.app.models.responses;

import lombok.Data;

import java.util.Date;

@Data
public class JwtResponse {

    private String accessToken;
    private Date tokenExpDate;
    private String refreshToken;

}
