package com.schedule.app.security;

public interface IExternalServiceAuthenticator {

    AuthenticationWithToken authenticate(String token);

}
