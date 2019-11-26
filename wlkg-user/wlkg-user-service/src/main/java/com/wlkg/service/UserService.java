package com.wlkg.service;

import com.wlkg.common.enums.ExceptionEnums;
import com.wlkg.common.exception.WlkgException;
import com.wlkg.common.utils.CodecUtils;
import com.wlkg.common.utils.NumberUtils;
import com.wlkg.mapper.UserMapper;
import com.wlkg.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;

    static final String KEY_PREFIX = "user:code:phone:";

    static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public Boolean checkData(String data, Integer type) {
        User record = new User();
        switch (type){
            case 1:
                record.setUsername(data);
                break;
            case 2:
                record.setPhone(data);
            default:
                return null;
        }
         User users = userMapper.selectOne(record);
        System.out.println("user:"+users);
        if(users != null){
            return true;
        }
        return false;
    }
/*
- 生成随机验证码
- 将验证码保存到Redis中，用来在注册的时候验证
- 发送验证码到`wlkg-sms `服务，发送短信
因此，我们需要引入Redis和AMQP：
 */
    public Boolean sendVerifyCode(String phone) {
    // 生成验证码
    String code = NumberUtils.generateCode(6);
//    System.out.println("随机的验证码code:"+code);
//    System.out.println("phone:"+phone);
    try {
        // 发送短信
        Map<String, String> msg = new HashMap<>();
        msg.put("phone", phone);
        msg.put("code", code);
//        System.out.println("msg:+++"+msg);
        amqpTemplate.convertAndSend("wlkg.sms.exchange", "sms.verify.code", msg);
        // 将code存入redis
        this.redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
        return true;
    } catch (Exception e) {
        logger.error("发送短信失败。phone：{}， code：{}", phone, code);
        return false;
    }
}

    /*
    注册
    - 1）校验短信验证码
    - 2）生成盐
    - 3）对密码加密
    - 4）写入数据库
    - 5）删除Redis中的验证码
     */
    public void register(User user, String code) {
        String key = KEY_PREFIX + user.getPhone();
        System.out.println("key:+++"+key);
        // 从redis取出验证码
        String codeCache = this.redisTemplate.opsForValue().get(key);
        System.out.println("redis_code:"+codeCache);
        System.out.println("code:"+code);
        // 检查验证码是否正确
        if (!code.equals(codeCache)) {
            // 不正确，返回
            throw new WlkgException(ExceptionEnums.INVALID_VERFIY_CODE);
        }

        // 生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        // 对密码进行加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));
        // 写入数据库
        user.setCreated(new Date());
        System.out.println("user:"+user);
        // 如果注册成功，删除redis中的code
        if (userMapper.insertSelective(user) == 1) {
            redisTemplate.delete(key);
        }
        System.out.println("注册成功了");
    }

    /*
    登录
     */
    public User queryUser(String username, String password) {
        // 查询
        User record = new User();
        record.setUsername(username);
        User user = this.userMapper.selectOne(record);
        // 校验用户名
        if (user == null) {
            throw new WlkgException(ExceptionEnums.INVALID_USERNAME_PASSWORD);
        }
        // 校验密码
        if (!user.getPassword().equals(CodecUtils.md5Hex(password, user.getSalt()))) {
            throw new WlkgException(ExceptionEnums.INVALID_USERNAME_PASSWORD);
        }
        // 用户名密码都正确
        return user;
    }
}
