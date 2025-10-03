package com.example.smart_schedule.service;

import com.example.smart_schedule.dto.request.AuthenticationRequest;
import com.example.smart_schedule.dto.request.IntrospectRequest;
import com.example.smart_schedule.dto.request.LogoutRequest;
import com.example.smart_schedule.dto.response.AuthenticationResponse;
import com.example.smart_schedule.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import javax.naming.AuthenticationException;
import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws AuthenticationException, JOSEException;
    IntrospectResponse introspect(IntrospectRequest introspectRequest) throws AuthenticationException,JOSEException;
    void logout(LogoutRequest logoutRequest) throws AuthenticationException, ParseException;
}
