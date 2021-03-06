package com.wlkg.service;

import com.wlkg.client.UserClient;
import com.wlkg.common.enums.ExceptionEnums;
import com.wlkg.common.exception.WlkgException;
import com.wlkg.config.JwtProperties;
import com.wlkg.entity.UserInfo;
import com.wlkg.pojo.User;
import com.wlkg.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {

    @Autowired
    private JwtProperties prop;

    @Autowired
    private UserClient userClient;

    //创建日志
//    private static Logger logger = LoggerFactory.getLogger(AuthService.class);

    public String login(String username, String password) {
//        System.out.println("password"+password);
        try {
            // 校验用户名和密码
            User user = userClient.queryUsernameAndPassword(username, password);
            if (user == null) {
                throw new WlkgException(ExceptionEnums.INVALID_USERNAME_PASSWORD);
            }
            // 生成token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), username), prop.getPrivateKey(), prop.getExpire());
            return token;
        }catch (Exception e){
//            logger.error("[授权中心] 用户名或者密码有误，用户名称：{}", username, e);
            throw new WlkgException(ExceptionEnums.INVALID_USERNAME_PASSWORD);
        }
    }
}
