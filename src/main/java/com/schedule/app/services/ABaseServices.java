package com.schedule.app.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.schedule.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author : Thành Lộc
 * @since : 10/5/2022, Wed
 **/
@Service
public abstract class ABaseServices {

    protected static final Logger logger = Logger.getLogger(ABaseServices.class.getName());
    private Gson gson;

    private final HttpHeaders httpHeaders;

    {
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }


    // repositoty
    @Autowired
    protected ITokenRepository tokenRepository;

    @Autowired
    protected IUserRepository userRepository;

    @Autowired
    protected IUserLoginRepository userLoginRepository;

    @Autowired
    protected IRoleRepository roleRepository;

    @Autowired
    protected IUserRoleRepository userRoleRepository;

    @Autowired
    protected ISubjectScheduleResultRepository subjectScheduleResultRepository;



    // service
    @Autowired
    protected IUserService userService;







}
