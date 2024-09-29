package com.zjn.mall.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.Verification;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 张健宁
 * @ClassName JwtUtils
 * @Description TODO
 * @createTime 2024年09月29日 13:22:00
 */
public class JwtUtils {
    /**
     * JWT 的 密钥
     */
    private static final String key="NIBUKENENGZHIDAOWODEMIMA";
    private static final String parseKey = "userinfo";

    /**
     * 通过jwt创建token令牌
     * @param map
     * @return
     */
    public static String createToken(Map<String,Object> map){
        Map<String,Object> head=new HashMap<>();
        head.put("alg","HS256");
        head.put("typ","JWT");

        Date date=new Date();//发布日期
        Calendar instance = Calendar.getInstance();//获取当前时间
        instance.set(Calendar.SECOND,7200);//在当前时间的基础上添加7200秒
        Date time = instance.getTime();

        String token = JWT.create()
                .withHeader(head) //设置头
                .withIssuedAt(date) //设置发布日期
                .withExpiresAt(time) //设置过期时间
                .withClaim("userinfo", map) //设置个人信息
                .sign(Algorithm.HMAC256(key));//签名

        return token;
    }

    /**
     * 校验token
     * @param token
     * @return
     */
    public static boolean verify(String token){
        Verification require = JWT.require(Algorithm.HMAC256(key));
        try {
            require.build().verify(token);
            return true;
        }catch (Exception e){
            System.out.println("token错误");
            return false;
        }
    }

    /**
     * 根据token获取自定义的信息
     * @param token
     * @return
     */
    public static Object getInfo(String token){
        JWTVerifier build = JWT.require(Algorithm.HMAC256(key)).build();
        Claim claim = build.verify(token).getClaim(parseKey);
        return claim.asMap().get("username");
    }

}

