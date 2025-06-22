package com.humg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.humg.entity.Admin;
import com.humg.repository.AdminRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
	 private final AdminRepository adminRepository;
	    
	    public SecurityConfig(AdminRepository adminRepository) {
	        this.adminRepository = adminRepository;
	    }

	    @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .authorizeHttpRequests(auth -> auth
	                .requestMatchers("/", "/home", "/css/**", "/js/**", "/images/**", "/public/**").permitAll() // Cho phép truy cập không cần đăng nhập
	                .requestMatchers("/admin/**").hasRole("ADMIN") 
	                .anyRequest().permitAll() 
	            )
	            .formLogin(form -> form
	                .loginPage("/login")
	                .defaultSuccessUrl("/admin/dashboard")
	                .permitAll()
	            )
	            .logout(logout -> logout
	                    .logoutUrl("/logout")  // URL để kích hoạt đăng xuất
	                    .logoutSuccessUrl("/login?logout")  // Chuyển hướng sau khi đăng xuất
	                    .invalidateHttpSession(true)  // Hủy session
	                    .deleteCookies("JSESSIONID")  // Xóa cookie session
	                    .permitAll()
	                );
	        return http.build();
	    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Admin admin = adminRepository.findByUsername(username);
            if (admin == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return User.withUsername(admin.getUsername())
                       .password(admin.getPassword())
                       .roles("ADMIN")
                       .build();
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // Không mã hóa mật khẩu
    }
}