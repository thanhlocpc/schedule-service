package com.schedule.app.services;

import com.google.gson.Gson;
import com.schedule.app.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

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

    @Autowired
    protected ICourseRegistrationResultRepository courseRegistrationResultRepository;

    @Autowired
    protected
    IScoreTableRepository scoreRepository;

    @Autowired
    protected
    ISemesterRepository semesterRepository;


    // service
    @Autowired
    protected IUserService userService;







}
