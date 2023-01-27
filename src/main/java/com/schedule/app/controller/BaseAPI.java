package com.schedule.app.controller;

import com.google.gson.Gson;
import com.schedule.app.repository.ISubjectScheduleResultRepository;
import com.schedule.app.services.ISubjectScheduleService;
import com.schedule.app.services.IUserService;
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



    //service
    @Autowired
    protected IUserService userService;

    @Autowired
    protected ISubjectScheduleService subjectScheduleService;


    //repository
    @Autowired
    protected ISubjectScheduleResultRepository courseRegisterResultRepository;
}
