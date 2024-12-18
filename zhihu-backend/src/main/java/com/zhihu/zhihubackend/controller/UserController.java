package com.zhihu.zhihubackend.controller;

import com.zhihu.zhihubackend.pojo.Result;
import com.zhihu.zhihubackend.pojo.User;
import com.zhihu.zhihubackend.service.UserService;
import com.zhihu.zhihubackend.utils.JwtUtil;
import com.zhihu.zhihubackend.utils.Md5Util;
import com.zhihu.zhihubackend.utils.ThreadLocalUtil;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password)
    {
        //查询用户
        User u = userService.findByUserName(username);
        if(u==null)
        {
            //没被占用
            //注册
            userService.register(username,password);
            return Result.success();
        }else{
            //占用
            return Result.error("用户名已被占用");
        }
    }

    @PostMapping("/login")
    public Result login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password){
        // 根据用户名查询用户
        User u = userService.findByUserName(username);

        // 判断用户是否存在
        if(u==null){
            return Result.error("用户名错误");
        }

        //判断密码是否正确
        if(Md5Util.getMD5String(password).equals(u.getPassword())){
            // 登陆成功
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",u.getId());
            claims.put("username",u.getUsername());
            String token = JwtUtil.genToken(claims);

            // 把token存储到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,12, TimeUnit.HOURS);
            return Result.success(token);
        }

        return Result.error("密码错误");
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/){
        // 根据用户名查询用户
//        Map<String,Object> claims = JwtUtil.parseToken(token);
//        String username = claims.get("username").toString();
//        return Result.success(userService.findByUserName(username));
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = map.get("username").toString();
        User u = userService.findByUserName(username);
        return Result.success(u);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user){
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params, @RequestHeader("Authorization") String token){
        //1.检验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要的参数");
        }

        //原密码是否正确
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = map.get("username").toString();
        User u = userService.findByUserName(username);
        if(!Md5Util.getMD5String(oldPwd).equals(u.getPassword())){
            return Result.error("原密码填写不正确");
        }

        //newPwd和rePwd是否一样
        if(!rePwd.equals(newPwd))
        {
            return Result.error("两次填写的新密码不一样");
        }

        //2.调用service完成密码更新
        userService.updatePwd(newPwd);

        //删除redis中响应的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }
}
