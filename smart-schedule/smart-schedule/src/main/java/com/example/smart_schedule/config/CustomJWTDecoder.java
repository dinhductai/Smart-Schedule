package com.example.smart_schedule.config;
import com.example.smart_schedule.dto.request.IntrospectRequest;
import com.example.smart_schedule.dto.response.IntrospectResponse;
import com.example.smart_schedule.exception.handle.TokenInvalid;
import com.example.smart_schedule.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.naming.AuthenticationException;
import java.util.Objects;

@Component
public class CustomJWTDecoder implements JwtDecoder {

    @NonFinal
    @Value("${jwt.secret}")
    protected String SIGNED_KEY;
    @Autowired
    private AuthenticationService authenticationService;
    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        try{
//            check xem token còn hiệu lực ko
            IntrospectResponse checkToken = authenticationService.introspect(new IntrospectRequest(token));
            if(!checkToken.isValid()){
                throw new TokenInvalid("Token invalid");
            }

        } catch (AuthenticationException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        if(Objects.isNull(nimbusJwtDecoder)){
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNED_KEY.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}