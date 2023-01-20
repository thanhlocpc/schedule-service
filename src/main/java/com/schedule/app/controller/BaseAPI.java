package com.schedule.app.controller;

import com.google.gson.Gson;
import com.schedule.app.services.IRefreshTokenService;
import com.schedule.app.services.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.logging.Logger;
import com.schedule.app.utils.JwtUtil;


/**
 * @author ThanhLoc
 * @created 1/16/2023
 */
public abstract class BaseAPI {
    protected static final Logger logger = Logger.getLogger(BaseAPI.class.getName());
    private Gson gson;


    // utils
    @Autowired
    protected JwtUtil jwtUtil;

//    @Autowired
//    protected ModelMapper mapper;



    @Autowired
    protected IUserService userService;
}
