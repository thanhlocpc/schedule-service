package com.schedule.app.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.schedule.app.security.UserPrincipal;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Date;

/**
 * @author : Thành Lộc
 * @since : 1/12/2023, Wed
 **/
@Component
public class JwtUtil {
    private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final String USER = "user";
    private static final String SECRET = "nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCld9VGHZ3GHMlsndhbd99TQMgigUBBpTvTjZS7WvVZBBJpSBJSbtmmrNHapYUKanPbIP0hAm3q8RbIM\\n1/em9S2Ok36FAXpxXsuBCBwBnEek///DdqAQeKHk4dR3gjWCT7UjtkCi+Jo51Vr+noL2oIPgccaPmPDTW99zYv2sv3uvp26eNFNMm89Mljanr/5yU7NTgpZzhlpjID8hHnDXmRnrxljaYroaiKrdV/LMmcbd0uTghIr0/cg+loIfr9zbol23C7gA68i9J3zRLHnRtSXk6QtcxengllUTFB5Yul5Q7geGhnuob5nSwvz08kX5SIbXOWuQXYP7jmoDT";


    public String generateToken(UserPrincipal user) {
        String token = null;
        try {
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
            builder.claim(USER, user);
            builder.expirationTime(generateExpirationDate());
            JWTClaimsSet claimsSet = builder.build();
            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            JWSSigner signer = new MACSigner(SECRET.getBytes());
            signedJWT.sign(signer);
            token = signedJWT.serialize();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return token;
    }

    public Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + 864000000);
    }
    public JWTClaimsSet getClaimsFromToken(String token) {
        JWTClaimsSet claims = null;
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(SECRET.getBytes());
            if (signedJWT.verify(verifier)) {
                claims = signedJWT.getJWTClaimsSet();
            }
        } catch (ParseException | JOSEException e) {
            logger.error(e.getMessage());
        }
        return claims;
    }

    public UserPrincipal getUserFromToken(String token) {
        UserPrincipal user = null;
        try {
            JWTClaimsSet claims = getClaimsFromToken(token);
            if (claims != null && isTokenExpired(claims)) {
                JSONObject jsonObject = (JSONObject) claims.getClaim(USER);
                user = new ObjectMapper().readValue(jsonObject.toJSONString(), UserPrincipal.class);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return user;
    }

    public Date getExpirationDateFromToken(JWTClaimsSet claims) {
        return claims != null ? claims.getExpirationTime() : new Date();
    }

    public boolean isTokenExpired(JWTClaimsSet claims) {
        return getExpirationDateFromToken(claims).after(new Date());
    }
}
