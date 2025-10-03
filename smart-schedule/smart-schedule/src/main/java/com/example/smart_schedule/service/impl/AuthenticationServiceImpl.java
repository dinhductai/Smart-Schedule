package com.example.smart_schedule.service.impl;
import com.example.smart_schedule.dto.request.AuthenticationRequest;
import com.example.smart_schedule.dto.request.IntrospectRequest;
import com.example.smart_schedule.dto.request.LogoutRequest;
import com.example.smart_schedule.dto.response.AuthenticationResponse;
import com.example.smart_schedule.dto.response.IntrospectResponse;
import com.example.smart_schedule.entity.User;
import com.example.smart_schedule.enumeration.RoleName;
import com.example.smart_schedule.exception.handle.ExpiredJwtException;
import com.example.smart_schedule.entity.InvalidatedToken;
import com.example.smart_schedule.exception.handle.UserNotFoundException;
import com.example.smart_schedule.mapper.RoleMapper;
import com.example.smart_schedule.repository.InvalidatedTokenRepository;
import com.example.smart_schedule.repository.PermissionRepository;
import com.example.smart_schedule.repository.RoleRepository;
import com.example.smart_schedule.repository.UserRepository;
import com.example.smart_schedule.service.AuthenticationService;
import com.example.smart_schedule.util.AuthenticationUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.naming.AuthenticationException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    RoleMapper roleMapper;
    PermissionRepository permissionRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.secret}") //secret key ngẫu nhiên 32 bytes, vì HMAC cần key có độ dài tối thiểu để an toàn,
    // dùng HS256,512 thì cần >= 32 bytes, càng dài càng tốt
    protected String SIGNED_KEY;


    //hàm xác thực tài khoản người dùng bằng email và password
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws JOSEException {
        //tìm thông tin người dùng bằng email
        User user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(()->new UserNotFoundException("User not found with email: "
                        + authenticationRequest.getEmail()));
        //nếu account bị block hay đã xóa thì thì từ chối
        AuthenticationUtil.checkAccountDeleteOrBlock(user);

        //check password vừa đc gửi về (raw pass) với password trong db đã được mã hóa
        AuthenticationUtil.checkPassword(authenticationRequest.getPassword(),user.getPassword());


        //lấy các role thuộc user đang đăng nhập để phân quyền, vì một acc có thể có nhiều role
        List<RoleName> listRoleUser = userRepository.findRoleNamesByUserId(user.getUserId());
        //build scope
        String scope = buildScope(listRoleUser);
        //sinh token dựa vào data truyền vào
        String token = generateToken(user.getUserId(),scope,user.getEmail());
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }


    //hàm kiểm tra token hợp lệ,trạng thái của token
    @Override
    public IntrospectResponse introspect(IntrospectRequest introspectRequest) throws AuthenticationException {
        String token = introspectRequest.getToken();
        boolean isInvalid = true;
        try {
            verifyToken(token);
        }catch (ExpiredJwtException e) {
            //nếu gặp trường hợp token đã bị logout thì chỉ cần đổi thành false là đc,ko cần trả exception
            isInvalid = false;
        }
        return new IntrospectResponse(isInvalid);
    }

    @Override
    public void logout(LogoutRequest logoutRequest) throws ParseException {
        SignedJWT signedJWT = verifyToken(logoutRequest.getToken());
        //lấy claim set ra
        String jwtTokenId = signedJWT.getJWTClaimsSet().getJWTID();
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .token(jwtTokenId)
                .expiryTime(expirationTime)
                .build();
        invalidatedTokenRepository.save(invalidatedToken);
    }

    private SignedJWT verifyToken(String token) {
        try {
            JWSVerifier verifier = new MACVerifier(SIGNED_KEY.getBytes());
            SignedJWT signedJWT = SignedJWT.parse(token);
            //kiểm tra xác thực
            boolean checkVerified = signedJWT.verify(verifier);
            //lấy thời gian hết hạn
            Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (!checkVerified && expiryTime.after(new Date())) {
                throw new ExpiredJwtException("Token expired");
            }else if (invalidatedTokenRepository.existsByToken(signedJWT.getJWTClaimsSet().getJWTID())){
                throw new ExpiredJwtException("Token expired");
            }else return signedJWT;

        } catch (JOSEException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

    //hàm sinh token bằng HS512 và secret key
    private String generateToken(Long userId,String roleString,String email) throws JOSEException {
        //thuật toán mã hóa header
        //dùng hs512 vì có độ dài lớn hơn, khó bị tấn công hơn, nhanh hơn so với thuật toán bất đối xứng
        //-> phù hợp RestApi, nơi server vừa tạo vừa xác thực,
        //phổ biến và dễ dùng,dễ triển khai
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        //set claim
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(userId))//đại diện cho user đăng nhập
                .issuer("website.com")//xác định token issue từ đâu ra, xác định nguồn gốc của token
                .issueTime(new Date()) //thời gian tạo
                .expirationTime(new Date(
                        //set thời gian token hết hạn là 1 giờ sau đó
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("email",email)
                .claim("scope",roleString)
                .build();

        //set claim cho payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header,payload);
        try {
            //ký với secret key
            jwsObject.sign(new MACSigner(SIGNED_KEY.getBytes(StandardCharsets.UTF_8)));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw e;
        }
    }

    //hàm build scope cho token
    private String buildScope(List<RoleName> listRoleUser){
        StringBuilder scopeBuilder = new StringBuilder();
        // Thêm role vào đầu tiên
        for (RoleName role : listRoleUser) {
            if (scopeBuilder.length() > 0) {
                scopeBuilder.append(" "); // thêm dấu cách nếu không phải phần tử đầu
            }
            scopeBuilder.append(role);
        }

        //gộp permission từ tất cả role, loại bỏ trùng lặp
        Set<String> uniquePermissions = new HashSet<>(); //set để loại bỏ trùng lặp
        for (RoleName role : listRoleUser) {
            List<String> listPermissionByRole = roleRepository.findPermissionNameByRoleName(role);
            uniquePermissions.addAll(listPermissionByRole); //thêm tất cả permission, tự động loại trùng
        }

        //thêm permission vào scope, tách permission riêng ra
        for (String permission : uniquePermissions) {
            if (scopeBuilder.length() > 0) {
                scopeBuilder.append(" "); // Thêm dấu cách
            }
            scopeBuilder.append(permission);
        }
        return scopeBuilder.toString();
    }
}