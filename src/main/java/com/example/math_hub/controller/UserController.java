package com.example.math_hub.controller;
import com.example.math_hub.pojo.Result;
import com.example.math_hub.pojo.User;
import com.example.math_hub.service.UserService;
import com.example.math_hub.utils.JwtUtil;
import com.example.math_hub.utils.Md5Util;
import com.example.math_hub.utils.ThreadLocalUtil;
import jakarta.annotation.Resource;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private void validateUsername(String username) {
        if (!username.matches("^[a-zA-Z0-9]{5,16}$")) {
            throw new IllegalArgumentException("用户名格式错误，请输入5-16位字母或数字"+username);
        }
    }

    private void validatePassword(String password) {
        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d).{5,16}$")) {
            throw new IllegalArgumentException("密码必须包含字母和数字，长度为5-16位");
        }
    }

    @PostMapping("/register")
    public Result register(@RequestParam String username, @RequestParam String password) {
        validateUsername(username);
        validatePassword(password);

        User u = userService.findByUserName(username);
        if (u == null) {
            userService.register(username, password);
            return Result.success();
        } else {
            return Result.error("用户名已被占用！");
        }
    }

    @PostMapping("/login")
    public Result<String> login(String username,String password){
        User loginUser =  userService.findByUserName(username);

        if(loginUser==null){
            return Result.error("用户名错误！");
        }
        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())){
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",loginUser.getId());
            claims.put("username",loginUser.getUsername());
            String token = JwtUtil.genToken(claims);
            ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,1, TimeUnit.HOURS);
            return  Result.success(token);
        }
        return Result.error("密码错误！");
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(){
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User user = userService.findByUserName(username);
        return Result.success(user);
    }

    @PutMapping("/update")
    public Result update(@RequestBody@Validated User user){
      userService.update(user);
      return  Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String,String> params,@RequestHeader("Authorization") String token){
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");
        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("缺少必要参数！");
        }
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User loginUser = userService.findByUserName(username);
        if(!loginUser.getPassword().equals(Md5Util.getMD5String(oldPwd))){
            return Result.error("原密码不正确！");
        }
        if(!rePwd.equals(newPwd)){
            return Result.error("两次填写的新密码不一致！");
        }
        userService.updatePwd(newPwd);
        ValueOperations<String,String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }
}
