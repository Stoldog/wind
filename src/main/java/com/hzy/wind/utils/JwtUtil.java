package com.hzy.wind.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by EduHzy-019 on 2018-05-16.
 */
public class JwtUtil {
    //默认的token密钥
    private static String base64SecurityKey = "1&$OAlAsh!wJCIJU";
    /**
     * JWT解析方法
     * 解析JSON WEB TOKEN
     */
    public static Claims parseJWT(String jsonWebToken, String base64Security){
        try
        {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    /**
     * JWT解析方法
     * 默认的base64SecurityKey
     * 解析JSON WEB TOKEN
     */
    public static Claims parseJWT(String jsonWebToken){
        String base64Security = base64SecurityKey;
        try
        {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(base64Security))
                    .parseClaimsJws(jsonWebToken).getBody();
            return claims;
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    /**
     * JWT构建方法
     * 构建JSON WEB TOKEN
     */
    public static <V> String createJWT(Claims claims, String base64Security,String name,V value)
    {
        //加密算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("role", claims.get("role"))
                .claim("room_id",claims.get("room_id"))
                .claim("unique_name", claims.get("unique_name"))
                .claim("user_id", claims.get("user_id"))
                .claim(name,value)//执行修改操作
                .setIssuer(claims.getIssuer())
                .setAudience(claims.getAudience())
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        builder.setExpiration(claims.getExpiration()).setNotBefore(claims.getNotBefore());

        //生成JWT
        return builder.compact();
    }

    /**
     * JWT构建方法
     * 构建JSON WEB TOKEN
     */
    public static <V> String createJWT(Claims claims,String name,V value)
    {
        //加密算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        String base64Security = base64SecurityKey;

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("role", claims.get("role"))
                .claim("room_id",claims.get("room_id"))
                .claim("unique_name", claims.get("unique_name"))
                .claim("user_id", claims.get("user_id"))
                .claim(name,value)//执行修改操作
                .setIssuer(claims.getIssuer())
                .setAudience(claims.getAudience())
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        builder.setExpiration(claims.getExpiration()).setNotBefore(claims.getNotBefore());

        //生成JWT
        return builder.compact();
    }

    /**
     * JWT构建方法
     * 默认的base64SecurityKey
     * 构建JSON WEB TOKEN
     */
    public static String createJWT(Integer userId, String name,String role,Long roomId,
                                   String audience, String issuer, long TTLMillis)
    {
        String base64Security = base64SecurityKey;
        //加密算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("role", role)
                .claim("room_id",roomId)
                .claim("unique_name", name)
                .claim("user_id", userId)
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }

        //生成JWT
        return builder.compact();
    }

    /**
     * JWT构建方法
     * 构建JSON WEB TOKEN
     */
    public static String createJWT(Integer userId, String name,String role,Long roomId,
                                   String audience, String issuer, long TTLMillis, String base64Security)
    {
        //加密算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Security);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                .claim("role", role)
                .claim("room_id",roomId)
                .claim("unique_name", name)
                .claim("user_id", userId)
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp).setNotBefore(now);
        }

        //生成JWT
        return builder.compact();
    }

    /**
     * 检查TOKEN是否过期
     * @return
     */
    public static boolean checkExpire(Claims claims){
        return claims.getExpiration().before(new Date(System.currentTimeMillis()));
    }
}
