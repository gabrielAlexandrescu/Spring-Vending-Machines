//package com.VendingMachines.root.config;
//
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//
//@Configuration
//@EnableWebSecurity
//@ComponentScan(basePackages = "com.example.myspring")
//public class MySecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception{
//        http
//                .authorizeRequests()
//                .antMatchers("/dashboard").authenticated()
//                .antMatchers("/home").permitAll()
//                .and()
//                .formLogin()
//                .and()
//                .httpBasic();
//
//    }
//
//
//
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http
////                .authorizeRequests()
////                // .antMatchers("/dashboard").authenticated()
////                // .antMatchers("/home").permitAll()
////                .antMatchers().permitAll()
////                .and()
////                .formLogin()
////                .and()
////                .httpBasic();
////        System.out.println("From MySecurityConfig");
////        return http.build();
////    }
//
//}
