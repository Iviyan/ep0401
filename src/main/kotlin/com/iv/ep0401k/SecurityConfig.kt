package com.iv.ep0401k

import com.iv.ep0401k.services.UserDetailService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class SecurityConfig {

    @Bean
    fun getPasswordEncoder(): PasswordEncoder = NoOpPasswordEncoder.getInstance()//BCryptPasswordEncoder(8)

    @Bean
    fun authManager(
        http: HttpSecurity,
        passwordEncoder: PasswordEncoder,
        userDetailService: UserDetailService?
    ): AuthenticationManager? {
        return http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .userDetailsService(userDetailService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build()
    }

    @Bean
    @Throws(java.lang.Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain? {
        http
            .authorizeRequests().antMatchers("/login", "/register").permitAll()
            .anyRequest().authenticated().and().formLogin().loginPage("/login").defaultSuccessUrl("/")
            .permitAll()
            .and().logout().permitAll().and().csrf().disable().cors().disable();
        return http.build()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer? = WebSecurityCustomizer { web: WebSecurity ->
        web.ignoring()
            .antMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico")
    }

    /*.csrf()
            .disable()
            .authorizeRequests()
            .antMatchers(HttpMethod.DELETE)
            .hasRole("ADMIN")
            .antMatchers("/admin/**")
            .hasAnyRole("ADMIN")
            .antMatchers("/user/**")
            .hasAnyRole("USER", "ADMIN")
            .antMatchers("/login/**")
            .anonymous()
            .anyRequest()
            .authenticated()
            .and()
            .httpBasic()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)*/*/*/*/

}