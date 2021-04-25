package com.example.springsecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
    @GetMapping("/user")
    @ResponseBody
    public String user(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "用户名 :"+authentication.getName();
    }
    @Autowired
    InMemoryUserDetailsManager userDetailsManager;
    @Autowired
    AuthenticationManager manager;
    @Autowired
    RememberMeServices rememberMeServices;
    @PostMapping("/register")
    @ResponseBody
    public String register(String username,String password){
        userDetailsManager.createUser(new User(username,password,new ArrayList<>()));
        if(userDetailsManager.userExists(username)){
            UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
            return "注册成功\n用户名 :"+userDetails.getUsername()+"\n密码："+userDetails.getPassword();
        }
       return "注册失败";
    }
    @GetMapping("/register")
    public String register(){
        return "/register.html";
    }
    @PostConstruct
    public void init(){
        register("admin","123");
    }

    //自定义登录
    @RequestMapping("/my_login")
    @ResponseBody
    public String login(String username, String password, Boolean rememberMe, HttpServletRequest req, HttpServletResponse resp){
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authenticate = manager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        if(rememberMe){
            rememberMeServices.loginSuccess(req,resp,authenticate);
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
