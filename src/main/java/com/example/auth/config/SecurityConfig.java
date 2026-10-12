package com.example.auth.config;
import com.example.auth.repository.UserRepository;
import org.springframework.context.annotation.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
@Configuration public class SecurityConfig {
@Bean PasswordEncoder passwordEncoder(){return new BCryptPasswordEncoder();}
@Bean UserDetailsService userDetailsService(UserRepository users){return email -> users.findByEmail(email.toLowerCase()).map(u->User.withUsername(u.email).password(u.password).roles("USER").build()).orElseThrow(()->new UsernameNotFoundException("User not found"));}
@Bean SecurityFilterChain filterChain(HttpSecurity http)throws Exception{return http.authorizeHttpRequests(a->a.requestMatchers("/","/login","/register","/forgot","/reset","/css/**","/js/**").permitAll().anyRequest().authenticated()).formLogin(f->f.loginPage("/login").loginProcessingUrl("/login").usernameParameter("email").defaultSuccessUrl("/dashboard",true).failureUrl("/login?error").permitAll()).logout(l->l.logoutSuccessUrl("/login?logout").permitAll()).build();}
}
