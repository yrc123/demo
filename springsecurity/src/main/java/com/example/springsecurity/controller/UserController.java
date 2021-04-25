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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserController {
//    @Autowired
//    AuthenticationManager manager;
    @GetMapping("/user")
    @ResponseBody
    public String user(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "用户名 :"+authentication.getName();
    }
    @Autowired
    InMemoryUserDetailsManager service;
//    @Autowired
//    AuthenticationManager authenticationManager;
    @PostMapping("/register")
    @ResponseBody
    public String register(String username,String password){
//        System.out.println(service.loadUserByUsername("yrc"));
//        System.out.println(service);
        service.createUser(new User(username,password,new ArrayList<>()));
        if(service.userExists(username)){
            UserDetails userDetails = service.loadUserByUsername(username);
            return "注册成功\n用户名 :"+userDetails.getUsername()+"\n密码："+userDetails.getPassword();
        }
////        System.out.println(authenticationManager);
       return "注册失败";
    }
    @GetMapping("/register")
    public String register(){
        return "/register.html";
    }
//    @Autowired
//    @Qualifier("springSecurityFilterChain")
//    Filter springSecurityFilterChain;
//    @Bean
//    RememberMeServices rememberMeServices(){
//        FilterChainProxy chain = (FilterChainProxy) springSecurityFilterChain;
//        List<SecurityFilterChain> filterChains = chain.getFilterChains();
//        List<Filter> filterList = filterChains.get(0).getFilters();
//        UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = null;
//        for (Filter filter : filterList) {
//                if(filter.getClass()==(UsernamePasswordAuthenticationFilter.class)){
//                    usernamePasswordAuthenticationFilter= (UsernamePasswordAuthenticationFilter) filter;
//                }
//        }
//        RememberMeServices rememberMeServices = usernamePasswordAuthenticationFilter.getRememberMeServices();
//        return rememberMeServices;
//    }
//    @GetMapping("/login")
//    @ResponseBody
//    public String login(String username,String password){
//        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
//        Authentication authenticate = manager.authenticate(token);
//        SecurityContextHolder.getContext().setAuthentication(authenticate);
//        return SecurityContextHolder.getContext().getAuthentication().getName();
//    }
}
