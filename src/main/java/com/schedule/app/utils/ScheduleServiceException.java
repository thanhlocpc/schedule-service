package com.schedule.app.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Data
@Component
@PropertySource(value = "classpath:application.yml")
@ConfigurationProperties(prefix = "schedule.service")
public class ScheduleServiceException {

//    private String loginUrl;

    private String swaggerUrl;

    private Boolean local;

}
