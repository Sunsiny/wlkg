package com.wlkg.utils;



import com.wlkg.entity.UserInfo;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;

public class JwtUtilsTest {
    private static final String pubKeyPath = "C:\\tmp\\rsa\\rsa.pub";

    private static final String priKeyPath = "C:\\tmp\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }
//测试解析token
    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJOYW1lIjoiamFjayIsImV4cCI6MTUyNzMzMDY5NX0.VpGNedy1z0aR262uAp2sM6xB4ljuxa4fzqyyBpZcGTBNLodIfuCNZkOjdlqf-km6TQPoz3epYf8cc_Rf9snsGdz4YPIwpm6X14JKU9jwL74q6zy61J8Nl9q7Zu3YnLibAvcnC_y9omiqKN8-iCi7-MvM-LwVS7y_Cx9eu0aaY8E";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}