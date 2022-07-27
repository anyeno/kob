package com.kob.backend.service.impl.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureProxyUntrustedEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> getToken(String username, String password) {
        //将用户名和密码封装成一个类  存加密之后的密码
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username,password);

        //如果登陆失败会自动处理
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        UserDetailsImpl lohinUser =  (UserDetailsImpl) authenticate.getPrincipal();
        User user = lohinUser.getUser();

        //根据userid生成jwt
        String jwt = JwtUtil.createJWT(user.getId().toString());

        Map<String,String> map = new HashMap<>();
        map.put("error_message","success");
        map.put("token",jwt);

        return map;
    }
}
