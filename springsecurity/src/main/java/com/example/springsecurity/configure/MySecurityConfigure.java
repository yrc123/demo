package com.example.springsecurity.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;
import java.util.List;

@Configuration
public class MySecurityConfigure extends WebSecurityConfigurerAdapter {

    UserDetailsService userDetailsService=null;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
//        auth.inMemoryAuthentication().withUser("admin").password("123");

        this.userDetailsService = auth.inMemoryAuthentication().getUserDetailsService();
//        ((InMemoryUserDetailsManager)userDetailsService).createUser(new User("admin","123",null));

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
//        http.formLogin().disable();
        http.authorizeRequests().antMatchers("/*").permitAll();
        super.configure(http);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public UserDetailsService getMyUserDetailsService(){
        return userDetailsService;

    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Autowired
    @Qualifier("springSecurityFilterChain")
    Filter springSecurityFilterChain;
    @Bean
    RememberMeServices rememberMeServices(){
        //获取remeberme服务器
        FilterChainProxy chain = (FilterChainProxy) springSecurityFilterChain;
        List<SecurityFilterChain> filterChains = chain.getFilterChains();
        List<Filter> filterList = filterChains.get(0).getFilters();
        UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = null;
        for (Filter filter : filterList) {
            if(filter.getClass()==(UsernamePasswordAuthenticationFilter.class)){
                usernamePasswordAuthenticationFilter= (UsernamePasswordAuthenticationFilter) filter;
            }
        }
        RememberMeServices rememberMeServices = usernamePasswordAuthenticationFilter.getRememberMeServices();
        return rememberMeServices;
    }
}
