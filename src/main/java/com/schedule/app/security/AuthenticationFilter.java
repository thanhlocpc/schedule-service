package com.schedule.app.security;

import com.google.gson.JsonObject;
import com.nimbusds.jwt.JWTClaimsSet;
//import com.nlu.tutor.entities.Token;
import com.schedule.app.utils.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class AuthenticationFilter extends GenericFilterBean {


    private final String HTTP_REQUEST_HEADER_NAME;
    private final AuthenticationManager authenticationManager;


    public AuthenticationFilter(AuthenticationManager authenticationManager, String requestHeaderName) {
        this.authenticationManager = authenticationManager;
        this.HTTP_REQUEST_HEADER_NAME = requestHeaderName;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        System.out.println("[Request] " + httpRequest.getRemoteAddr() + " - " + httpRequest.getMethod() + " - " + httpRequest.getRequestURI());

//        Optional<String> token = Optional.ofNullable(httpRequest.getHeader(HTTP_REQUEST_HEADER_NAME));
        Object d = httpRequest.getHeaderNames();
        final String authorizationHeader = httpRequest.getHeader(HTTP_REQUEST_HEADER_NAME);
        UserPrincipal user = null;
//        Token token2 = null;
        JwtUtil jwtUtil = new JwtUtil();
        try {
            if (authorizationHeader != null) {
                if (null == authorizationHeader) {
                    throw new InternalAuthenticationServiceException("Thiếu header token.");
                }
                String jwt = authorizationHeader.substring(6);
                user = jwtUtil.getUserFromToken(jwt);
                JWTClaimsSet claims = jwtUtil.getClaimsFromToken(jwt);
                if (null == claims) {
                    throw new InternalAuthenticationServiceException("Token không hợp lệ.");
                }
                boolean isTokenExpired = jwtUtil.isTokenExpired(claims);
                if (!isTokenExpired) {
                    throw new InternalAuthenticationServiceException("Token hết hạn.");
                }
            }

            if (null != user) {
                Set<GrantedAuthority> authorities = new HashSet<>();
                user.getAuthorities().forEach(
                        p -> authorities.add(new SimpleGrantedAuthority((String) p))
                );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(servletRequest, servletResponse);

        } catch (InternalAuthenticationServiceException ea) {
            SecurityContextHolder.clearContext();
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("timestamp", LocalDateTime.now().toString());
            jsonObject.addProperty("status", httpResponse.getStatus());
            jsonObject.addProperty("message", ea.getMessage());
            jsonObject.addProperty("path", httpRequest.getServletPath());
            httpResponse.getWriter().write(jsonObject.toString());
            httpResponse.getWriter().flush();
        } catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("timestamp", LocalDateTime.now().toString());
            jsonObject.addProperty("status", httpResponse.getStatus());
            jsonObject.addProperty("message", "Error: Unauthorized");
            jsonObject.addProperty("path", httpRequest.getServletPath());
            httpResponse.getWriter().write(jsonObject.toString());
            httpResponse.getWriter().flush();
        } catch (StringIndexOutOfBoundsException t) {
            SecurityContextHolder.clearContext();
            httpResponse.setContentType("application/json;charset=UTF-8");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("timestamp", LocalDateTime.now().toString());
            jsonObject.addProperty("status", httpResponse.getStatus());
            jsonObject.addProperty("message", "Token không hợp lệ.");
            jsonObject.addProperty("path", httpRequest.getServletPath());
            httpResponse.getWriter().write(jsonObject.toString());
            httpResponse.getWriter().flush();
        }
    }

    private void processTokenAuthentication(String token) {
        PreAuthenticatedAuthenticationToken requestAuthentication = new PreAuthenticatedAuthenticationToken(token, null);
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
        }
        SecurityContextHolder.getContext().setAuthentication(responseAuthentication);
    }

}
