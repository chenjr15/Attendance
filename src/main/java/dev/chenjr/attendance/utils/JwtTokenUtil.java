package dev.chenjr.attendance.utils;


import dev.chenjr.attendance.dao.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
@Data
@Slf4j
public class JwtTokenUtil {

    private static Key KEY = null;
    @Value("${token.secret}")
    private String secret;
    @Value("${token.expireTime}")
    private Long expiration;

    @Value("${token.header}")
    private String header;

    /**
     * 生成token令牌
     *
     * @param user 用户实体
     * @return 令token牌
     */
    public String generateToken(User user) {
        log.info("[JwtTokenUtils] generateToken " + user.getId().toString());
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("sub", user.getLoginName());
        claims.put("uid", user.getId().toString());
        claims.put("created", new Date());

        return generateToken(claims);
    }


    /**
     * 从令牌中获取用户名
     *
     * @param token 令牌
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.get("sub", String.class);
            log.info("get username from token:" + username);
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 从令牌中获取UID
     *
     * @param token 令牌
     * @return 用户id
     */
    public Long getUidFromToken(String token) {
        Long uid;
        try {
            Claims claims = getClaimsFromToken(token);
            String uidString = claims.get("uid", String.class);
            uid = Long.valueOf(uidString);
            log.info("get uid from:" + uid);
        } catch (Exception e) {
            uid = null;
        }
        return uid;
    }

    /**
     * 判断令牌是否过期
     *
     * @param token 令牌
     * @return 是否过期
     */
    public Boolean isTokenExpired(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新令牌
     *
     * @param token 原令牌
     * @return 新令牌
     */
    public String refreshToken(String token) {
        String refreshedToken;
        try {
            Claims claims = getClaimsFromToken(token);
            claims.put("created", new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 验证令牌
     *
     * @param token       令牌
     * @param userDetails 用户
     * @return 是否有效
     */
    public Boolean validateToken(String token, UserDetails userDetails) {

        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) &&
                !isTokenExpired(token));
    }


    /**
     * 从claims生成令牌,如果看不懂就看谁调用它
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration);
        return Jwts.builder().setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(getKeyInstance(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 从令牌中获取数据声明,如果看不懂就看谁调用它
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;

        try {

            claims = Jwts.parserBuilder()
                    .setSigningKey(getKeyInstance())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception ignored) {
        }
        return claims;
    }


    private Key getKeyInstance() {
        if (KEY == null) {
            synchronized (JwtTokenUtil.class) {
                if (KEY == null) {// 双重锁
                    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
                    KEY = new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
                }
            }
        }
        return KEY;
    }
}
