package dev.chenjr.attendance.config.security;

import dev.chenjr.attendance.config.security.filter.JwtAuthTokenFilter;
import dev.chenjr.attendance.service.impl.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private UserDetailService myUserDetailsService;

    @Autowired
    private JwtAuthTokenFilter jwtAuthTokenFilter;

    public static final String HeaderString = "Authorization";

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 认证失败处理类
                //.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                // 基于token，所以不需要session,这里设置STATELESS(无状态)是在请求是不生成session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .exceptionHandling().and()
                //配置权限
                .authorizeRequests()
                //对于登录login  验证码captchaImage  允许匿名访问
//                .antMatchers("/auth/*").anonymous()
                // swagger
                .antMatchers(SWAGGER_WHITELIST).permitAll()
                .antMatchers(SYSTEM_WHITELIST).permitAll()
                // 假定一个order
                .antMatchers("/order").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")    //user角色和 admin角色都可以访问
                //admin角色可以访问
                .antMatchers("/system/user", "/system/role", "/system/menu").hasAnyRole("ADMIN")
                //  除上面外的所有请求全部需要鉴权认证
                //authenticated()要求在执 行该请求时，必须已经登录了应用
                .anyRequest().authenticated()
                //  禁用跨站csrf攻击防御，否则无法登陆成功，因为不使用session
                .and().csrf().disable();
        //登出功能
        httpSecurity.logout().logoutUrl("/logout");
        //  添加JWT  filter, 在每次http请求前进行拦截
        httpSecurity.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }


    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        //调用DetailsService完成用户身份验证              设置密码加密方式
        auth.userDetailsService(myUserDetailsService).passwordEncoder(getBCryptPasswordEncoder());
    }


    // 在通过数据库验证登录的方式中不需要配置此种密码加密方式, 因为已经在JWT配置中指定
    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // Swagger WHITELIST
    public static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/*",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/v3/api-docs",
            "/webjars/**"
    };

    // System WHITELIST
    public static final String[] SYSTEM_WHITELIST = {
            "/auth/login",
            "/users/signup", "/users",
            "/auth/password/*",
            "/randomString",
            "/randomNumberString",
            "/sms/**",
            "/",
            "/version",

    };

//    以下跨域无效，请使用注解@CrossOrigin
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//
//        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
//        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "PUT", "DELETE", "HEAD"));
//        configuration.setAllowedHeaders(Collections.singletonList(HeaderString));
//        configuration.setAllowCredentials(true);
//
//
//        configuration.addExposedHeader(HeaderString);
//        source.registerCorsConfiguration(ALL_PATTERN, configuration);
//
//        return source;
//    }
}
