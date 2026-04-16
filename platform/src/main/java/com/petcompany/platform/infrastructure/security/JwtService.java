package com.petcompany.platform.infrastructure.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * JWT服务类
 */
@Service
@Slf4j
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire}")
    private Long expire;

    /**
     * 生成令牌
     */
    public String generateToken(Map<String, Object> claims) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        Date now = new Date();
        Date expiresAt = new Date(now.getTime() + expire * 1000);

        com.auth0.jwt.JWTCreator.Builder builder = JWT.create()
                .withIssuedAt(now)
                .withExpiresAt(expiresAt);

        // 添加自定义声明
        if (claims != null) {
            claims.forEach((key, value) -> {
                if (value instanceof String) {
                    builder.withClaim(key, (String) value);
                } else if (value instanceof Integer) {
                    builder.withClaim(key, (Integer) value);
                } else if (value instanceof Long) {
                    builder.withClaim(key, (Long) value);
                } else if (value instanceof Double) {
                    builder.withClaim(key, (Double) value);
                } else if (value instanceof Boolean) {
                    builder.withClaim(key, (Boolean) value);
                }
            });
        }

        return builder.sign(algorithm);
    }

    /**
     * 验证令牌
     */
    public DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    /**
     * 解析令牌
     */
    public Map<String, Claim> parseToken(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        return decodedJWT.getClaims();
    }

}
