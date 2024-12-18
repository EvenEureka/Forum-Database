package com.zhihu.zhihubackend;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {

    @Test
    public void testGen(){
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",1);
        claims.put("username","zhihu");
        // 生成jwt代码
        String token = JWT.create()
                .withClaim("user",claims) //添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60)) //添加过期时间
                .sign(Algorithm.HMAC256("zhihu")); //指定算法，配置密钥
        System.out.println(token);
    }

    @Test
    public void testParse(){
        // 定义字符串，模拟用户传递过来的token
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                ".eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6InpoaWh1In0sImV4cCI6MTcxNjgwMzA2Nn0" +
                ".Cvp7TCoaJ4XnqJs4mc3vYmfMapu_nR_7ZQ1pyUArKUw";

        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("zhihu")).build();
        // 验证token，生成一个解析后的jwt对象
        DecodedJWT jwt = jwtVerifier.verify(token);
        Map<String, Claim> claims = jwt.getClaims();
        System.out.println(claims.get("user"));

        //如果改了头部或载荷，验证失败
        // 如果改了密钥，验证失败
        // 过期验证失败
    }
}
