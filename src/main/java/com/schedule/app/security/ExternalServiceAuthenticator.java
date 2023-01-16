package com.schedule.app.security;


import com.schedule.app.utils.ScheduleServiceException;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


//@ExtensionMethod(Extensions.class)
@Component
public class ExternalServiceAuthenticator implements IExternalServiceAuthenticator {

    private HttpHeaders headers;
    private Map<String, String> data;

    private final RestTemplate restTemplate;
    private final ScheduleServiceException scheduleServiceException;

    @Autowired
    public ExternalServiceAuthenticator(RestTemplate restTemplate, ScheduleServiceException scheduleServiceException) {
        this.restTemplate = restTemplate;
        this.scheduleServiceException = scheduleServiceException;
    }

    private HttpHeaders getBasicHeader() {
        if (headers == null) {
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        return headers;
    }

    public Map<String, String> getData() {
        if (data == null) {
            data = new HashMap<>();
        }
        return data;
    }

    @Override
    public AuthenticationWithToken authenticate(String token) {
//        getData().put("token", token);
//
//        HttpEntity<Object> request = new HttpEntity<>(data, getBasicHeader());
//
//        ResponseEntity<AuthResponseWrapper> entity = restTemplate.postForEntity(productServiceProperties.getAuthUrl(),
//                request, AuthResponseWrapper.class);
//
//        AuthenticationWithToken authenticationWithToken;
//
//        if (entity.getStatusCode().is2xxSuccessful()) {
//            AuthResponseWrapper authResponseWrapper = entity.getBody();
//            Auth data = Objects.requireNonNull(authResponseWrapper).getData();
//            if (data == null) {
//                throw new BadCredentialsException("Token không hợp lệ");
//            }
//            String authStr = String.join(",", data.getUserRoles());
//            authenticationWithToken = new AuthenticationWithToken(data, null,
//                    AuthorityUtils.commaSeparatedStringToAuthorityList(authStr));
//        } else {
//            throw new TmdtServiceException(Objects.requireNonNull(entity.getBody()).getMessage());
//        }
//        return authenticationWithToken;

        return new AuthenticationWithToken(data, null,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ADMIN_PRODUCT"));

    }

}
