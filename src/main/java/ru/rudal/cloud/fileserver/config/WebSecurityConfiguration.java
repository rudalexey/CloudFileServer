package ru.rudal.cloud.fileserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.*;
import ru.rudal.cloud.fileserver.security.jwt.AuthenticationJwtTokenFilter;
import ru.rudal.cloud.fileserver.security.jwt.CustomAuthenticationEntryPoint;
import ru.rudal.cloud.fileserver.security.jwt.JwtUtils;
import ru.rudal.cloud.fileserver.security.service.CustomUserDetailsService;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService detailsService;
    private final CustomAuthenticationEntryPoint unauthorizedHandler;
    private final JwtUtils jwtUtils;
    public WebSecurityConfiguration(CustomUserDetailsService detailsService, CustomAuthenticationEntryPoint unauthorizedHandler, JwtUtils jwtUtils) {
        this.detailsService = detailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                /* .csrf()
                .ignoringAntMatchers("/login")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()*/
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/api/auth/login").permitAll()
                //TODO Убрать позже Swagger без авторизации
//                .antMatchers("/v2/api-docs").permitAll()
//				.antMatchers("/api/v2/api-docs").permitAll()
//                .antMatchers(HttpMethod.GET,"/swagger/index.html","/swagger/*.css","/swagger/*.js","/swagger/*.png","/swagger/*.map").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(detailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public AuthenticationJwtTokenFilter authenticationJwtTokenFilter() {
        return new AuthenticationJwtTokenFilter(detailsService, jwtUtils);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
