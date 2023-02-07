package com.schedule.app.controller;

import com.schedule.app.models.wrapper.ObjectResponseWrapper;
import com.schedule.app.utils.Extensions;
import com.schedule.app.utils.SendMail;
import lombok.experimental.ExtensionMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping
@ExtensionMethod(Extensions.class)
public class InitAPI extends BaseAPI {

    private static final Logger logger = LoggerFactory.getLogger(InitAPI.class);

    @GetMapping("/")
    public ObjectResponseWrapper checkAPI() {
        return ObjectResponseWrapper.builder().status(1).message("Schedule Service REST API").build();
    }


    @GetMapping("/send-mail")
    public ObjectResponseWrapper checkAPI2() throws MessagingException, UnsupportedEncodingException {
        SendMail.sendMailActiveAccountGG("nguyenthanhlocpc@gmail.com","43434","fdfdfdf");
        return ObjectResponseWrapper.builder().status(1).message("Send mail").build();
    }


}
