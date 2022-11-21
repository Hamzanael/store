package com.bloobloom.assessment.security

import com.bloobloom.assessment.security.jwt.JwtAuthenticationFilter
import com.bloobloom.assessment.security.jwt.JwtConfiguration
import com.bloobloom.assessment.security.jwt.JwtVerificationFilter
import com.bloobloom.assessment.security.jwt.JwtVerificationProcessor
import com.bloobloom.assessment.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.POST
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class SpringSecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var jwtConfiguration: JwtConfiguration

    @Autowired
    lateinit var verificationProcessor: JwtVerificationProcessor

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userService)
    }

    override fun configure(http: HttpSecurity) {
        http
            .cors().and()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilter(JwtAuthenticationFilter(authenticationManager(), jwtConfiguration))
            .addFilterAfter(
                JwtVerificationFilter(verificationProcessor),
                JwtAuthenticationFilter::class.java
            )
            .authorizeRequests()
            .antMatchers(POST, "/webhook")
            .permitAll()
            .antMatchers(
                "/swagger-ui/**",
                "/swagger-ui**",
                "/api-docs/**",
                "/api-docs**",
                "/api/swagger**",
                "/api/swagger-ui/**",
                "/api",
                "/static/**",
            )
            .permitAll()
            .anyRequest()
            .authenticated()
    }

    @Bean
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD", "TRACE", "CONNECT")
                    .allowedOrigins("*")
            }
        }
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder(6)
}